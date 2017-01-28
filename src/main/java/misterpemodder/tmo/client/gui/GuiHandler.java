package misterpemodder.tmo.client.gui;

import misterpemodder.tmo.main.blocks.containers.BlockItemKeeper;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumChest;
import misterpemodder.tmo.main.tileentity.TileEntityItemKeeper;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

public class GuiHandler implements IGuiHandler {
	
	private interface GuiElement {
		public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z);
		public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z);
	}
	
	public enum EnumGuiElements implements GuiElement {
		
		ITEM_KEEPER(0) {
			public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new ContainerItemKeeper((TileEntityItemKeeper)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
			}
			public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new GuiContainerItemKeeper(player.inventory, (TileEntityItemKeeper)world.getTileEntity(new BlockPos(x, y, z)));
			}
		},
		TITANIUM_CHEST(1) {
			public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new ContainerTitaniumChest((TileEntityTitaniumChest)world.getTileEntity(new BlockPos(x, y, z)), new PlayerMainInvWrapper(player.inventory));
			}
			public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new GuiContainerTitaniumChest(new PlayerMainInvWrapper(player.inventory), (TileEntityTitaniumChest)world.getTileEntity(new BlockPos(x, y, z)));
			}
		},
		;
		
		public final int ID;
		
		private EnumGuiElements(int ID) {
			this.ID = ID;
		}
		
	}
	
	public GuiElement getElementByID(int ID) {
		try {
			return EnumGuiElements.values()[ID];
		} catch(ArrayIndexOutOfBoundsException e) {
			TMOHelper.LOGGER.error("Invalid GUI Element ID!", e);
			return null;
		}
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return getElementByID(ID).getServerGuiElement(player, world, x, y, z);
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return getElementByID(ID).getClientGuiElement(player, world, x, y, z);
	}

}
