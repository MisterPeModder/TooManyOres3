/**
 * Copyright (c) 2011-2014, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package buildcraft.api.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

/***
 * Implement this interface on subclasses of Item to have that item work as a wrench for buildcraft
 */
public interface IToolWrench {

    /***
     * Called to ensure that the wrench can be used. To get the ItemStack that is used, check player.inventory.getCurrentItem()
     *
     * @param player
     *            - The player doing the wrenching
     * @param pos
     *
     * @return true if wrenching is allowed, false if not
     */
    boolean canWrench(EntityPlayer player, BlockPos pos);

    /***
     * Callback after the wrench has been used. This can be used to decrease durability or for other purposes. To get the ItemStack that was used, check
     * player.inventory.getCurrentItem()
     *
     * @param player
     *            - The player doing the wrenching
     * @param pos
     */
    void wrenchUsed(EntityPlayer player, BlockPos pos);

    boolean canWrench(EntityPlayer player, Entity entity);

    void wrenchUsed(EntityPlayer player, Entity entity);

}