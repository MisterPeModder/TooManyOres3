package misterpemodder.tmo.main.tileentity;

import javax.annotation.Nullable;

import misterpemodder.tmo.api.recipe.IMachineRecipe;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public abstract class TileEntityMachine<V extends IMachineRecipe<V>> extends TileEntityContainerBase implements ITickable{
	
	public static final int CAPACITY = 8000;
	protected int progress;
	protected V currentRecipe;
	
	public int getProgress() {
		return this.progress;
	}
	
	@Nullable
	public V getCurrentRecipe() {
		return this.currentRecipe;
	}
	
	protected abstract IForgeRegistry<V> getRecipeRegistry();
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		if(this.currentRecipe != null) {
			compound.setString("current_recipe", this.currentRecipe.getRegistryName().toString());
		}
		compound.setInteger("progress", this.progress);
		NBTTagCompound tag = super.writeToNBT(compound);
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if(compound.hasKey("current_recipe")) {
			this.currentRecipe = getRecipeRegistry().getValue(new ResourceLocation(compound.getString("current_recipe")));
		}
		this.progress = compound.getInteger("progress");
		super.readFromNBT(compound);
	}
	
	@Override
	public void onInvOpen(EntityPlayer player) {}

	@Override
	public void onInvClose(EntityPlayer player) {}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		this.sync();
	}
	
	@Nullable
	protected abstract V findRecipe();
	
	protected abstract IBlockNames getBlockNames();
	
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(TextFormatting.getTextWithoutFormattingCodes(Tmo.proxy.translate("tile."+getBlockNames().getUnlocalizedName()+".name")));
	}
	
	public void emptyTank(short tankId) {
		
	}

}
