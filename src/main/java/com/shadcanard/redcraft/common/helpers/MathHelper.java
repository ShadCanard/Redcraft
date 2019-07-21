package com.shadcanard.redcraft.common.helpers;

public class MathHelper {

    public static int ValueFromPercentage(int min, int max, int percentage){
        int delta = max - min;
        return (delta * percentage / 100);
    }
}
