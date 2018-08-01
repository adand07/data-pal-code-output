package io.pivotal.pal.wehaul.domain;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RentalService {

    private final TruckAllocationService truckAllocationService;
    private final RentalRepository rentalRepository;
    private final TruckRepository truckRepository;

    public RentalService(TruckAllocationService truckAllocationService,
                         RentalRepository rentalRepository,
                         TruckRepository truckRepository) {
        this.truckAllocationService = truckAllocationService;
        this.rentalRepository = rentalRepository;
        this.truckRepository = truckRepository;
    }

    @Transactional
    public void create(String customerName, TruckSize truckSize) {

        // business invariant that applies across many trucks
        // domain logic in transaction script app service
        List<Truck> availableTrucks =
                truckRepository.findAllByTruckSizeAndStatus(truckSize, TruckStatus.RENTABLE);
        if (availableTrucks.size() < 1) {
            throw new IllegalStateException("No trucks available to rent");
        }

        Truck truck = availableTrucks.get(0);

        truck.reserve();
        truckRepository.save(truck);

        Rental rental = new Rental(customerName, truck.getVin());
        rentalRepository.save(rental);
    }

    @Transactional
    public void pickUp(ConfirmationNumber confirmationNumber) {
        Rental rental = rentalRepository.findOne(confirmationNumber);
        if (rental == null) {
            throw new IllegalArgumentException(String.format("No rental found for id=%s", confirmationNumber));
        }
        if (rental.getDistanceTraveled() != null) {
            throw new IllegalStateException("Rental has already been picked up");
        }

        rental.setDistanceTraveled(0);

        rentalRepository.save(rental);

        Truck truck = truckRepository.findOne(rental.getTruckVin());
        if (truck.getStatus() != TruckStatus.RESERVED) {
            throw new IllegalStateException("Only reserved trucks can be picked up");
        }

        truck.setStatus(TruckStatus.RENTED);
        truckRepository.save(truck);
    }

    @Transactional
    public void dropOff(ConfirmationNumber confirmationNumber, int distanceTraveled) {
        Rental rental = rentalRepository.findOne(confirmationNumber);
        if (rental == null) {
            throw new IllegalArgumentException(String.format("No rental found for id=%s", confirmationNumber));
        }
        if (rental.getDistanceTraveled() == null) {
            throw new IllegalStateException("Cannot drop off before picking up rental");
        }
        if (rental.getDistanceTraveled() != 0) {
            throw new IllegalStateException("Rental is already dropped off");
        }

        rental.setDistanceTraveled(distanceTraveled);

        rentalRepository.save(rental);

        Vin vin = rental.getTruckVin();
        Truck truck = truckRepository.findOne(vin);

        int odometerReading = truck.getOdometerReading() + distanceTraveled;

        if (truck.getStatus() != TruckStatus.RENTED) {
            throw new IllegalStateException("Truck is not currently rented");
        }
        if (truck.getOdometerReading() > odometerReading) {
            throw new IllegalArgumentException("Odometer reading cannot be less than previous reading");
        }

        truck.setStatus(TruckStatus.RENTABLE);
        truck.setOdometerReading(odometerReading);

        truckRepository.save(truck);
    }

    public Collection<Rental> findAll() {

        return StreamSupport.stream(rentalRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
