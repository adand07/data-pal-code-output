package io.pivotal.pal.wehaul.domain;

import javax.persistence.*;

@Entity
@Table
public class Truck {

    @EmbeddedId
    private Vin vin;

    @Enumerated(EnumType.STRING)
    @Column
    private TruckStatus status;

    @Column
    private Integer odometerReading;

    @Enumerated(EnumType.STRING)
    @Column
    private TruckSize truckSize;

    @Column
    private Integer truckLength;

    Truck() {
        // default constructor required by JPA
    }

    public Truck(Vin vin, Integer odometerReading, TruckSize truckSize, Integer truckLength) {
        if (odometerReading < 0) {
            throw new IllegalArgumentException("Cannot buy a truck with negative odometer reading");
        }

        this.vin = vin;
        this.status = TruckStatus.RENTABLE;
        this.odometerReading = odometerReading;
        this.truckSize = truckSize;
        this.truckLength = truckLength;
    }

    public void returnFromInspection(int odometerReading) {
        if (status != TruckStatus.IN_INSPECTION) {
            throw new IllegalStateException("Truck is not currently in inspection");
        }
        if (this.odometerReading > odometerReading) {
            throw new IllegalArgumentException("Odometer reading cannot be less than previous reading");
        }

        this.status = TruckStatus.RENTABLE;
        this.odometerReading = odometerReading;
    }

    public void reserve() {
        if (status != TruckStatus.RENTABLE) {
            throw new IllegalStateException("Truck cannot be reserved");
        }

        this.status = TruckStatus.RESERVED;
    }

    public void pickUp() {
        if (status != TruckStatus.RESERVED) {
            throw new IllegalStateException("Only reserved trucks can be picked up");
        }

        this.status = TruckStatus.RENTED;
    }

    public void returnToService(int odometerReading) {
        if (status != TruckStatus.RENTED) {
            throw new IllegalStateException("Truck is not currently rented");
        }
        if (this.odometerReading > odometerReading) {
            throw new IllegalArgumentException("Odometer reading cannot be less than previous reading");
        }

        this.status = TruckStatus.RENTABLE;
        this.odometerReading = odometerReading;
    }

    public void sendForInspection() {
        if (status != TruckStatus.RENTABLE) {
            throw new IllegalStateException("Truck cannot be inspected");
        }

        this.status = TruckStatus.IN_INSPECTION;
    }

    public Vin getVin() {
        return vin;
    }

    public TruckStatus getStatus() {
        return status;
    }

    public Integer getOdometerReading() {
        return odometerReading;
    }

    public TruckSize getTruckSize() {
        return truckSize;
    }

    public Integer getTruckLength() {
        return truckLength;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "vin=" + vin +
                ", status=" + status +
                ", odometerReading=" + odometerReading +
                ", truckSize=" + truckSize +
                ", truckLength=" + truckLength +
                '}';
    }
}
