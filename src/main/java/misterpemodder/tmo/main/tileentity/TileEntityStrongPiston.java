package misterpemodder.tmo.main.tileentity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.block.ISlimeBlock;
import misterpemodder.tmo.main.blocks.BlockStrongPistonMoving;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindMethodException;

public class TileEntityStrongPiston extends TileEntityPiston {
	
	private static Field lastProgressField;
	private static Field progressField;
	private static Field moving_entity;
	private static Method moveByPositionAndProgressMethod;
	private static Method getMovementAreaMethod;
	private static Method getMovementMethod;
	private static Method fixEntityWithinPistonBaseMethod;
	
	public Class<? extends TileEntity> containedTileEntityClazz;
	public NBTTagCompound containedTileEntityNBT;
	
	static {
		lastProgressField = ReflectionHelper.findField(TileEntityPiston.class, "lastProgress");
		progressField = ReflectionHelper.findField(TileEntityPiston.class, "progress");
		moving_entity = ReflectionHelper.findField(TileEntityPiston.class, "MOVING_ENTITY");
		moveByPositionAndProgressMethod = tryFindMethod(TileEntityPiston.class, new String[]{"moveByPositionAndProgress"}, AxisAlignedBB.class, AxisAlignedBB.class);
		getMovementAreaMethod = tryFindMethod(TileEntityPiston.class, new String[]{"getMovementArea"}, AxisAlignedBB.class, AxisAlignedBB.class, EnumFacing.class, double.class);
		getMovementMethod = tryFindMethod(TileEntityPiston.class, new String[]{"getMovement"}, double.class, AxisAlignedBB.class, EnumFacing.class, AxisAlignedBB.class);
		fixEntityWithinPistonBaseMethod = tryFindMethod(TileEntityPiston.class, new String[]{"fixEntityWithinPistonBase"}, void.class, Entity.class, EnumFacing.class, double.class);
	}
	
	public TileEntityStrongPiston() {}

	public <T extends TileEntity> TileEntityStrongPiston(IBlockState pistonStateIn, @Nullable T tileEntityIn, EnumFacing pistonFacingIn, boolean extendingIn, boolean shouldHeadBeRenderedIn) {
        super(pistonStateIn, pistonFacingIn, extendingIn, shouldHeadBeRenderedIn);
        this.containedTileEntityNBT = new NBTTagCompound();
        if(tileEntityIn != null) {
        	tileEntityIn.writeToNBT(this.containedTileEntityNBT);
        	this.containedTileEntityClazz = tileEntityIn.getClass();
        }
    }
	
	/**Fallback method to avoid as much as possible errors due to different mappings*/
	public static Method tryFindMethod(Class clazz, String[] methodNames, Class returnType, Class... methodTypes) {
		try {
			return ReflectionHelper.findMethod(clazz, null, methodNames, methodTypes);
		} catch(UnableToFindMethodException e) {
			
		}
		
		Method m = null;
		for(Method method : clazz.getDeclaredMethods()) {
			if(returnType == method.getReturnType() && method.getParameterTypes().equals(methodTypes)) {
				return m;
			}
		}
		throw new UnableToFindMethodException(methodNames, new NoSuchMethodException());
	}
	
	public void setContainedTileEntity(World world, BlockPos pos) {
		try {
			if(containedTileEntityNBT != null && containedTileEntityClazz != null && !containedTileEntityNBT.hasNoTags()) {
				TileEntity te;
				te = containedTileEntityClazz.newInstance();
				te.setPos(pos);
				te.readFromNBT(containedTileEntityNBT);
				if(world.isRemote)
					te.handleUpdateTag(te.getUpdateTag());
				world.setTileEntity(pos, te);
        	}
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("contained_tile_entity")) {
        	this.containedTileEntityNBT = compound.getCompoundTag("contained_tile_entity");
        }
        if(compound.hasKey("tile_entity_class")) {
        	try {
				this.containedTileEntityClazz = (Class<? extends TileEntity>) Class.forName(compound.getString("tile_entity_class"));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if(!containedTileEntityNBT.hasNoTags()) {
        	compound.setTag("contained_tile_entity", containedTileEntityNBT);
        	compound.setString("tile_entity_class", containedTileEntityClazz.getName());
        }
        return compound;
    }
	
	protected float getLastProgress() {
		float f = 0.0F;
		try {
			f = (Float)lastProgressField.get((TileEntityPiston) this);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			TMORefs.LOGGER.error("Something when wrong!", e);
		}
		return f;
	}
	
	protected void setLastProgress(float lastProgress) {
		try {
			lastProgressField.set((TileEntityPiston) this, lastProgress);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			TMORefs.LOGGER.error("Something when wrong!", e);
		}
	}
	
	protected float getProgress() {
		float f = 0.0F;
		try {
			f = (Float)progressField.get((TileEntityPiston) this);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			TMORefs.LOGGER.error("Something when wrong!", e);
		}
		return f;
	}
	
	protected void setProgress(float progress) {
		try {
			progressField.set((TileEntityPiston) this, progress);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			TMORefs.LOGGER.error("Something when wrong!", e);
		}
	}
	
	protected ThreadLocal<EnumFacing> getMovingEntity() {
		try {
			return (ThreadLocal<EnumFacing>)moving_entity.get((TileEntityPiston) this);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			TMORefs.LOGGER.error("Something when wrong!", e);
		}
		return null;
	}
	
	protected AxisAlignedBB moveByPositionAndProgress(AxisAlignedBB boundingBox) {
		try {
			return (AxisAlignedBB) moveByPositionAndProgressMethod.invoke((TileEntityPiston)this, boundingBox);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			TMORefs.LOGGER.error("Something when wrong!", e);
		}
		return null;
	}
	
	protected AxisAlignedBB getMovementArea(AxisAlignedBB boundingBox, EnumFacing facing, double d) {
		try {
			return (AxisAlignedBB) getMovementAreaMethod.invoke((TileEntityPiston)this, boundingBox, facing, d);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			TMORefs.LOGGER.error("Something when wrong!", e);
		}
		return null;
	}
	
	protected double getMovement(AxisAlignedBB boundingBox1, EnumFacing facing, AxisAlignedBB boundingBox2) {
		try {
			return (double) getMovementMethod.invoke((TileEntityPiston)this, boundingBox1, facing, boundingBox2);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			TMORefs.LOGGER.error("Something when wrong!", e);
		}
		return 0;
    }
	
	protected void fixEntityWithinPistonBase(Entity entity, EnumFacing facing, double d) {
		try {
			fixEntityWithinPistonBaseMethod.invoke((TileEntityPiston)this, entity, facing, d);
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			TMORefs.LOGGER.error("Something when wrong!", e);
		}
	}
	
	@Override
	public void clearPistonTileEntity() {
        if (getLastProgress() < 1.0F && this.world != null) {
            setProgress(1.0F);
            setLastProgress(getProgress());
            this.world.removeTileEntity(this.pos);
            this.invalidate();

            if (this.world.getBlockState(this.pos).getBlock() instanceof BlockStrongPistonMoving) {
                this.world.setBlockState(this.pos, getPistonState(), 3);
                this.setContainedTileEntity(this.world, this.pos);
                this.world.neighborChanged(this.pos, getPistonState().getBlock(), this.pos);
            }
        }
    }
	
	public void update() {
		float p = getProgress();
		setLastProgress(p);
        if (p >= 1.0F) {
            this.world.removeTileEntity(this.pos);
            this.invalidate();
            if (this.world.getBlockState(this.pos).getBlock() instanceof BlockStrongPistonMoving) {
                this.world.setBlockState(this.pos, getPistonState(), 3);
                this.setContainedTileEntity(this.world, this.pos);
                this.world.neighborChanged(this.pos, getPistonState().getBlock(), this.pos);
            }
        }
        else {
            float f = p + 0.5F;
            this.moveCollidedEntities(f);
            setProgress(f);

            if (getProgress() >= 1.0F) {
            	setProgress(1.0F);
            }
        }
    }
	
	private IBlockState getCollisionRelatedBlockState() {
        return !this.isExtending() && this.shouldPistonHeadBeRendered() ? TheBlocks.STRONG_PISTON_EXTENSION.getBlock().getDefaultState().withProperty(BlockPistonExtension.TYPE, this.getPistonState().getBlock() == TheBlocks.STRONG_PISTON_STICKY.getBlock() ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT).withProperty(BlockPistonExtension.FACING, this.getPistonState().getValue(BlockPistonBase.FACING)) : this.getPistonState();
    }
	
	private AxisAlignedBB fuseCollisionBoxes(List<AxisAlignedBB> collisionBoxes) {
        double d0 = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        double d3 = 1.0D;
        double d4 = 1.0D;
        double d5 = 1.0D;

        for (AxisAlignedBB axisalignedbb : collisionBoxes) {
            d0 = Math.min(axisalignedbb.minX, d0);
            d1 = Math.min(axisalignedbb.minY, d1);
            d2 = Math.min(axisalignedbb.minZ, d2);
            d3 = Math.max(axisalignedbb.maxX, d3);
            d4 = Math.max(axisalignedbb.maxY, d4);
            d5 = Math.max(axisalignedbb.maxZ, d5);
        }

        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }
	
	private void moveCollidedEntities(float f) {
        EnumFacing enumfacing = this.isExtending() ? this.getFacing() : this.getFacing().getOpposite();
        double d0 = (double)(f - this.getProgress());
        List<AxisAlignedBB> list = Lists.<AxisAlignedBB>newArrayList();
        this.getCollisionRelatedBlockState().addCollisionBoxToList(this.world, BlockPos.ORIGIN, new AxisAlignedBB(BlockPos.ORIGIN), list, (Entity)null, true);

        if (!list.isEmpty()) {
            AxisAlignedBB axisalignedbb = this.moveByPositionAndProgress(this.fuseCollisionBoxes(list));
            List<Entity> list1 = this.world.getEntitiesWithinAABBExcludingEntity((Entity)null, this.getMovementArea(axisalignedbb, enumfacing, d0).union(axisalignedbb));

            if (!list1.isEmpty()) {
            	IBlockState pState = this.getPistonState();
            	ISlimeBlock slimeBlock = TooManyOresAPI.methodHandler.getSlimeBlock(pState);

                for (int i = 0; i < list1.size(); ++i) {
                    Entity entity = (Entity)list1.get(i);

                    if (entity.getPushReaction() != EnumPushReaction.IGNORE) {
                        if (slimeBlock != null && slimeBlock.canLaunchEntity(pState, entity))  {
                        	double power = slimeBlock.getLaunchingPowerMultiplier(pState, entity);
                            switch (enumfacing.getAxis()) {
                                case X:
                                    entity.motionX = (double)enumfacing.getFrontOffsetX() * power;
                                    break;
                                case Y:
                                    entity.motionY = (double)enumfacing.getFrontOffsetY() * power;
                                    break;
                                case Z:
                                    entity.motionZ = (double)enumfacing.getFrontOffsetZ() * power;
                            }
                        }

                        double d1 = 0.0D;

                        for (int j = 0; j < ((List)list).size(); ++j) {
                            AxisAlignedBB axisalignedbb1 = this.getMovementArea(this.moveByPositionAndProgress((AxisAlignedBB)list.get(j)), enumfacing, d0);
                            AxisAlignedBB axisalignedbb2 = entity.getEntityBoundingBox();

                            if (axisalignedbb1.intersectsWith(axisalignedbb2)) {
                                d1 = Math.max(d1, this.getMovement(axisalignedbb1, enumfacing, axisalignedbb2));

                                if (d1 >= d0) {
                                    break;
                                }
                            }
                        }
                        
                        if (d1 > 0.0D) {
                            d1 = Math.min(d1, d0) + 0.01D;
                            ThreadLocal<EnumFacing> movingEntity = getMovingEntity();
                            movingEntity.set(enumfacing);
                            entity.move(MoverType.PISTON, d1 * (double)enumfacing.getFrontOffsetX(), d1 * (double)enumfacing.getFrontOffsetY(), d1 * (double)enumfacing.getFrontOffsetZ());
                            movingEntity.set((EnumFacing)null);

                            if (!this.isExtending() && this.shouldPistonHeadBeRendered()) {
                                this.fixEntityWithinPistonBase(entity, enumfacing, d0);
                            }
                        }
                    }
                }
            }
        }
    }
	
}
