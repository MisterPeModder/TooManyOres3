package misterpemodder.tmo.main.tileentity;

import javax.annotation.Nullable;

import misterpemodder.tmo.api.recipe.IMachineRecipe;
import misterpemodder.tmo.main.blocks.BlockMachineCasing.EnumMachineCasingVariant;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public abstract class TileEntityCraftingMachine<V extends IMachineRecipe<V>> extends TileEntityMachine {
	
	protected int progress;
	protected V currentRecipe;
	
	public int getProgress() {
		return this.progress;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
	}
	
	@Nullable
	public V getCurrentRecipe() {
		return this.currentRecipe;
	}
	
	protected abstract IForgeRegistry<V> getRecipeRegistry();
	
	public final int getChangedCraftingTime(IMachineRecipe<?> recipe) {
		if(this.hasWorld()) {
			EnumMachineCasingVariant casing = this.world.getBlockState(this.pos).getValue(BlockMachine.CASING);
			if(casing != null) {
				return (int) (recipe.getTotalTime() * casing.speedModifier);
			}
		}
		return recipe.getTotalTime();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if(this.currentRecipe != null) {
			compound.setString("current_recipe", this.currentRecipe.getRegistryName().toString());
		}
		compound.setInteger("progress", this.progress);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if(compound.hasKey("current_recipe")) {
			this.currentRecipe = getRecipeRegistry().getValue(new ResourceLocation(compound.getString("current_recipe")));
		}
		this.progress = compound.getInteger("progress");
		super.readFromNBT(compound);
	}
	
	@Nullable
	protected abstract V findRecipe();

}
