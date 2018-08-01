package io.pivotal.pal.wehaul.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table
public class TruckInspection {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column
    private Vin truckVin;

    @Column
    private Integer odometerReading;

    @Column
    private String notes;

    TruckInspection() {
        // default constructor required by JPA
    }

    public TruckInspection(Vin truckVin, int odometerReading, String notes) {
        this.id = UUID.randomUUID();
        this.truckVin = truckVin;
        this.odometerReading = odometerReading;
        this.notes = notes;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Vin getTruckVin() {
        return truckVin;
    }

    public void setTruckVin(Vin truckVin) {
        this.truckVin = truckVin;
    }

    public Integer getOdometerReading() {
        return odometerReading;
    }

    public void setOdometerReading(Integer odometerReading) {
        this.odometerReading = odometerReading;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "TruckInspection{" +
                "id=" + id +
                ", truckVin=" + truckVin +
                ", odometerReading=" + odometerReading +
                ", notes='" + notes + '\'' +
                '}';
    }
}
