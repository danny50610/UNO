package club.dannyserver.uno.common.packet;

import java.util.HashMap;
import java.util.Map;

public abstract class PacketManager {

    private static Map<Integer, Class<? extends IPacket>> packetMap = new HashMap<>();

    static {
        // client -> server
        packetMap.put(1, PacketLogin.class);
        packetMap.put(2, PacketRegister.class);
        packetMap.put(3, PacketPlayCard.class);
        packetMap.put(4, PacketReAddRoom.class);

        // server -> client
        packetMap.put(64, PacketLoginResult.class);
        packetMap.put(65, PacketRegisterResult.class);
        packetMap.put(66, PacketUserIndex.class);
        packetMap.put(67, PacketUpdateRoomUsername.class);
        packetMap.put(68, PacketUpdateCardCount.class);
        packetMap.put(69, PacketUpdateCard.class);
        packetMap.put(70, PacketUpdateRoomInfo.class);
        packetMap.put(71, PacketGameStart.class);
        packetMap.put(72, PacketWinResult.class);
        packetMap.put(73, PacketUserLeave.class);
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
