package misterpemodder.tmo.main.blocks;

import java.lang.reflect.Method;
import java.util.List;

import com.google.common.collect.Lists;

import misterpemodder.tmo.api.IStrongPistonBehavior;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.main.blocks.base.BlockTMO;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.tileentity.TileEntityStrongPiston;
import misterpemodder.tmo.main.utils.BlockStrongPistonStructureHelper;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.BlockPistonMoving;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class BlockPistonStrongBase extends BlockPistonBase implements BlockTMO {
	
	protected ItemBlock itemBlock;
	protected boolean isSticky;
	
	private Method shouldBeExtended;

	public BlockPistonStrongBase(IBlockNames names, boolean isSticky) {
		super(isSticky);
		this.setUnlocalizedName(names.getUnlocalizedName());
		this.setRegistryName(TMORefs.PREFIX + names.getRegistryName());
		this.setLightOpacity(0);
		
		this.itemBlock = new ItemBlock(this);
		itemBlock.setRegistryName(this.getRegistryName());
		this.setCreativeTab(TMORefs.TMO_TAB);
		this.setSoundType(SoundType.METAL);
		this.setHarvestLevel("pickaxe", 1);
		
		this.isSticky = isSticky;
		this.shouldBeExtended = ReflectionHelper.findMethod(BlockPistonBase.class, Blocks.PISTON, new String[]{"shouldBeExtended"}, World.class, BlockPos.class, EnumFacing.class);
	}

	@Override
	public ItemBlock getItemBlock() {
		return this.itemBlock;
	}
	
	@Override
	public boolean hasOwnItemBlock() {
		return true;
	}
	
	protected boolean shouldBeExtended(World world, BlockPos pos, EnumFacing facing) {
		boolean b = false;
		try {
			b = (boolean)shouldBeExtended.invoke(this, world, pos, facing);
		} catch (Exception e) {
			TMORefs.LOGGER.error("Something when wrong!", e);
		}
		return b;
	}
	
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer)), 2);

        if (!worldIn.isRemote) {
            this.checkForMove(worldIn, pos, state);
        }
    }
	
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            this.checkForMove(worldIn, pos, state);
        }
    }
    
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote && worldIn.getTileEntity(pos) == null) {
            this.checkForMove(worldIn, pos, state);
        }
    }
	
	protected void checkForMove(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        boolean flag = this.shouldBeExtended(worldIn, pos, enumfacing);

        if (flag && !((Boolean)state.getValue(EXTENDED)).booleanValue()) {
            if ((new BlockStrongPistonStructureHelper(worldIn, pos, enumfacing, true)).canMove()) {
                worldIn.addBlockEvent(pos, this, 0, enumfacing.getIndex());
            }
        }
        else if (!flag && ((Boolean)state.getValue(EXTENDED)).booleanValue()) {
            worldIn.addBlockEvent(pos, this, 1, enumfacing.getIndex());
        }
    }

	
	@Override
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param) {
		
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

        if (!worldIn.isRemote) {
            boolean flag = shouldBeExtended(worldIn, pos, enumfacing);

            if (flag && id == 1) {
                worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 2);
                return false;
            }

            if (!flag && id == 0) {
                return false;
            }
        }

        if (id == 0) {
            if (!this.doMove(worldIn, pos, enumfacing, true)) {
                return false;
            }

            worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 3);
            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
        }
        else if (id == 1) {
            TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));

            if (tileentity1 instanceof TileEntityStrongPiston) {
                ((TileEntityStrongPiston)tileentity1).clearPistonTileEntity();
            }
            worldIn.setBlockState(pos, TheBlocks.STRONG_PISTON_MOVING.getBlock().getDefaultState().withProperty(BlockPistonMoving.FACING, enumfacing).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);

            worldIn.setTileEntity(pos, BlockStrongPistonMoving.createTilePiston(this.getStateFromMeta(param), null, enumfacing, false, true));

            if (this.isSticky) {
                BlockPos blockpos = pos.add(enumfacing.getFrontOffsetX() * 2, enumfacing.getFrontOffsetY() * 2, enumfacing.getFrontOffsetZ() * 2);
                IBlockState iblockstate = worldIn.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                boolean flag1 = false;

                if (block == TheBlocks.STRONG_PISTON_EXTENSION.getBlock()) {
                    TileEntity tileentity = worldIn.getTileEntity(blockpos);

                    if (tileentity instanceof TileEntityStrongPiston) {
                    	TileEntityStrongPiston tileentitypiston = (TileEntityStrongPiston)tileentity;

                        if (tileentitypiston.getFacing() == enumfacing && tileentitypiston.isExtending()) {
                            tileentitypiston.clearPistonTileEntity();
                            flag1 = true;
                        }
                    }
                }
                
                boolean flag2 = iblockstate.getMobilityFlag() == EnumPushReaction.NORMAL || block instanceof BlockPistonBase;

                if (!flag1 && !iblockstate.getBlock().isAir(iblockstate, worldIn, pos) && canPush(iblockstate, worldIn, blockpos, enumfacing.getOpposite(), false) && flag2) {
                    this.doMove(worldIn, pos, enumfacing, false);
                }
            }
            else {
                worldIn.setBlockToAir(pos.offset(enumfacing));
            }

            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.15F + 0.6F);
        }

        return true;
    }
	
	public static boolean canPush(IBlockState blockStateIn, World worldIn, BlockPos pos, EnumFacing facing, boolean destroyBlocks) {
        Block block = blockStateIn.getBlock();

        if (block == Blocks.OBSIDIAN) {
            return false;
        }
        else if (!worldIn.getWorldBorder().contains(pos)) {
            return false;
        }
        else if (pos.getY() >= 0 && (facing != EnumFacing.DOWN || pos.getY() != 0)) {
            if (pos.getY() <= worldIn.getHeight() - 1 && (facing != EnumFacing.UP || pos.getY() != worldIn.getHeight() - 1)) {
        
                if(TooManyOresAPI.STRONG_PISTON_BEHAVIORS.isEmpty()) {
                	return true;
                } else {
                	for(IStrongPistonBehavior behavior : TooManyOresAPI.STRONG_PISTON_BEHAVIORS) {
                		if(!behavior.canPushBlock(blockStateIn, worldIn, pos, facing, destroyBlocks)) {
                			return false;
                		}
                	}
                	return true;
                }
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
	
	protected boolean doMove(World worldIn, BlockPos pos, EnumFacing direction, boolean extending) {
        if (!extending) {
            worldIn.setBlockToAir(pos.offset(direction));
        }

        BlockStrongPistonStructureHelper blockpistonstructurehelper = new BlockStrongPistonStructureHelper(worldIn, pos, direction, extending);

        if (!blockpistonstructurehelper.canMove()) {
            return false;
        }
        else {
            List<BlockPos> list = blockpistonstructurehelper.getBlocksToMove();
            List<IBlockState> list1 = Lists.<IBlockState>newArrayList();

            for (int i = 0; i < list.size(); ++i) {
                BlockPos blockpos = (BlockPos)list.get(i);
                list1.add(worldIn.getBlockState(blockpos).getActualState(worldIn, blockpos));
            }

            List<BlockPos> list2 = blockpistonstructurehelper.getBlocksToDestroy();
            int k = list.size() + list2.size();
            IBlockState[] aiblockstate = new IBlockState[k];
            EnumFacing enumfacing = extending ? direction : direction.getOpposite();

            for (int j = list2.size() - 1; j >= 0; --j) {
                BlockPos blockpos1 = (BlockPos)list2.get(j);
                IBlockState iblockstate = worldIn.getBlockState(blockpos1);
                float chance = iblockstate.getBlock() instanceof BlockSnow ? -1.0f : 1.0f;
                iblockstate.getBlock().dropBlockAsItemWithChance(worldIn, blockpos1, iblockstate, chance, 0);
                worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 4);
                --k;
                aiblockstate[k] = iblockstate;
            }

            for (int l = list.size() - 1; l >= 0; --l) {
                BlockPos blockpos3 = (BlockPos)list.get(l);
                TileEntity te = worldIn.getTileEntity(blockpos3);
                IBlockState iblockstate2 = worldIn.getBlockState(blockpos3);
                worldIn.removeTileEntity(blockpos3);
                worldIn.setBlockState(blockpos3, Blocks.AIR.getDefaultState(), 2);
                blockpos3 = blockpos3.offset(enumfacing);
                worldIn.setBlockState(blockpos3, TheBlocks.STRONG_PISTON_MOVING.getBlock().getDefaultState().withProperty(FACING, direction), 4);
                worldIn.setTileEntity(blockpos3, BlockStrongPistonMoving.createTilePiston((IBlockState)list1.get(l), te, direction, extending, false));
                --k;
                aiblockstate[k] = iblockstate2;
            }

            BlockPos blockpos2 = pos.offset(direction);

            if (extending) {
                BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iblockstate3 = TheBlocks.STRONG_PISTON_EXTENSION.getBlock().getDefaultState().withProperty(BlockPistonExtension.FACING, direction).withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype);
                IBlockState iblockstate1 = TheBlocks.STRONG_PISTON_MOVING.getBlock().getDefaultState().withProperty(BlockPistonMoving.FACING, direction).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
                worldIn.setBlockState(blockpos2, iblockstate1, 2);
                worldIn.setTileEntity(blockpos2, BlockStrongPistonMoving.createTilePiston(iblockstate3, null, direction, true, true));
            }

            for (int i1 = list2.size() - 1; i1 >= 0; --i1) {
                worldIn.notifyNeighborsOfStateChange((BlockPos)list2.get(i1), aiblockstate[k++].getBlock(), false);
            }

            for (int j1 = list.size() - 1; j1 >= 0; --j1) {
                worldIn.notifyNeighborsOfStateChange((BlockPos)list.get(j1), aiblockstate[k++].getBlock(), false);
            }

            if (extending){
                worldIn.notifyNeighborsOfStateChange(blockpos2, TheBlocks.STRONG_PISTON_EXTENSION.getBlock(), false);
            }

            return true;
        }
    }

}
