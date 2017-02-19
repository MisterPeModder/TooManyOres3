package misterpemodder.tmo.main.client.render;

import misterpemodder.tmo.main.init.ModBlocks;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;

public class TileEntityTitaniumChestInvRenderer extends TileEntityItemStackRenderer{
	
	private TileEntityTitaniumChest tileEntityTitaniumChest = new TileEntityTitaniumChest();
	
	@Override
	public void renderByItem(ItemStack itemStack) {
		Block block = Block.getBlockFromItem(itemStack.getItem());
		if (block == ModBlocks.TheBlocks.TITANIUM_CHEST.getBlock()) {
			TileEntityRendererDispatcher.instance.renderTileEntityAt(this.tileEntityTitaniumChest, 0.0D, 0.0D, 0.0D, 0.0F);
		} else {
			super.renderByItem(itemStack);
		}
	}

}
