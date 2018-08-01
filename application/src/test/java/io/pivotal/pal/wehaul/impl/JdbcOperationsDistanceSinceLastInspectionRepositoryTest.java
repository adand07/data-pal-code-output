package io.pivotal.pal.wehaul.impl;

import io.pivotal.pal.wehaul.fleet.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@DirtiesContext
@RunWith(SpringRunner.class)
@DataJpaTest
@Import(JdbcOperationsDistanceSinceLastInspectionRepository.class)
public class JdbcOperationsDistanceSinceLastInspectionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DistanceSinceLastInspectionRepository distanceSinceLastInspectionRepository;

    @Mock
    private TruckInfoLookupClient mockTruckInfoLookupClient;

    @Test
    public void findAllSinceInspection() {
        String vin = "test-0001";
        when(mockTruckInfoLookupClient.getMakeModelByVin(any())).thenReturn(new MakeModel("make", "model"));
        FleetTruck truck = new FleetTruck.Factory(mockTruckInfoLookupClient).buyTruck("test-0001", 4000);
        TruckInspection truckInspection =
                TruckInspection.createTruckInspection(vin, 2000, "bad stuff");
        truck.getInspections().add(truckInspection);
        entityManager.persistAndFlush(truck);

        Collection<DistanceSinceLastInspection> distanceSinceLastInspections =
                distanceSinceLastInspectionRepository.findAll();

        assertThat(distanceSinceLastInspections)
                .hasSize(1)
                .extracting(DistanceSinceLastInspection::getTruckVin)
                .containsExactly(vin);
        assertThat(distanceSinceLastInspections)
                .extracting(DistanceSinceLastInspection::getDistanceSinceLastInspection)
                .containsExactly(2000);
    }
}
