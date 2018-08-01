package io.pivotal.pal.wehaul.application;

import io.pivotal.pal.wehaul.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TruckSizeChart truckSizeChart() {
        return new TruckSizeChart();
    }

    @Bean
    public TruckAllocationService truckAllocationService(TruckRepository truckRepository) {
        return new TruckAllocationService(truckRepository);
    }

    @Bean
    public FleetService fleetService(TruckSizeChart truckSizeChart,
                                     TruckRepository truckRepository,
                                     TruckInspectionRepository truckInspectionRepository) {
        return new FleetService(
                truckSizeChart,
                truckRepository,
                truckInspectionRepository
        );
    }

    @Bean
    public RentalService rentalService(TruckAllocationService truckAllocationService,
                                       RentalRepository rentalRepository,
                                       TruckRepository truckRepository) {
        return new RentalService(truckAllocationService, rentalRepository, truckRepository);
    }
}
