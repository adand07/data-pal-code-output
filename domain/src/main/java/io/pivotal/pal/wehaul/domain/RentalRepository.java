package io.pivotal.pal.wehaul.domain;

import org.springframework.data.repository.CrudRepository;

public interface RentalRepository extends CrudRepository<Rental, ConfirmationNumber> {

}
