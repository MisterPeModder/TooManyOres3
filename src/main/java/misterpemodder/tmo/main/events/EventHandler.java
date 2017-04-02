package misterpemodder.tmo.main.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.api.owner.IOwnerHandler;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.BlockLamp;
import misterpemodder.tmo.main.capability.CapabilityOwner;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.items.ItemLock;
import misterpemodder.tmo.main.items.ItemVariant;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.StringUtils;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules.ValueType;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
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
//					} else if(state.getBlock() instanceof BlockInjector) {
//						TileEntityInjector te = ((BlockInjector)state.getBlock()).getTileEntity(mc.world, hitPos.getBlockPos());
//						if(te != null)
//						StringUtils.drawCenteredString(mc.fontRendererObj, "Mode: " + (te.getTransferMode() == TransferMode.INJECTION? "injection" : "extracton"), res.getScaledWidth()/2, res.getScaledHeight()/2 + h);
//					}
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
		if(I18n.hasKey(event.getItemStack().getUnlocalizedName()+".desc")) {
			String t = I18n.format(event.getItemStack().getUnlocalizedName()+".desc");
				
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
				event.getToolTip().add(TextFormatting.GRAY+""+TextFormatting.ITALIC+str);
		}
	}
	
}
