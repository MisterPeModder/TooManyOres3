package misterpemodder.tmo.main.items;

import javax.annotation.Nullable;

import misterpemodder.tmo.main.items.base.TMOItem;
import misterpemodder.tmo.main.items.materials.TmoArmorMaterial;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTmoArmor extends ItemArmor implements TMOItem{

	protected EnumItemsNames itemRefs;
	private TmoArmorMaterial material;
	
	
	public ItemTmoArmor(EnumItemsNames itemRefs, TmoArmorMaterial material, EntityEquipmentSlot slot) {
		super(material.material, 0, slot);
		this.material = material;
		this.itemRefs = itemRefs;
		setUnlocalizedName(itemRefs.getUnlocalizedName());
		setRegistryName(itemRefs.getRegistryName());
		if(isEnabled()) setCreativeTab(TMORefs.TMO_TAB);
		
		if(slot == EntityEquipmentSlot.HEAD) {
			this.addPropertyOverride(new ResourceLocation("probe"), new IItemPropertyGetter() {
	            @SideOnly(Side.CLIENT)
	            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
	            	if(stack.hasTagCompound() && stack.getTagCompound().hasKey(TMORefs.TOP_TAG)) {
	            		return 1.0F;
	            	}
	            	return 0.0F;
	            }
	        });
		}
		
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
		subItems.add(new ItemStack(itemIn));
		if(((ItemTmoArmor) itemIn).armorType == EntityEquipmentSlot.HEAD) {
			ItemStack helmetStack = new ItemStack(itemIn);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger(TMORefs.TOP_TAG, 1);
			helmetStack.setTagCompound(tag);
			subItems.add(helmetStack);
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            return this.getUnlocalizedName();
        }
		if(stack.getTagCompound().hasKey("theoneprobe") && ((ItemTmoArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD) {
			return this.getUnlocalizedName() + ".probe";
		}
		else return this.getUnlocalizedName();
	}
	
	@Override
	public boolean isEnabled() {
		return material.isEnabled();
	}

	@Override
	public void registerRender() {
		ModelResourceLocation location = new ModelResourceLocation(TMORefs.PREFIX + itemRefs.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(this, 0, location);
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return material.rarity == EnumRarity.COMMON? super.getRarity(stack) : material.rarity;
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return ItemStackUtils.blinkColorOnLowDurability(super.getRGBDurabilityForDisplay(stack), stack);
	}

}
