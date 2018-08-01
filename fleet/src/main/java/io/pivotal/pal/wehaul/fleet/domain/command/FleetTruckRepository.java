package io.pivotal.pal.wehaul.fleet.domain.command;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;

@NoRepositoryBean
public interface FleetTruckRepository extends Repository<FleetTruck, String> {
    FleetTruck save(FleetTruck fleetTruck);

    FleetTruck findOne(String vin);

    List<FleetTruck> findAll();
}