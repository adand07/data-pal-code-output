package io.pivotal.pal.wehaul.domain;

public class TruckSizeChart {

    public TruckSize getSizeByTruckLength(int truckLength) {
        return (truckLength >= 20) ? TruckSize.LARGE : TruckSize.SMALL;
    }
}
