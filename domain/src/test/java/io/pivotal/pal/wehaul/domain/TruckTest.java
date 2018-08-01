package io.pivotal.pal.wehaul.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(MockitoJUnitRunner.class)
public class TruckTest {

    @Test
    public void buyTruck() {
        Vin vin = Vin.of("test-0001");
        int odometerReading = 0;
        TruckSize truckSize = TruckSize.LARGE;
        int truckLength = 25;


        Truck truck = new Truck(vin, odometerReading, truckSize, truckLength);


        assertThat(truck.getVin()).isEqualTo(Vin.of("test-0001"));
        assertThat(truck.getStatus()).isEqualTo(TruckStatus.RENTABLE);
        assertThat(truck.getOdometerReading()).isEqualTo(odometerReading);
        assertThat(truck.getTruckSize()).isEqualTo(truckSize);
    }

    @Test
    public void sendForInspection() {
        Truck truck = new Truck(Vin.of("test-0001"), 0, null, null);


        truck.sendForInspection();


        assertThat(truck.getStatus()).isEqualTo(TruckStatus.IN_INSPECTION);
    }

    @Test
    public void returnFromInspection() {
        Truck truck = new Truck(Vin.of("test-0001"), 0, null, null);
        truck.sendForInspection();
        int odometerReading = 1;


        truck.returnFromInspection(odometerReading);


        assertThat(truck.getStatus()).isEqualTo(TruckStatus.RENTABLE);
        assertThat(truck.getOdometerReading()).isEqualTo(odometerReading);
    }

    @Test
    public void reserve() {
        Truck truck = new Truck(Vin.of("test-0001"), 0, null, null);


        truck.reserve();


        assertThat(truck.getStatus()).isEqualTo(TruckStatus.RESERVED);
    }

    @Test
    public void pickUp() {
        Truck truck = new Truck(Vin.of("test-0001"), 0, null, null);
        truck.reserve();


        truck.pickUp();


        assertThat(truck.getStatus()).isEqualTo(TruckStatus.RENTED);
    }

    @Test
    public void returnToService() {
        Truck truck = new Truck(Vin.of("test-0001"), 0, null, null);
        truck.reserve();
        truck.pickUp();
        int odometerReading = 101;


        truck.returnToService(odometerReading);


        assertThat(truck.getStatus()).isEqualTo(TruckStatus.RENTABLE);
        assertThat(truck.getOdometerReading()).isEqualTo(odometerReading);
    }

    @Test
    public void buyTruck_negativeOdometer() {
        Vin vin = Vin.of("test-0001");
        int odometerReading = -10;
        int truckLength = 25;


        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    new Truck(vin, odometerReading, TruckSize.LARGE, truckLength);
                })
                .withMessage("Cannot buy a truck with negative odometer reading");
    }

    @Test
    public void sendForInspection_whenNotRentable() {
        Truck truck = new Truck(Vin.of("test-0001"), 0, null, null);
        truck.sendForInspection();


        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> truck.sendForInspection())
                .withMessage("Truck cannot be inspected");
    }

    @Test
    public void returnFromInspection_whenNotInInspection() {
        Truck truck = new Truck(Vin.of("test-0001"), 0, null, null);


        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> truck.returnFromInspection(100))
                .withMessage("Truck is not currently in inspection");
    }

    @Test
    public void returnFromInspection_withLowerOdometerReading() {
        Truck truck = new Truck(Vin.of("test-0001"), 100, null, null);
        truck.sendForInspection();


        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> truck.returnFromInspection(0))
                .withMessage("Odometer reading cannot be less than previous reading");
    }

    @Test
    public void reserveTruck_whenNotRentable() {
        Truck truck = new Truck(Vin.of("test-0001"), 0, null, null);
        truck.sendForInspection();


        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> truck.reserve())
                .withMessage("Truck cannot be reserved");
    }

    @Test
    public void pickUp_whenNotReserved() {
        Truck truck = new Truck(Vin.of("test-0001"), 0, null, null);


        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> truck.pickUp())
                .withMessage("Only reserved trucks can be picked up");
    }

    @Test
    public void returnToService_withLowerOdometerReading() {
        Truck truck = new Truck(Vin.of("test-0001"), 100, null, null);
        truck.reserve();
        truck.pickUp();


        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> truck.returnToService(99))
                .withMessage("Odometer reading cannot be less than previous reading");
    }

    @Test
    public void returnToService_whenNotRented() {
        Truck truck = new Truck(Vin.of("test-0001"), 100, null, null);


        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> truck.returnToService(101))
                .withMessage("Truck is not currently rented");
    }
}
