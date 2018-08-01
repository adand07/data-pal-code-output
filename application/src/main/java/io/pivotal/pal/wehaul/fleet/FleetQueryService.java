package io.pivotal.pal.wehaul.fleet;

import io.pivotal.pal.wehaul.fleet.domain.query.FleetTruckQueryRepository;
import io.pivotal.pal.wehaul.fleet.domain.query.FleetTruckSnapshot;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class FleetQueryService {
    private final FleetTruckQueryRepository fleetTruckQueryRepository;

    public FleetQueryService(FleetTruckQueryRepository fleetTruckQueryRepository) {
        this.fleetTruckQueryRepository = fleetTruckQueryRepository;
    }

    public Collection<FleetTruckSnapshot> findAll() {
        return Collections.emptyList();
    }

    public FleetTruckSnapshot findOne(String vin) {
        return null;
    }
}
