package misterpemodder.tmo.main.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.items.base.ItemBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.DispenseFluidContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fluids.capability.templates.VoidFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.BlockLiquidWrapper;
import net.minecraftforge.fluids.capability.wrappers.BlockWrapper;
import net.minecraftforge.fluids.capability.wrappers.FluidBlockWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTitaniumBucket extends ItemBase {
	
	public static final String EMPTY_UNLOC_NAME = ".empty";
	public static final String FILLED_UNLOC_NAME = ".filled";
	
	public ItemTitaniumBucket() {
		super(EnumItemsNames.TITANIUM_BUCKET);
		this.setMaxStackSize(1);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, DispenseFluidContainer.getInstance());
	}
	
	
	public int getCapacity() {
		return ConfigValues.IntValues.BUCKET_CAPACITY.currentValue;
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(@Nonnull Item itemIn, @Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
		//empty bucket
		subItems.add(new ItemStack(itemIn));
		
		//filled buckets 
        for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
        	FluidStack fs = new FluidStack(fluid, getCapacity());
            ItemStack stack = new ItemStack(this);
            IFluidHandlerItem fluidHandler = new FluidHandlerItemStack(stack, getCapacity());
            if (fluidHandler.fill(fs, true) == fs.amount) {
            	ItemStack filled = fluidHandler.getContainer();
                subItems.add(filled);
            }
        }
    }
	
	private static boolean canEditFluid(World world, EntityPlayer player, BlockPos pos, EnumFacing sideHit, ItemStack stack) {
		if (world.isBlockModifiable(player, pos) && player.canPlayerEdit(pos.offset(sideHit), sideHit, stack)) {
    		return true;
		}
		return false;
	}
	
	@Override
    @Nonnull
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        FluidStack fluidStack = FluidUtil.getFluidContained(stack);
        String unloc = this.getUnlocalizedNameInefficiently(stack);
        if (fluidStack == null) {
        	return Tmo.proxy.translate(unloc + EMPTY_UNLOC_NAME);
        }

        return Tmo.proxy.translate(unloc + FILLED_UNLOC_NAME, fluidStack.amount, fluidStack.getFluid().getLocalizedName(fluidStack));
    }
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		
		FluidStack fluidStack = FluidUtil.getFluidContained(stack);
		String str = "";
		
		if(fluidStack == null || fluidStack.amount == 0) {
			str = Tmo.proxy.translate("item.titaniumBucket.desc.emptyContent");
		} else {
			str = Tmo.proxy.translate("item.titaniumBucket.desc.content", fluidStack.getFluid().getLocalizedName(fluidStack), fluidStack.amount, getCapacity());
		}
		
		tooltip.add(TextFormatting.GREEN+str);
		super.addInformation(stack, playerIn, tooltip, advanced);
	}
	
	@Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        FluidStack fluidStack = FluidUtil.getFluidContained(itemstack);
        IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(itemstack);
        
        RayTraceResult rtrSolid = this.rayTrace(world, player, false);
        RayTraceResult rtrLiquid = this.rayTrace(world, player, true);
        
        boolean creative = player.capabilities.isCreativeMode;
        
    	if(player.isSneaking()) {
    		if(!(fluidStack == null || fluidStack.amount + Fluid.BUCKET_VOLUME <= getCapacity())) {
    			player.sendStatusMessage(new TextComponentString(TextFormatting.DARK_RED+Tmo.proxy.translate("item.titaniumBucket.message.notEnougthCapacity")), true);
    			return ActionResult.newResult(EnumActionResult.PASS, itemstack);
    		}
    		RayTraceResult rtr = rtrSolid;
    		BlockPos pos = null;
    		BlockPos offsetPos = null;
    		if(rtrSolid == null || rtrSolid.typeOfHit != RayTraceResult.Type.BLOCK) {
    			if(rtrLiquid == null || rtrLiquid.typeOfHit != RayTraceResult.Type.BLOCK) {    				
    				return ActionResult.newResult(EnumActionResult.PASS, itemstack);
    			} else {
    				rtr = rtrLiquid;
    				pos = rtr.getBlockPos();
    				offsetPos = pos;
    			}
        	} else {
    			pos = rtr.getBlockPos();
        		offsetPos = pos.offset(rtr.sideHit);
        	}
    		
    		
    		if(canEditFluid(world, player, pos, rtr.sideHit, itemstack)) {
    			FluidActionResult filledResult = FluidUtil.tryPickUpFluid(itemstack, player, world, offsetPos, rtr.sideHit);
    			if (filledResult.isSuccess() && !creative) {
    				player.addStat(StatList.getObjectUseStats(this));
            		
    				FluidStack newFluidStack = FluidUtil.getFluidContained(filledResult.result);
            		if (newFluidStack.amount > 0) {
            			player.setHeldItem(hand, filledResult.result);
            			player.sendStatusMessage(new TextComponentString(Tmo.proxy.translate("item.titaniumBucket.message.content", newFluidStack.getFluid().getLocalizedName(newFluidStack), newFluidStack.amount, getCapacity())), true);
            			return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
            		}
    			}
            
    		}
    	}
    	else if(!player.isSneaking()) {
    		
    		if(fluidStack == null || fluidStack.amount == 0) {
    			player.sendStatusMessage(new TextComponentString(TextFormatting.DARK_RED+Tmo.proxy.translate("item.titaniumBucket.message.empty")), true);
    			return ActionResult.newResult(EnumActionResult.PASS, itemstack);
    		}
    		else if(fluidStack == null || fluidStack.amount < 1000) {
    			player.sendStatusMessage(new TextComponentString(TextFormatting.DARK_RED+Tmo.proxy.translate("item.titaniumBucket.message.notEnougthLiquid")), true);
    			return ActionResult.newResult(EnumActionResult.PASS, itemstack);
    		}
    		
    		if(rtrSolid == null || rtrSolid.typeOfHit != RayTraceResult.Type.BLOCK) {
        		return ActionResult.newResult(EnumActionResult.PASS, itemstack);
        	}
    		if(canEditFluid(world, player, rtrSolid.getBlockPos(), rtrSolid.sideHit, itemstack)) {
    			FluidActionResult result = ItemTitaniumBucket.tryPlaceFluid(player, world, rtrSolid.getBlockPos().offset(rtrSolid.sideHit), itemstack, fluidStack);
    			if (result.isSuccess() && !creative) {
    				player.addStat(StatList.getObjectUseStats(this));
        				
    				FluidStack drained = fluidHandler.drain(Fluid.BUCKET_VOLUME, true);
        				
    			// check whether we replace the item or add the empty one to the inventory
    				if (drained != null && drained.amount > 0) {
    					FluidStack newFluidStack = FluidUtil.getFluidContained(itemstack);
    					if(newFluidStack == null || newFluidStack.amount == 0) {
    						player.sendStatusMessage(new TextComponentString(Tmo.proxy.translate("item.titaniumBucket.message.empty")), true);
    					} else {
    						player.sendStatusMessage(new TextComponentString(Tmo.proxy.translate("item.titaniumBucket.message.content", newFluidStack.getFluid().getLocalizedName(newFluidStack), newFluidStack.amount, getCapacity())), true);
    					}
    					return ActionResult.newResult(EnumActionResult.SUCCESS, itemstack);
    				}
    			}
    		}
    	}
        return ActionResult.newResult(EnumActionResult.FAIL, itemstack);
    }
	
	//A version of FluidUtil.tryPlaceFluid that doesn't place still lava and water sources
    public static FluidActionResult tryPlaceFluid(@Nullable EntityPlayer player, World world, BlockPos pos, @Nonnull ItemStack container, FluidStack resource) {
        if (world == null || resource == null || pos == null) {
            return FluidActionResult.FAILURE;
        }

        Fluid fluid = resource.getFluid();
        if (fluid == null || !fluid.canBePlacedInWorld()) {
            return FluidActionResult.FAILURE;
        }

        // check that we can place the fluid at the destination
        IBlockState destBlockState = world.getBlockState(pos);
        Material destMaterial = destBlockState.getMaterial();
        boolean isDestNonSolid = !destMaterial.isSolid();
        boolean isDestReplaceable = destBlockState.getBlock().isReplaceable(world, pos);
        if (!world.isAirBlock(pos) && !isDestNonSolid && !isDestReplaceable) {
            return FluidActionResult.FAILURE; // Non-air, solid, unreplacable block. We can't put fluid here.
        }

        if (world.provider.doesWaterVaporize() && fluid.doesVaporize(resource)) {
            fluid.vaporize(player, world, pos, resource);
            return FluidUtil.tryEmptyContainer(container, new VoidFluidHandler(), Fluid.BUCKET_VOLUME, player, true);
        }
        else {
            if (!world.isRemote && (isDestNonSolid || isDestReplaceable) && !destMaterial.isLiquid()) {
                world.destroyBlock(pos, true);
            }

            // Defer the placement to the fluid block
            // Instead of actually "filling", the fluid handler method replaces the block
            Block block = fluid.getBlock();
            IFluidHandler handler;
            if (block instanceof IFluidBlock) {
                handler = new FluidBlockWrapper((IFluidBlock) block, world, pos);
            }
            else if (block instanceof BlockLiquid) {
                handler = new BlockLiquidWrapper((BlockLiquid) block, world, pos);
            }
            else {
                handler = new BlockWrapper(block, world, pos);
            }
            
            boolean isVanilla = block == Blocks.WATER || block == Blocks.LAVA;
            
            FluidActionResult result = FluidUtil.tryEmptyContainer(container, handler, Integer.MAX_VALUE, player, !isVanilla);
            
            if (result.isSuccess()) {
            	
            	if(isVanilla) {
            	if (block == Blocks.WATER) {
            		block = Blocks.FLOWING_WATER;
            	}
            	else if (block == Blocks.LAVA) {
            		block = Blocks.FLOWING_LAVA;
            	}
            	world.setBlockState(pos, block.getDefaultState().withProperty(BlockLiquid.LEVEL, 0), 11);
            	}
                SoundEvent soundevent = fluid.getEmptySound(resource);
                world.playSound(player, pos, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }

            return result;
        }
    
    }
	
	
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		return new FluidHandlerItemStack(stack, getCapacity());
	}
	
}
