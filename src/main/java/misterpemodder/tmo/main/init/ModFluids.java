package misterpemodder.tmo.main.init;

import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.fluids.BlockFluidTMO;
import misterpemodder.tmo.main.fluids.FluidTMO;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class ModFluids {
	
	public static enum TheFluids {
		
		IGNUM_FUEL(new FluidTMO(EnumBlocksNames.IGNUM_FUEL, 2473, 0xFFFF6F3D, 3, 800)),
		FROZIUM_FUEL(new FluidTMO(EnumBlocksNames.FROZIUM_FUEL, 123, 0xFF90FFFF, 0, 800)),
		;
		
		private final FluidTMO declaredFluid;
		public Fluid fluid = null; 
		public BlockFluidTMO block = null;
		
		public FluidTMO getDeclaredFluid() {
			return declaredFluid;
		}
		
		public Fluid getFluid() {
			return fluid;
		}
		
		public BlockFluidTMO getBlock() {
			return block;
		}
		
		TheFluids(FluidTMO fluid) {
			this.declaredFluid = fluid;
		}
		
	}
	
	
	public static void registerFluids() {
		TMORefs.LOGGER.info("Registering Fluids...");
		for(TheFluids f : TheFluids.values()) {
			FluidTMO fluid = f.getDeclaredFluid();
			
			FluidRegistry.registerFluid(fluid);
			f.fluid = FluidRegistry.getFluid(fluid.getName());
			FluidRegistry.addBucketForFluid(fluid);
			
			IBlockNames names = fluid.getNames();
			
			BlockFluidTMO block = new BlockFluidTMO(fluid, Material.WATER);
			
			block.setRegistryName(names.getRegistryName());
			block.setUnlocalizedName(names.getUnlocalizedName());
			
			GameRegistry.register(block);
			f.block = block;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerFluidBlocksRendering() {
		for(TheFluids f : TheFluids.values()) {
			if(f.getFluid() != null) {
				registerFluidBlockRendering(f.getFluid());
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerFluidBlockRendering(Fluid fluid) {

		FluidStateMapper mapper = new FluidStateMapper(fluid);
	    Block block = fluid.getBlock();
//	    Item item = Item.getItemFromBlock(block);
//
//	    // item-model
//	    if (item != null) {
//	      ModelLoader.registerItemVariants(item);
//	      ModelLoader.setCustomMeshDefinition(item, mapper);
//	    }
//	    // block-model
	    if (block != null) {
	      ModelLoader.setCustomStateMapper(block, mapper);
	    }
	}
	
	public static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

	    public final Fluid fluid;
	    public final ModelResourceLocation location;

	    public FluidStateMapper(Fluid fluid) {
	      this.fluid = fluid;
	      location = new ModelResourceLocation(TMORefs.PREFIX + "fluids", fluid.getName());
	    }

	    @Override
	    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
	      return location;
	    }

	    @Override
	    public ModelResourceLocation getModelLocation(ItemStack stack) {
	      return location;
	    }
	    
	}

}