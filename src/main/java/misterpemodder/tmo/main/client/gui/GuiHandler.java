package misterpemodder.tmo.main.client.gui;

import misterpemodder.tmo.main.inventory.ContainerDestabilizer;
import misterpemodder.tmo.main.inventory.ContainerInjector;
import misterpemodder.tmo.main.inventory.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.inventory.ContainerTitaniumChest;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
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
		INJECTOR(3) {
			public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new ContainerInjector((TileEntityInjector)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
			}
			public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new GuiContainerInjector(player.inventory, (TileEntityInjector)world.getTileEntity(new BlockPos(x, y, z)));
			}
		},
		CRYSTAL_DESTABILIZER(4) {
			public Object getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new ContainerDestabilizer((TileEntityDestabilizer)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
			}
			public Object getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
				return new GuiContainerDestabilizer(player.inventory, (TileEntityDestabilizer)world.getTileEntity(new BlockPos(x, y, z)));
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
