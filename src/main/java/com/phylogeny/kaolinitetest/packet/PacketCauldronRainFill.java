package com.phylogeny.kaolinitetest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;

public class PacketCauldronRainFill extends PacketPositioned {

    public PacketCauldronRainFill() {}

    public PacketCauldronRainFill(BlockPos pos) {
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

    public static class Handler implements IMessageHandler<PacketCauldronRainFill, IMessage> {
        @Override
        public IMessage onMessage(final PacketCauldronRainFill message, final MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TileEntity tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(message.pos);
                    if (tileEntity != null && tileEntity instanceof TileEntityCauldron) {
                        ((TileEntityCauldron) tileEntity).fill(new FluidStack(FluidRegistry.WATER, 50), true);
                    }
                }
            });
            return null;
        }

    }

}