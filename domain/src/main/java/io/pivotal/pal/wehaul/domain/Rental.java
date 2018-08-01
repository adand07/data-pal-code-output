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
        // TODO: implement for lab exercise
    }

    public void dropOff(int distanceTraveled) {
        // TODO: implement for lab exercise
    }

    public ConfirmationNumber getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(ConfirmationNumber confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Vin getTruckVin() {
        return truckVin;
    }

    public void setTruckVin(Vin truckVin) {
        this.truckVin = truckVin;
    }

    public Integer getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(Integer distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
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
