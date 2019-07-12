package com.shadcanard.redcraft.common.tools;

import net.minecraftforge.energy.EnergyStorage;

public class ConsumerEnergyStorage extends EnergyStorage {
    public ConsumerEnergyStorage(int capacity, int maxReceive) {
        super(capacity, maxReceive);
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    public void consumePower(int energy){
        this.energy -= energy;
        if(this.energy < 0) {
            this.energy = 0;
        }
    }

    public int getCapacity(){
        return this.capacity;
    }
}
