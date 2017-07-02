package misterpemodder.tmo.main.items.tools;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Multimap;

import misterpemodder.tmo.api.item.IItemForgeHammer;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.enchant.EnchantementXpCostReduction;
import misterpemodder.tmo.main.init.ModEnchants;
import misterpemodder.tmo.main.init.ModEnchants.TheEnchants;
import misterpemodder.tmo.main.items.EnumItemsNames;
import misterpemodder.tmo.main.items.base.ItemBase;
import misterpemodder.tmo.main.items.materials.TmoToolMaterial;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemHammer extends ItemBase implements IItemForgeHammer {
	
	private final float attackDamage;
	public final TmoToolMaterial toolMaterial;
	
	public ItemHammer(EnumItemsNames names, TmoToolMaterial material) {
		super(names);
		this.toolMaterial = material;
		this.maxStackSize = 1;
		this.setMaxDamage((int)Math.floor(toolMaterial.material.getMaxUses()/1.5));
		this.attackDamage = 4.0F + toolMaterial.material.getDamageVsEntity();
		if(this.isEnabled()) this.setCreativeTab(TMORefs.TMO_TAB);
	}

	@Override
	public void onHammerUsed(EntityPlayer player, ItemStack stack, int durability) {
		stack.damageItem(durability, player);
		if(stack.getItemDamage() > stack.getMaxDamage()) {
			ForgeEventFactory.onPlayerDestroyItem(player, stack, player.getActiveHand());
			player.world.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, new Random().nextFloat() + 1.0F);
		}
	}
	
	@Override
	public int getExperienceCostReduction(ItemStack stack) {
		return this.toolMaterial.getBaseExperienceCostReduction();
	}
	
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(player.isSneaking() && player.getHeldItem(hand).getItem() instanceof IItemForgeHammer){
			SoundType soundtype = world.getBlockState(pos).getBlock().getSoundType(world.getBlockState(pos), world, pos, player);
			world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
			return EnumActionResult.SUCCESS;
		}
		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
        	multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -3.2F, 0));
        }

        return multimap;
    }
	
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		Material material = state.getMaterial();
        return material != Material.GLASS && material != Material.ICE && material != Material.PACKED_ICE ? 1.0F : this.toolMaterial.material.getEfficiencyOnProperMaterial();
    }
	
	public int getItemEnchantability() {
        return toolMaterial.material.getEnchantability();
    }
	
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = toolMaterial.material.getRepairItemStack();
        if (!mat.isEmpty() && OreDictionary.itemMatches(mat, repair, false)) 
        	return true;
        return super.getIsRepairable(toRepair, repair);
    }
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return enchantment.type == EnumEnchantmentType.ALL || enchantment.type == EnumEnchantmentType.WEAPON || enchantment.type == ModEnchants.FORGE_HAMMER_ENCHANTEMENT_TYPE;
	}
	
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if(stack.getItem() instanceof IItemForgeHammer) {
			int e = 0;
    		Map<Enchantment, Integer> m = EnchantmentHelper.getEnchantments(stack);
    		if(m.containsKey(TheEnchants.XP_COST_REDUCTION.getEnchantement())) {
    			e = EnchantementXpCostReduction.REDUCTION_PER_LEVEL * m.get(TheEnchants.XP_COST_REDUCTION.getEnchantement());
    		}
    		
    		int r = Math.abs(Math.min(90, ((IItemForgeHammer)stack.getItem()).getExperienceCostReduction(stack)));
    		
    		tooltip.add(TextFormatting.AQUA+Tmo.proxy.translate("enchantment.hammerXpCostReduction.tooltip")+": "+TextFormatting.GREEN+r+(e>0? " +"+e+"%":"%"));
		}
    }
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return ItemStackUtils.blinkColorOnLowDurability(super.getRGBDurabilityForDisplay(stack), stack);
	}

}
