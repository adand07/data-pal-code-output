package io.pivotal.pal.wehaul.domain;

public class TruckAllocationService {

    private final TruckRepository truckRepository;

    public TruckAllocationService(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    public Truck allocateTruck(TruckSize truckSize) {
        // TODO: implement for lab exercise
        return null;
    }
}
