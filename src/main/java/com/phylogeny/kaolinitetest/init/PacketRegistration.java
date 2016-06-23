package com.phylogeny.kaolinitetest.init;

import com.phylogeny.kaolinitetest.KaoliniteTest;
import com.phylogeny.kaolinitetest.packet.PacketCauldronConsumeItem;

import net.minecraftforge.fml.relauncher.Side;

public class PacketRegistration {
    private static int packetId;

    public static void registerPackets() {
        registerPacket(PacketCauldronConsumeItem.Handler.class, PacketCauldronConsumeItem.class, Side.CLIENT);
    }

    private static void registerPacket(Class handler, Class packet, Side side) {
        KaoliniteTest.packetNetwork.registerMessage(handler, packet, packetId++, side);
    }

}