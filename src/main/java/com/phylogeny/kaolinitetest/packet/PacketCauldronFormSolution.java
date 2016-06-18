package com.phylogeny.kaolinitetest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;

public class PacketCauldronFormSolution extends PacketCauldronBase
{
    public PacketCauldronFormSolution() {}

    public PacketCauldronFormSolution(BlockPos pos) {
        super(pos);
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
    }

    public static class Handler implements IMessageHandler<PacketCauldronFormSolution, IMessage> {
        @Override
        public IMessage onMessage(final PacketCauldronFormSolution message, final MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TileEntity tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(message.pos);
                    if (tileEntity != null && tileEntity instanceof TileEntityCauldron) {
                        ((TileEntityCauldron) tileEntity).setToWater();
                    }
                }
            });
            return null;
        }

    }

}