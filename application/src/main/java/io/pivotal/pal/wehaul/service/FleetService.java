package io.pivotal.pal.wehaul.service;

import io.pivotal.pal.wehaul.fleet.domain.DistanceSinceLastInspection;
import io.pivotal.pal.wehaul.fleet.domain.DistanceSinceLastInspectionRepository;
import io.pivotal.pal.wehaul.fleet.domain.FleetTruck;
import io.pivotal.pal.wehaul.fleet.domain.FleetTruckRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FleetService {

    private final FleetTruckRepository fleetTruckRepository;
    private final DistanceSinceLastInspectionRepository distanceSinceLastInspectionRepository;
    private final FleetTruck.Factory fleetTruckFactory;

    public FleetService(
            FleetTruck.Factory fleetTruckFactory,
            FleetTruckRepository fleetTruckRepository,
            DistanceSinceLastInspectionRepository distanceSinceLastInspectionRepository
    ) {
        this.fleetTruckFactory = fleetTruckFactory;
        this.fleetTruckRepository = fleetTruckRepository;
        this.distanceSinceLastInspectionRepository = distanceSinceLastInspectionRepository;
    }

    public FleetTruck buyTruck(String vin, int odometerReading) {
        FleetTruck truck = fleetTruckFactory.buyTruck(vin, odometerReading);

        fleetTruckRepository.save(truck);
        return truck;
    }

    public void returnFromInspection(String vin, String notes, int odometerReading) {
        FleetTruck truck = fleetTruckRepository.findOne(vin);

        if (truck == null) {
            throw new IllegalArgumentException(String.format("No truck found with VIN=%s", vin));
        }

        truck.returnFromInspection(notes, odometerReading);
        fleetTruckRepository.save(truck);
    }

    public void sendForInspection(String vin) {
        FleetTruck truck = fleetTruckRepository.findOne(vin);

        if (truck == null) {
            throw new IllegalArgumentException(String.format("No truck found with VIN=%s", vin));
        }

        truck.sendForInspection();

        fleetTruckRepository.save(truck);
    }

    public void removeFromYard(String vin) {
        FleetTruck truck = fleetTruckRepository.findOne(vin);

        if (truck == null) {
            throw new IllegalArgumentException(String.format("No truck found with VIN=%s", vin));
        }

        truck.removeFromYard();

        fleetTruckRepository.save(truck);
    }

    public void returnToYard(String vin, int distanceTraveled) {
        FleetTruck truck = fleetTruckRepository.findOne(vin);

        if (truck == null) {
            throw new IllegalArgumentException(String.format("No truck found with VIN=%s", vin));
        }

        truck.returnToYard(distanceTraveled);

        fleetTruckRepository.save(truck);
    }

    public Collection<DistanceSinceLastInspection> findAllDistanceSinceLastInspections() {
        return distanceSinceLastInspectionRepository.findAllDistanceSinceLastInspections();
    }

    public Collection<FleetTruck> findAll() {

        return StreamSupport
            .stream(fleetTruckRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }
}