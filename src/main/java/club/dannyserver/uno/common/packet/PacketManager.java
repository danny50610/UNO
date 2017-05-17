package club.dannyserver.uno.common.packet;

import java.util.HashMap;
import java.util.Map;

public abstract class PacketManager {

    private static Map<Integer, Class<? extends IPacket>> packetMap = new HashMap<>();

    static {
        packetMap.put(1, PacketLogin.class);
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
