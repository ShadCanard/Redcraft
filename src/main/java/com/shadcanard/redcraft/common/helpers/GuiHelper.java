package com.shadcanard.redcraft.common.helpers;

public class GuiHelper {

    public static int PROGRESS_BAR_COLOR = 0xff40cf40;
    public static int ENERGY_BAR_COLOR = 0xffdf0000;

    public static int BACKGROUND_BAR_COLOR = 0xff777777;

    public static int GetBarEndX(int backgroundStartX, int percentage, int backgroundEndX){
        return MathHelper.ValueFromPercentage(backgroundStartX,backgroundEndX,percentage);
    }
}
