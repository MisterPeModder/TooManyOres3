package misterpemodder.tmo.main.items.base;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.tmo.main.items.EnumItemsNames;
import misterpemodder.tmo.main.utils.TMOHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import scala.actors.threadpool.Arrays;

public class ItemBase extends Item implements TMOItem {
	
	protected EnumItemsNames names;
	
	@Override
	public void registerRender() {
		ModelResourceLocation location = new ModelResourceLocation(TMOHelper.PREFIX + names.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, location);
	}
	
	public void registerOreDict() {
		String[] oreDictNames = this.names.getOreDictNames();
		if(oreDictNames.length == 0) {
			return;
		}
		for (String str : oreDictNames) {
			OreDictionary.registerOre(str, this);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if(I18n.hasKey(this.getUnlocalizedName(stack)+".desc")) {
			String t = I18n.format(this.getUnlocalizedName(stack)+".desc");
				
			//expand new lines
			List<String> expandedLines = Arrays.asList(t.split("\\\\n"));
				
			List<String> toAdd = new ArrayList<>();
			FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
			for(String line : expandedLines) {
				
				if(font.getStringWidth(line)>200) {
					toAdd.addAll(font.listFormattedStringToWidth(line, 200));
				} else {
					toAdd.add(line);
				}
			}
			
			for(String str : toAdd)
				tooltip.add(TextFormatting.GRAY+""+TextFormatting.ITALIC+str);
		}
	}
	
	protected void attemptSetCreativeTab() {
		if(isEnabled()) setCreativeTab(TMOHelper.TMO_TAB);
	}
	
	public ItemBase(EnumItemsNames names) {
		this(names, true);
	}
	
	public ItemBase(EnumItemsNames names, boolean useDefaultTab) {
		this.names = names;
		setUnlocalizedName(names.getUnlocalizedName());
		setRegistryName(names.getRegistryName());
		if(useDefaultTab) attemptSetCreativeTab();
	}	
}
