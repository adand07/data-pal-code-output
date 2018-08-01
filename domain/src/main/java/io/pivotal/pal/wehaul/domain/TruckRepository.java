package io.pivotal.pal.wehaul.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TruckRepository extends CrudRepository<Truck, Vin> {

    List<Truck> findAllByTruckSizeAndStatus(TruckSize truckSize, TruckStatus truckStatus);
}
