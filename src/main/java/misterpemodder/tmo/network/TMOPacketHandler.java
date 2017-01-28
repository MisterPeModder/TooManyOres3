package misterpemodder.tmo.network;

import misterpemodder.tmo.network.packet.PacketClientToServer;
import misterpemodder.tmo.network.packet.PacketServerToClient;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public final class TMOPacketHandler {
	
	private static final int SERVER_TO_CLIENT_CHANNEL_ID = 0;
	private static final int CLIENT_TO_SERVER_CHANNEL_ID = 1;
	
	public static SimpleNetworkWrapper network;
	
	public static void init() {
		TMOPacketHandler.network = NetworkRegistry.INSTANCE.newSimpleChannel(TMOHelper.MOD_ID);
		
		TMOPacketHandler.network.registerMessage(PacketServerToClient.PacketServerToClientHandler.class, PacketServerToClient.class, SERVER_TO_CLIENT_CHANNEL_ID, Side.CLIENT);
		TMOPacketHandler.network.registerMessage(PacketClientToServer.PacketClientToServerHandler.class, PacketClientToServer.class, CLIENT_TO_SERVER_CHANNEL_ID, Side.SERVER);
	}
	
}
