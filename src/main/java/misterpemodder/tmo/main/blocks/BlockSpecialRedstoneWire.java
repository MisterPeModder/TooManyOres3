package misterpemodder.tmo.main.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import misterpemodder.tmo.main.blocks.base.IBlockTMO;
import misterpemodder.tmo.main.blocks.itemblock.ItemBlockSpecialRedstone;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpecialRedstoneWire extends BlockRedstoneWire implements IBlockTMO {
	
	private final ItemBlockSpecialRedstone itemBlock;
	private final EnumBlocksNames names;

	public BlockSpecialRedstoneWire(EnumBlocksNames names) {
		
		this.names = names;
		
		this.setUnlocalizedName(names.getUnlocalizedName());
		this.setRegistryName(TMORefs.PREFIX + names.getRegistryName());
		this.setLightOpacity(0);
		
		this.itemBlock = new ItemBlockSpecialRedstone(this);
		itemBlock.setRegistryName(this.getRegistryName());
		this.setCreativeTab(TMORefs.TMO_TAB);
	}
	
	@Override
	public ItemBlock getItemBlock() {
		return this.itemBlock;
	}
	
	public IBlockNames getNames() {
		return this.names;
	}
	
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItemBlock());
    }
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this.getItemBlock();
    }
	
	@SideOnly(Side.CLIENT)
	public static void registerColorHandler() {
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor()
        {
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
            {
                return BlockSpecialRedstoneWire.getWireColorMultiplier(state.getValue(BlockRedstoneWire.POWER).intValue(), (BlockSpecialRedstoneWire)state.getBlock());
            }
        }, TheBlocks.TITANIUM_REDSTONE.getBlock(), TheBlocks.COPPER_REDSTONE.getBlock());
	}
	
	@SideOnly(Side.CLIENT)
	public static int getWireColorMultiplier(int power, BlockSpecialRedstoneWire block) {
		
		float f = power / 15.0F;
		float r = 0.0F;
		float g = 0.0F;
		float b = 0.0F;
		
		if(block == TheBlocks.TITANIUM_REDSTONE.getBlock()) {
			 r = f * 0.2F + 0.15F;
			 b = f * 0.6F + 0.2F;
			 
		} 
		else if(block == TheBlocks.COPPER_REDSTONE.getBlock()) {
			 r = f * 0.55F + 0.45F;
			 g = f * 0.3F + 0.1F;
			 
		}
		
        int i = MathHelper.clamp((int)(r * 255.0F), 0, 255); //XX----
        int j = MathHelper.clamp((int)(g * 255.0F), 0, 255); //--XX--
        int k = MathHelper.clamp((int)(b * 255.0F), 0, 255); //----XX
        
        //-16777216 -> HEX: 1000000 (alpha channel)
        //TODO Change colors
        return -16777216 | i << 16 | j << 8 | k;
	}

}
