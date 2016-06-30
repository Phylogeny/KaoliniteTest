package com.phylogeny.kaolinitetest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;

public class PacketCauldronDrain extends PacketPositioned {
    private int amount;
    private boolean doDrain;

    public PacketCauldronDrain() {}

    public PacketCauldronDrain(BlockPos pos, int amount, boolean doDrain) {
        super(pos);
        this.amount = amount;
        this.doDrain = doDrain;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeInt(amount);
        buffer.writeBoolean(doDrain);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
        amount = buffer.readInt();
        doDrain = buffer.readBoolean();
    }

    public static class Handler implements IMessageHandler<PacketCauldronDrain, IMessage> {
        @Override
        public IMessage onMessage(final PacketCauldronDrain message, final MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TileEntity tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(message.pos);
                    if (tileEntity != null && tileEntity instanceof TileEntityCauldron) {
                        ((TileEntityCauldron) tileEntity).drain(message.amount, message.doDrain);
                    }
                }
            });
            return null;
        }

    }

}