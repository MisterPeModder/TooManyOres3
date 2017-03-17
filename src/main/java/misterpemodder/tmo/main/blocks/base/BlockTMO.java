package misterpemodder.tmo.main.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;

public interface BlockTMO {
	
	default void registerItemRender() {
		ModelResourceLocation location = new ModelResourceLocation(((Block)this).getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this.getItemBlock(), 0, location);
	}
	
	default void registerOreDict(){}
	
	default boolean hasOwnItemBlock() {
		return true;
	}
	
	default ItemBlock getItemBlock() {
		return null;
	}
	
}
