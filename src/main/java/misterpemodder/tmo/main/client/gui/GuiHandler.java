package misterpemodder.tmo.main.client.gui;

import misterpemodder.tmo.main.tileentity.TileEntityTitaniumAnvil;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	private interface GuiElement {
		public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z);
		public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z);
	}
	
	public enum EnumGuiElements implements GuiElement {
		
		TITANIUM_CHEST(1) {
			public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new ContainerTitaniumChest((TileEntityTitaniumChest)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
			}
			public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new GuiContainerTitaniumChest(player.inventory, (TileEntityTitaniumChest)world.getTileEntity(new BlockPos(x, y, z)));
			}
		},
		TITANIUM_ANVIL(2) {
			public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new ContainerTitaniumAnvil((TileEntityTitaniumAnvil)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
			}
			public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new GuiContainerTitaniumAnvil(player.inventory, (TileEntityTitaniumAnvil)world.getTileEntity(new BlockPos(x, y, z)));
			}
		},
		;
		
		public final int ID;
		
		private EnumGuiElements(int ID) {
			this.ID = ID;
		}
		
	}
	
	public GuiElement getElementByID(int ID) {
		for(EnumGuiElements el : EnumGuiElements.values()) {
			if(el.ID == ID) {
				return el;
			}
		}
		TMORefs.LOGGER.error("Invalid GUI Element ID!");
		return null;
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
