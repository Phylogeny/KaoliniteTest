package com.phylogeny.kaolinitetest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;

public class PacketCauldronConsumeItem extends PacketCauldronBase {
    private boolean isAluminum;

    public PacketCauldronConsumeItem() {}

    public PacketCauldronConsumeItem(BlockPos pos, boolean isAluminum) {
        super(pos);
        this.isAluminum = isAluminum;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeBoolean(isAluminum);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
        isAluminum = buffer.readBoolean();
    }

    public static class Handler implements IMessageHandler<PacketCauldronConsumeItem, IMessage> {
        @Override
        public IMessage onMessage(final PacketCauldronConsumeItem message, final MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TileEntity tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(message.pos);
                    if (tileEntity != null && tileEntity instanceof TileEntityCauldron) {
                        ((TileEntityCauldron) tileEntity).onItemConsumed(message.isAluminum);
                    }
                }
            });
            return null;
        }

    }

}