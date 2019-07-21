package com.shadcanard.redcraft.common.tools;

import net.minecraftforge.energy.EnergyStorage;

public class RedcraftEnergyStorage extends EnergyStorage {
    public RedcraftEnergyStorage(int capacity, int maxReceive) {
        super(capacity, maxReceive);
    }

    public RedcraftEnergyStorage(int capacity){
        super(capacity);
    }

    public RedcraftEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
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

    public void generatePower(int energy) {
        this.energy += energy;
        if(this.energy > capacity) this.energy = capacity;
    }
}
