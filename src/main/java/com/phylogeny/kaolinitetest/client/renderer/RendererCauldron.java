package com.phylogeny.kaolinitetest.client.renderer;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector4f;

import org.lwjgl.opengl.GL11;

import com.phylogeny.kaolinitetest.block.BlockCauldron;
import com.phylogeny.kaolinitetest.client.util.ModelTransformer;
import com.phylogeny.kaolinitetest.init.FluidsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.ModelRegistration;
import com.phylogeny.kaolinitetest.tileentity.TileEntityCauldron;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class RendererCauldron extends FastTESR<TileEntityCauldron> {
    public static void register() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCauldron.class, new RendererCauldron());
    }

    @Override
    public void renderTileEntityFast(final TileEntityCauldron cauldronTE, double x, double y, double z, final float partialTicks, int destroyStage, VertexBuffer VertexBuffer) {
        IBlockState state = cauldronTE.getWorld().getBlockState(cauldronTE.getPos());
        if (!(state.getBlock() instanceof BlockCauldron))
            return;

        VertexBuffer vb = Tessellator.getInstance().getBuffer();
        double x2 = x + 0.1875;
        double z2 = z + 0.1875;

        if (MinecraftForgeClient.getRenderPass() == 1) {
            FluidStack fluidStack = cauldronTE.getFluid();
            if (!cauldronTE.isEmpty() && fluidStack != null) {
                vb.finishDrawing();
                Vec3d pos = new Vec3d(x2, y + cauldronTE.getWaterLevel(), z2);
                Fluid fluid = fluidStack.getFluid();
                if (fluid == FluidRegistry.WATER || fluid == FluidsKaoliniteTest.kaolinitePrecursor) {
                    float alpha = cauldronTE.getAlpha();
                    renderTexturedSide(pos, alpha, FluidRegistry.WATER.getStill().toString());
                    renderTexturedSide(pos, 1 - alpha, FluidsKaoliniteTest.kaolinitePrecursor.getStill().toString());
                } else {
                    renderTexturedSide(pos, 1, fluid.getStill().toString());
                }
                vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            }
        } else {
            int threshold = 80;
            int progress = cauldronTE.getPrecipitationProgressTicks();
            if (progress > threshold) {
                vb.finishDrawing();
                bindTexture(ModelRegistration.CAULDRON_PRECIPITATE);
                renderTexturedSide(new Vec3d(x2, y + cauldronTE.getSolidPrecipitateLevel(), z2), 0, 1, 0, 1, (progress - threshold) / 600.0F);
                vb.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            }

            IBakedModel origModel = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(ModelRegistration.CAULDRON_HANDLE);
            VertexBuffer.setTranslation(x - cauldronTE.getPos().getX(), y - cauldronTE.getPos().getY(), z - cauldronTE.getPos().getZ());

            IBakedModel model = ModelTransformer.transform(origModel, new ModelTransformer.IVertexTransformer() {
                @Override
                public float[] transform(BakedQuad quad, VertexFormatElement.EnumType type, VertexFormatElement.EnumUsage usage, float... data) {
                    if (usage == VertexFormatElement.EnumUsage.POSITION) {
                        Vector4f vec = new Vector4f(data[0] - 0.5F, data[1] - 0.8F, data[2] - 0.5F, 0);
                        Matrix4f mat = new Matrix4f();
                        mat.setIdentity();
                        mat.rotZ(cauldronTE.getHandleRotation());
                        mat.transform(vec);
                        data[0] = vec.x + 0.5F;
                        data[1] = vec.y + 0.8F;
                        data[2] = vec.z + 0.5F;
                    }
                    return data;
                }
            }, null, 0);

            BlockModelRenderer modelRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer();
            modelRenderer.renderModel(cauldronTE.getWorld(), model, state, cauldronTE.getPos(), VertexBuffer, false);
        }
    }

    private void renderTexturedSide(Vec3d pos, float alpha, String path) {
        TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite texture = textureMap.getTextureExtry(path);
        if (texture == null)
            texture = textureMap.getAtlasSprite("");
        renderTexturedSide(pos, texture.getMinU(), texture.getMaxU(), texture.getMinV(), texture.getMaxV(), alpha);
    }

    private void renderTexturedSide(Vec3d pos, double minU, double maxU, double minV, double maxV, float alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vb = tessellator.getBuffer();
        double f = 0.625;
        GlStateManager.color(1, 1, 1, alpha);
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(pos.xCoord, pos.yCoord, pos.zCoord + f).tex(maxU, minV).endVertex();
        vb.pos(pos.xCoord + f, pos.yCoord, pos.zCoord + f).tex(minU, minV).endVertex();
        vb.pos(pos.xCoord + f, pos.yCoord, pos.zCoord).tex(minU, maxV).endVertex();
        vb.pos(pos.xCoord, pos.yCoord, pos.zCoord).tex(maxU, maxV).endVertex();
        tessellator.draw();
    }

}