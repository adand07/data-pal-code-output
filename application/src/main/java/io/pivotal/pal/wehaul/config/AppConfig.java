package io.pivotal.pal.wehaul.config;

import io.pivotal.pal.wehaul.fleet.FleetTruckEventSourcedRepository;
import io.pivotal.pal.wehaul.fleet.FleetTruckEventStoreRepository;
import io.pivotal.pal.wehaul.fleet.domain.command.FleetTruckRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public FleetTruckRepository eventPublishingFleetTruckRepository(FleetTruckEventStoreRepository eventStoreRepository,
                                                                    ApplicationEventPublisher applicationEventPublisher) {

        return new FleetTruckEventSourcedRepository(eventStoreRepository, applicationEventPublisher);
    }
}
