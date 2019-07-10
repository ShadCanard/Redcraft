package com.shadcanard.redcraft.common.items;

import com.shadcanard.redcraft.common.helpers.Names;

public class ItemIronBowl extends ItemBase {
    public ItemIronBowl() {
        super(Names.Items.IRON_BOWL);
        setContainerItem(this);
        setMaxStackSize(1);
    }
}
