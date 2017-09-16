package misterpemodder.tmo.main.client.gui;

import misterpemodder.hc.main.utils.GuiHelper.IGuiElement;
import misterpemodder.tmo.main.inventory.ContainerDestabilizer;
import misterpemodder.tmo.main.inventory.ContainerInjector;
import misterpemodder.tmo.main.inventory.ContainerThermoelectricGenerator;
import misterpemodder.tmo.main.inventory.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.inventory.ContainerTitaniumChest;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import misterpemodder.tmo.main.tileentity.TileEntityThemoelectricGenerator;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumAnvil;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public enum EnumGuiElements implements IGuiElement {
		
	TITANIUM_CHEST {
		@Override
		public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
			return new ContainerTitaniumChest((TileEntityTitaniumChest)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
		}
		@Override
		public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
			return new GuiContainerTitaniumChest(player.inventory, (TileEntityTitaniumChest)world.getTileEntity(new BlockPos(x, y, z)));
		}
	},
	TITANIUM_ANVIL {
		@Override
		public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
			return new ContainerTitaniumAnvil((TileEntityTitaniumAnvil)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
		}
		@Override
		public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
			return new GuiContainerTitaniumAnvil(player.inventory, (TileEntityTitaniumAnvil)world.getTileEntity(new BlockPos(x, y, z)));
		}
	},
	INJECTOR {
		@Override
		public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
			return new ContainerInjector((TileEntityInjector)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
		}
		@Override
		public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
			return new GuiContainerInjector(player.inventory, (TileEntityInjector)world.getTileEntity(new BlockPos(x, y, z)));
		}
	},
	CRYSTAL_DESTABILIZER {
		@Override
		public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
			return new ContainerDestabilizer((TileEntityDestabilizer)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
		}
		@Override
		public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
			return new GuiContainerDestabilizer(player.inventory, (TileEntityDestabilizer)world.getTileEntity(new BlockPos(x, y, z)));
		}
	},
	THERMOELECTRIC_GENERATOR {
		@Override
		public Container getServerGuiElement(EntityPlayer player, World world, int x, int y, int z) {
			return new ContainerThermoelectricGenerator((TileEntityThemoelectricGenerator)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
		}
		@Override
		public GuiScreen getClientGuiElement(EntityPlayer player, World world, int x, int y, int z) {
			return new GuiContainerThermoelectricGenerator(player.inventory, (TileEntityThemoelectricGenerator)world.getTileEntity(new BlockPos(x, y, z)));
		}
	},
	;
	
}
