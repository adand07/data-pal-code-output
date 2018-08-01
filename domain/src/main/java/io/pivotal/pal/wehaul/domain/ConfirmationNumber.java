package io.pivotal.pal.wehaul.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class ConfirmationNumber implements Serializable {

    @Column(columnDefinition = "uuid")
    private final UUID confirmationNumber;

    ConfirmationNumber() {
        // default constructor required for JPA
        this.confirmationNumber = null;
    }

    private ConfirmationNumber(UUID confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public static ConfirmationNumber of(UUID confirmationNumber) {
        return new ConfirmationNumber(confirmationNumber);
    }

    public static ConfirmationNumber of(String confirmationNumber) {
        return new ConfirmationNumber(UUID.fromString(confirmationNumber));
    }

    public static ConfirmationNumber newId() {
        return ConfirmationNumber.of(UUID.randomUUID());
    }

    public UUID getConfirmationNumber() {
        return confirmationNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfirmationNumber that = (ConfirmationNumber) o;
        return Objects.equals(confirmationNumber, that.confirmationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confirmationNumber);
    }

    @Override
    public String toString() {
        return "ConfirmationNumber{" +
                "confirmationNumber=" + confirmationNumber +
                '}';
    }
}
