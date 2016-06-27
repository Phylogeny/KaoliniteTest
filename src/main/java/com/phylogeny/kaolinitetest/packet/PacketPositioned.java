package com.phylogeny.kaolinitetest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketPositioned implements IMessage {
    protected BlockPos pos;

    public PacketPositioned() {}

    public PacketPositioned(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeDouble(pos.getX());
        buffer.writeDouble(pos.getY());
        buffer.writeDouble(pos.getZ());
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        pos = new BlockPos(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

}