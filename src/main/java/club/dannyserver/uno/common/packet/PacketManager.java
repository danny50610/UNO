package club.dannyserver.uno.common.packet;

import java.util.HashMap;
import java.util.Map;

public abstract class PacketManager {

    private static Map<Integer, Class<? extends IPacket>> packetMap = new HashMap<>();

    static {
        // client -> server
        packetMap.put(1, PacketLogin.class);
        packetMap.put(2, PacketRegister.class);

        // server -> client
        packetMap.put(64, PacketLoginResult.class);
        packetMap.put(65, PacketRegisterResult.class);
        packetMap.put(66, PacketUserIndex.class);
        packetMap.put(67, PacketUpdateRoomUsername.class);
        packetMap.put(68, PacketUpdateCardCount.class);
        packetMap.put(69, PacketUpdateCard.class);
        packetMap.put(70, PacketUpdateRoomInfo.class);
        packetMap.put(71, PacketGameStart.class);
    }

    public static IPacket getPacket(int packetId) {
        Class<? extends IPacket> clazz = packetMap.get(packetId);

        IPacket packet = null;
        try {
            packet = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return packet;
    }

}
