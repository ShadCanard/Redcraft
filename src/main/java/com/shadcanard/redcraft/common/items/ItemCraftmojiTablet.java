package com.shadcanard.redcraft.common.items;

import com.shadcanard.redcraft.common.RedCraft;
import com.shadcanard.redcraft.common.helpers.Names;
import com.shadcanard.redcraft.common.helpers.References;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ItemCraftmojiTablet extends ItemBase {

    public ItemCraftmojiTablet() {
        super(Names.Items.CRAFTMOJI_TABLET);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nullable EnumHand handIn) {
        BlockPos pos = playerIn.getPosition();
        playerIn.openGui(RedCraft.instance, References.GUI_CRAFTMOJI_TABLET, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return super.onItemRightClick(worldIn, playerIn, Objects.requireNonNull(handIn));
    }
}
