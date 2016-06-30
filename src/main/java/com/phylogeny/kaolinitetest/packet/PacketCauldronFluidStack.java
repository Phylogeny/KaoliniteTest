package com.phylogeny.kaolinitetest.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;

public class PacketCauldronFluidStack extends PacketPositioned {
    private NBTTagCompound fluidStackNBT;
    private boolean doAction, fill;;

    public PacketCauldronFluidStack() {}

    public PacketCauldronFluidStack(BlockPos pos, FluidStack fluidStack, boolean fill, boolean doAction) {
        super(pos);
        fluidStackNBT = new NBTTagCompound();
        fluidStack.writeToNBT(fluidStackNBT);
        this.fill = fill;
        this.doAction = doAction;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        super.toBytes(buffer);
        ByteBufUtils.writeTag(buffer, fluidStackNBT);
        buffer.writeBoolean(fill);
        buffer.writeBoolean(doAction);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        super.fromBytes(buffer);
        fluidStackNBT = ByteBufUtils.readTag(buffer);
        fill = buffer.readBoolean();
        doAction = buffer.readBoolean();
    }

    public static class Handler implements IMessageHandler<PacketCauldronFluidStack, IMessage> {
        @Override
        public IMessage onMessage(final PacketCauldronFluidStack message, final MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    TileEntity tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(message.pos);
                    if (tileEntity != null && tileEntity instanceof TileEntityCauldron) {
                        TileEntityCauldron cauldronTE = (TileEntityCauldron) tileEntity;
                        FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(message.fluidStackNBT);
                        if (message.fill) {
                            cauldronTE.fill(fluidStack, true);
                        } else {
                            cauldronTE.drain(fluidStack, true);
                        }
                    }
                }
            });
            return null;
        }

    }

}