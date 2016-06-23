package com.phylogeny.kaolinitetest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;

public class PacketCauldronConsumeItem implements IMessage {
    protected BlockPos pos;
    private boolean isAluminum;
    private int amount;
    private Vec3d dustPos, dustMotion;
    private float dustWidth;
    private double dustMinY;
    
    public PacketCauldronConsumeItem() {}

    public PacketCauldronConsumeItem(BlockPos pos, boolean isAluminum, int amount, Vec3d dustPos, Vec3d dustMotion, float dustWidth, double dustMinY) {
        this.pos = pos;
        this.isAluminum = isAluminum;
        this.amount = amount;
        this.dustPos = dustPos;
        this.dustMotion = dustMotion;
        this.dustWidth = dustWidth;
        this.dustMinY = dustMinY;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeDouble(pos.getX());
        buffer.writeDouble(pos.getY());
        buffer.writeDouble(pos.getZ());
        buffer.writeBoolean(isAluminum);
        buffer.writeInt(amount);
        buffer.writeDouble(dustPos.xCoord);
        buffer.writeDouble(dustPos.yCoord);
        buffer.writeDouble(dustPos.zCoord);
        buffer.writeDouble(dustMotion.xCoord);
        buffer.writeDouble(dustMotion.yCoord);
        buffer.writeDouble(dustMotion.zCoord);
        buffer.writeFloat(dustWidth);
        buffer.writeDouble(dustMinY);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        pos = new BlockPos(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        isAluminum = buffer.readBoolean();
        amount = buffer.readInt();
        dustPos = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        dustMotion = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        dustWidth = buffer.readFloat();
        dustMinY = buffer.readDouble();
    }

    public static class Handler implements IMessageHandler<PacketCauldronConsumeItem, IMessage> {
        @Override
        public IMessage onMessage(final PacketCauldronConsumeItem message, final MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TileEntity tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(message.pos);
                    if (tileEntity != null && tileEntity instanceof TileEntityCauldron) {
                        ((TileEntityCauldron) tileEntity).onItemConsumed(message.isAluminum, message.amount, message.dustPos, message.dustMotion, message.dustWidth, message.dustMinY);
                    }
                }
            });
            return null;
        }

    }

}