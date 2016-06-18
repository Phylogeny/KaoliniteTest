package com.phylogeny.kaolinitetest.packet;

import com.phylogeny.kaolinitetest.client.particle.ParticleCauldronBubble;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketCauldronWaterEffects implements IMessage
{
    private Vec3d pos, motion;
    private double minY;

    public PacketCauldronWaterEffects() {}

    public PacketCauldronWaterEffects(double posX, double posY, double posZ, double motionX, double motionY, double motionZ, double minY) {
        pos = new Vec3d(posX, posY, posZ);
        motion = new Vec3d(motionX, motionY, motionZ);
        this.minY = minY;
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeDouble(pos.xCoord);
        buffer.writeDouble(pos.yCoord);
        buffer.writeDouble(pos.zCoord);
        buffer.writeDouble(motion.xCoord);
        buffer.writeDouble(motion.yCoord);
        buffer.writeDouble(motion.zCoord);
        buffer.writeDouble(minY);
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        pos = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        motion = new Vec3d(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        minY = buffer.readDouble();
    }

    public static class Handler implements IMessageHandler<PacketCauldronWaterEffects, IMessage> {
        @Override
        public IMessage onMessage(final PacketCauldronWaterEffects message, final MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Vec3d pos = message.pos;
                    Vec3d motion = message.motion;
                    float width = 0.25F;
                    World world = Minecraft.getMinecraft().theWorld;
                    float f1 = MathHelper.floor_double(message.minY);
                    for (int i = 0; i < 1.0F + width * 20.0F; ++i) {
                        float xOffset = (world.rand.nextFloat() * 2.0F - 1.0F) * width;
                        float zOffset = (world.rand.nextFloat() * 2.0F - 1.0F) * width;
                        Minecraft.getMinecraft().effectRenderer.addEffect((new ParticleCauldronBubble.Factory()).getEntityFX(0, world, pos.xCoord + xOffset,
                                pos.yCoord, pos.zCoord + zOffset, motion.xCoord, motion.yCoord - world.rand.nextFloat() * 0.2F, motion.zCoord, new int[0]));
                    }

                    for (int j = 0; j < 1.0F + width * 20.0F; ++j) {
                        float f4 = (world.rand.nextFloat() * 2.0F - 1.0F) * width;
                        float f5 = (world.rand.nextFloat() * 2.0F - 1.0F) * width;
                        world.spawnParticle(EnumParticleTypes.WATER_SPLASH, pos.xCoord + f4, f1 + 1.0F, pos.zCoord + f5,
                                motion.xCoord, motion.yCoord, motion.zCoord, new int[0]);
                    }
                }
            });
            return null;
        }

    }

}