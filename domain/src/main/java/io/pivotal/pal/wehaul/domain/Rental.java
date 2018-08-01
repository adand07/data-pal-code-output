package io.pivotal.pal.wehaul.domain;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "rental")
public class Rental {

    @EmbeddedId
    private ConfirmationNumber confirmationNumber;

    @Column(nullable = false)
    private String customerName;

    @Column
    private Vin truckVin;

    @Column
    private Integer distanceTraveled;

    Rental() {
        // default constructor required by JPA
    }

    public Rental(String customerName, Vin truckVin) {
        this.customerName = customerName;
        this.confirmationNumber = ConfirmationNumber.newId();
        this.truckVin = truckVin;
    }

    public void pickUp() {
        if (distanceTraveled != null) {
            throw new IllegalStateException("Rental has already been picked up");
        }

        distanceTraveled = 0;
    }

    public void dropOff(int distanceTraveled) {
        if (this.distanceTraveled == null) {
            throw new IllegalStateException("Cannot drop off before picking up rental");
        }
        if (this.distanceTraveled != 0) {
            throw new IllegalStateException("Rental is already dropped off");
        }

        this.distanceTraveled = distanceTraveled;
    }

    public ConfirmationNumber getConfirmationNumber() {
        return confirmationNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Vin getTruckVin() {
        return truckVin;
    }

    public Integer getDistanceTraveled() {
        return distanceTraveled;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "confirmationNumber=" + confirmationNumber +
                ", customerName='" + customerName + '\'' +
                ", truckVin='" + truckVin + '\'' +
                ", distanceTraveled=" + distanceTraveled +
                '}';
    }
}
