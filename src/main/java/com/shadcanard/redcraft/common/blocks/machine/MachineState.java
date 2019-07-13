package com.shadcanard.redcraft.common.blocks.machine;

import net.minecraft.util.IStringSerializable;

/**
 * Enumerates the different states of a machine
 */
public enum MachineState implements IStringSerializable {
    /**
     * The machine is not working
     */
    OFF("off"),

    /**
     * The machine is working
     */
    WORKING("on");

    /**
     * Optimization of the getting values (it's no longer calculated each time the values are needed
     */
    public static final MachineState[] VALUES = MachineState.values();

    /**
     * Name of the state
     */
    private final String name;

    /**
     * Constructor by default
     * @param name Name of the state
     */
    MachineState(String name) {this.name = name;}

    /**
     * Returns the state's name of a machine
     * @return State string
     */
    @Override
    public String getName() {
        return name;
    }
}
