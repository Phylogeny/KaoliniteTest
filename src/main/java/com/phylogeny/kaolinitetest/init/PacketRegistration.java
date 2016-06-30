package com.phylogeny.kaolinitetest.init;

import com.phylogeny.kaolinitetest.KaoliniteTest;
import com.phylogeny.kaolinitetest.packet.PacketCauldronClearInventory;
import com.phylogeny.kaolinitetest.packet.PacketCauldronConsumeItem;
import com.phylogeny.kaolinitetest.packet.PacketCauldronDrain;
import com.phylogeny.kaolinitetest.packet.PacketCauldronFluidStack;

import net.minecraftforge.fml.relauncher.Side;

public class PacketRegistration {
    private static int packetId;

    public static void registerPackets() {
        registerPacket(PacketCauldronConsumeItem.Handler.class, PacketCauldronConsumeItem.class, Side.CLIENT);
        registerPacket(PacketCauldronFluidStack.Handler.class, PacketCauldronFluidStack.class, Side.CLIENT);
        registerPacket(PacketCauldronDrain.Handler.class, PacketCauldronDrain.class, Side.CLIENT);
        registerPacket(PacketCauldronClearInventory.Handler.class, PacketCauldronClearInventory.class, Side.CLIENT);
    }

    private static void registerPacket(Class handler, Class packet, Side side) {
        KaoliniteTest.packetNetwork.registerMessage(handler, packet, packetId++, side);
    }

}