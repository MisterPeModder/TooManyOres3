package misterpemodder.tmo.main.events;

import java.util.List;
import java.util.Map;
import java.util.Random;

import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.api.capability.owner.IOwnerHandler;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.BlockLamp;
import misterpemodder.tmo.main.capability.CapabilityFreezing;
import misterpemodder.tmo.main.capability.CapabilityFreezing.Freezing;
import misterpemodder.tmo.main.capability.CapabilityFreezing.IFreezing;
import misterpemodder.tmo.main.capability.owner.CapabilityOwner;
import misterpemodder.tmo.main.client.gui.GuiContainerThermoelectricGenerator;
import misterpemodder.tmo.main.enchant.EnchantementBase;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.init.ModPotions.ThePotions;
import misterpemodder.tmo.main.items.ItemLock;
import misterpemodder.tmo.main.items.ItemVariant;
import misterpemodder.tmo.main.items.materials.TmoToolMaterial;
import misterpemodder.tmo.main.items.tools.IItemTMOTool;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import misterpemodder.tmo.main.utils.StringUtils;
import misterpemodder.tmo.main.utils.TMORefs;
import misterpemodder.tmo.main.utils.TemperatureUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules.ValueType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TMORefs.MOD_ID)
public class EventHandler {
		
	@SubscribeEvent
	public static void onGameOverlay(RenderGameOverlayEvent.Post event){
		//true if the screen is not drawn yet.
		if(Minecraft.getMinecraft().currentScreen == null && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {

			Minecraft mc = Minecraft.getMinecraft();
			RayTraceResult hitPos = mc.objectMouseOver;
					
			if(hitPos != null && hitPos.getBlockPos() != null) {
				IBlockState state = mc.world.getBlockState(hitPos.getBlockPos());
				EntityPlayerSP p = mc.player;
				NetworkPlayerInfo n = Minecraft.getMinecraft().getConnection().getPlayerInfo(p.getGameProfile().getId());
				if(p.capabilities.allowEdit && n != null && n.getGameType() != GameType.SPECTATOR || n.getGameType() != GameType.ADVENTURE) {
					ScaledResolution res = event.getResolution();
					int h = res.getScaledHeight()/20;
					if(state.getBlock() instanceof BlockLamp) {
						String txt = state.getValue(BlockLamp.INVERTED)? TextFormatting.RED+Tmo.proxy.translate("tile.blockLamp.mode.inverted") : TextFormatting.GREEN+Tmo.proxy.translate("tile.blockLamp.mode.normal");
						StringUtils.drawCenteredString(mc.fontRendererObj, TextFormatting.BOLD+""+TextFormatting.UNDERLINE+Tmo.proxy.translate("tile.blockLamp.mode.title")+TextFormatting.RESET+" "+TextFormatting.BOLD+""+txt, res.getScaledWidth()/2, res.getScaledHeight()/2 + h);
						StringUtils.drawCenteredString(mc.fontRendererObj, TextFormatting.GRAY+""+TextFormatting.ITALIC+Tmo.proxy.translate("tile.blockLamp.mode.hint"), res.getScaledWidth()/2, res.getScaledHeight()/2 + 2*h);
					}
			    }
			}
		}
	}
	
	@SubscribeEvent
	public static void anvilUpdateEvent(AnvilUpdateEvent event) {
		
		if(event.getLeft().getItem() == ModItems.TheItems.LOCK.getItem()) {
			ItemStack lStack = event.getLeft();
			ItemStack rStack = event.getRight();
			ItemVariant.LockVariant variant = ((ItemLock)lStack.getItem()).getVariant(lStack.getMetadata());
			ItemStack repairItem = variant.getRepairItem();
			
			if(rStack.isItemEqual(repairItem) && variant.isBroken() && rStack.getCount() >= repairItem.getCount()) {
				ItemStack output = variant.getOtherVariant();
				
				if(lStack.hasTagCompound()) {
					output.setTagCompound(lStack.getTagCompound());
				}
				
				event.setMaterialCost(repairItem.getCount());
				event.setOutput(output);
				event.setCost(variant.getCost());
			} 
			else if(rStack.getItem() == net.minecraft.init.Items.ENCHANTED_BOOK) {
				ItemEnchantedBook book = (ItemEnchantedBook)rStack.getItem();
				NBTTagList enchants = book.getEnchantments(rStack);
				boolean valid = false;
				
				if (enchants != null) {
		            for (int i = 0; i < enchants.tagCount(); ++i) {
		                int id = enchants.getCompoundTagAt(i).getShort("id");
		                if (Enchantment.getEnchantmentByID(id) == Enchantments.UNBREAKING) {
		                   valid = true;
		                }
		            }
		        }
				
				if(!valid) event.setCanceled(true);
				
			}
		}
	}
	
	
	@SubscribeEvent
	public static void worldLoadingEvent(WorldEvent.Load event) {
		if(!event.getWorld().getGameRules().hasRule("dropInvContents")) {
			event.getWorld().getGameRules().addGameRule("dropInvContents", "true", ValueType.BOOLEAN_VALUE);
		}
	}
	
	@SubscribeEvent
	public static void blockBreakEvent(BlockEvent.BreakEvent event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		TileEntity t = world.getTileEntity(pos);
		
		if(t != null) {
			
			boolean flag1 = t instanceof ILockable && ((ILockable)t).isLocked();
			
			IOwnerHandler ownerHandler = t.getCapability(CapabilityOwner.OWNER_HANDLER_CAPABILITY, null);
			boolean flag2 = false;
			if(ownerHandler != null) {
				flag2 = ownerHandler.hasOwner() && !ownerHandler.isOwner(event.getPlayer());
			}
			
			if(flag1 && flag2) {
				IBlockState state = world.getBlockState(pos);
				event.getPlayer().sendMessage(new TextComponentString(TextFormatting.RED + Tmo.proxy.translate("protectedBlock.noBreaking", state.getBlock().getItem(world, pos, state).getDisplayName())));
				event.setCanceled(true);
				if(t instanceof TileEntityContainerBase) {
					((TileEntityContainerBase)t).sync();
					t.markDirty();
				}
			}
		}
		
	}
	
	@SubscribeEvent
	public static void itemTooltipEvent(ItemTooltipEvent event) {
		if(event.getItemStack().getItem() == Items.ENCHANTED_BOOK) {
			Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(event.getItemStack());
			
			for(Enchantment e : enchants.keySet()) {
				if(e instanceof EnchantementBase && ((EnchantementBase)e).hasDescription()) {
					
					String eName = TextFormatting.RESET+Tmo.proxy.translate(e.getName())+": ";
					List<String> toAdd = StringUtils.parseTooltip(eName+TextFormatting.GRAY+""+TextFormatting.ITALIC+((EnchantementBase)e).getDescription());
					
					event.getToolTip().addAll(toAdd);
				}
			}
		}
		else if(I18n.hasKey(event.getItemStack().getUnlocalizedName()+".desc") && event.getItemStack().getItem().getRegistryName().getResourceDomain().equals(TMORefs.MOD_ID)) {
			if(GuiScreen.isShiftKeyDown()) {
				List<String> toAdd = StringUtils.parseTooltip(I18n.format(event.getItemStack().getUnlocalizedName()+".desc"));
			
				for(String str : toAdd)
					event.getToolTip().add(TextFormatting.GRAY+""+TextFormatting.ITALIC+str);
			} else {
				event.getToolTip().add(TextFormatting.GRAY+""+TextFormatting.ITALIC+Tmo.proxy.translate("item.desc"));
			}
		}
		
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		if(screen != null && screen instanceof GuiContainerThermoelectricGenerator) {
			if(event.getItemStack().hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
				IFluidHandlerItem h = event.getItemStack().getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
				FluidStack fs = h.drain(Fluid.BUCKET_VOLUME, false);
				if(fs != null) {
					event.getToolTip().add(TemperatureUtils.getFluidTemperatureString(fs, true));
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void attachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
		if(event.getObject() instanceof EntityLivingBase) {
			event.addCapability(new ResourceLocationTmo("freezing"), new ICapabilitySerializable<NBTTagCompound>() {
				
				private final IFreezing freezingCap = new Freezing();
				
				@Override
				public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
					return capability == CapabilityFreezing.FREEZING_CAPABILITY;
				}
				
				@Override
				public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
					return capability == CapabilityFreezing.FREEZING_CAPABILITY? CapabilityFreezing.FREEZING_CAPABILITY.cast(freezingCap) : null;
				}

				@Override
				public NBTTagCompound serializeNBT() {
					return freezingCap.serializeNBT();
				}

				@Override
				public void deserializeNBT(NBTTagCompound nbt) {
					freezingCap.deserializeNBT(nbt);
				}
			});
		}
	}
	
	@SubscribeEvent
	public static void entityUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if(entity.getEntityWorld() != null && !entity.getEntityWorld().isRemote &&entity.hasCapability(CapabilityFreezing.FREEZING_CAPABILITY, null)) {
			PotionEffect effect = entity.getActivePotionEffect(ThePotions.FREEZING.getPotion());
			event.getEntityLiving().getCapability(CapabilityFreezing.FREEZING_CAPABILITY, null).update(effect == null? 0 : effect.getDuration(), entity);
		}
	}
	
	@SubscribeEvent
	public static void livingAttackEvent(LivingAttackEvent event) {
		Entity attacker = event.getSource().getSourceOfDamage();
		
		if(attacker != null && attacker instanceof EntityLivingBase) {
			ItemStack stack = ((EntityLivingBase)attacker).getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
			EntityLivingBase living = event.getEntityLiving();
			
			if(stack.getItem() instanceof IItemTMOTool && ((IItemTMOTool)stack.getItem()).getMaterial() == TmoToolMaterial.FROZIUM_MATERIAL) {
				if(new Random().nextInt(2) == 1) {
					event.getEntityLiving().addPotionEffect(new PotionEffect(ThePotions.FREEZING.getPotion(), 60, 0, false, true));
				}	
			}
			if(living.hasCapability(CapabilityFreezing.FREEZING_CAPABILITY, null) && living.getCapability(CapabilityFreezing.FREEZING_CAPABILITY, null).isFreezing()) {
				living.getEntityWorld().playSound(null, living.posX, living.posY, living.posZ, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.HOSTILE, 1.0F, 0.5F);
			}
		}
	}
	
}
