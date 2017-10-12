package misterpemodder.tmo.main.blocks.containers;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.elements.ElementVertical;
import mcjty.theoneprobe.config.Config;
import misterpemodder.hc.api.capability.owner.IOwnerHandler;
import misterpemodder.hc.main.apiimpl.capability.owner.CapabilityOwner;
import misterpemodder.hc.main.blocks.BlockCustomChest;
import misterpemodder.hc.main.tileentity.TileEntityCustomChest;
import misterpemodder.hc.main.utils.GuiHelper.IGuiElement;
import misterpemodder.hc.main.utils.StringUtils;
import misterpemodder.tmo.api.IStrongPistonBehavior;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.client.gui.EnumGuiElements;
import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "mcjty.theoneprobe.api.IProbeInfoAccessor", modid = "theoneprobe")
public class BlockTitaniumChest extends BlockCustomChest<TileEntityTitaniumChest> implements IProbeInfoAccessor {

	public BlockTitaniumChest() {
		super(EnumBlocksNames.TITANIUM_CHEST, EnumBlocksValues.TITANIUM_CHEST, TMORefs.TMO_TAB);
	}
	
    @Override
    protected IGuiElement getGuiElements() {
    	return EnumGuiElements.TITANIUM_CHEST;
    }
	
	@Override
	public Class<TileEntityTitaniumChest> getTileEntityClass() {
		return TileEntityTitaniumChest.class;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityTitaniumChest();
	}

	@Override
	@Optional.Method(modid = "theoneprobe")
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		TileEntity te = world.getTileEntity(data.getPos());
		if(te != null && te instanceof TileEntityTitaniumChest && ConfigValues.BoolValues.TOP_COMPAT.currentValue) { 
			TileEntityTitaniumChest chest = (TileEntityTitaniumChest) te;
			IOwnerHandler ownerHandler = chest.getCapability(CapabilityOwner.OWNER_HANDLER_CAPABILITY, null);
			
			String title = StringUtils.translate("top.info.titaniumChest.owner.title");
			String empty = StringUtils.translate("top.info.titaniumChest.owner.empty");
			
			String str = TextFormatting.GREEN + StringUtils.translate("top.info.titaniumChest.access.granted");
			String str2 = TextFormatting.GREEN+title+" "+TextFormatting.RED+empty;
			
			if(ownerHandler!=null && ownerHandler.hasOwner()) {
				str2 = TextFormatting.GREEN+title+" "+TextFormatting.YELLOW+ownerHandler.getOwnerName();
				if(ownerHandler.getOwner(world) != player) {
					if(chest.isLocked()) {
						str = TextFormatting.RED+StringUtils.translate("top.info.titaniumChest.access.denied");
						if(player.isSneaking())
						probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(Config.chestContentsBorderColor).spacing(0)).text(StringUtils.translateFormatted(TextFormatting.GRAY+""+TextFormatting.ITALIC+"<%s>", "top.info.titaniumChest.hidden"));
					} else {
						str = TextFormatting.GOLD+StringUtils.translate("top.info.titaniumChest.access.forced");
					}
				}
			}
			IProbeInfo main = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xff040448).spacing(0));
			
			ItemStack lock = chest.getLockItemHandler().getStackInSlot(0);
			String lockedText = TextFormatting.YELLOW+""+TextFormatting.UNDERLINE+StringUtils.translate("top.info.titaniumChest.locked")+":"+TextFormatting.RESET+" "+(chest.isLocked()? TextFormatting.GREEN+StringUtils.translate("top.info.titaniumChest.locked.yes"):TextFormatting.RED+StringUtils.translate("top.info.titaniumChest.locked.no"));
			ElementVertical textInfo = new ElementVertical(0, 2, ElementAlignment.ALIGN_TOPLEFT);
			
			main.text(str2);
			textInfo.text(lockedText).text(TextFormatting.YELLOW+""+TextFormatting.UNDERLINE+StringUtils.translate("top.info.titaniumChest.access")+":"+TextFormatting.RESET+" "+str);
			
			if(lock.isEmpty()) {
				main.horizontal().element(textInfo);
			}
			else {
				main.horizontal().item(lock, main.defaultItemStyle().height(27)).element(textInfo);
			}
		}
		
	}
	
	public static class TChestPistonBehavior implements IStrongPistonBehavior {

		@Override
		public boolean canPushBlock(IBlockState state, World world, BlockPos pos, EnumFacing facing, boolean destroyBlocks) {
			if(state.getBlock() instanceof BlockCustomChest) {
				TileEntity te = world.getTileEntity(pos);
				if(te != null && te instanceof TileEntityCustomChest) {
					return !((TileEntityCustomChest)te).isLocked();
				}
			}
			return true;
		}
		
	}

}
