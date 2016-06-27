package com.phylogeny.kaolinitetest.init;

import com.phylogeny.kaolinitetest.reference.Reference;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;

public class ModelRegistration {
    public static final ModelResourceLocation CAULDRON_HANDLE = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "cauldron_handle"), null);
    public static final ResourceLocation CAULDRON_PRECIPITATE = new ResourceLocation(Reference.MOD_ID, "textures/blocks/precipitate_kaolinite.png");
    public static final ResourceLocation CAULDRON_DISOLVE_DUST = new ResourceLocation(Reference.MOD_ID, "textures/blocks/disolve_dust.png");

    public static void registerModels() {
        registerFluidModel(FluidsKaoliniteTest.kaolinitePrecursorBlock, FluidsKaoliniteTest.kaolinitePrecursor, "kaolinite_precursor");
        registerBlockModel(BlocksKaoliniteTest.kaoliniteBlock);
        registerBlockModel(BlocksKaoliniteTest.cauldron);
        registerItemModel(ItemsKaoliniteTest.aluminumDust);
        registerItemModel(ItemsKaoliniteTest.silicaDust);
        registerItemModel(ItemsKaoliniteTest.kaoliniteDust);
        registerItemModel(ItemsKaoliniteTest.crucibleClayDust);
        registerItemModel(ItemsKaoliniteTest.kaoliniteBall);
        registerItemModel(ItemsKaoliniteTest.kaoliniteBrick);
        registerItemModel(ItemsKaoliniteTest.kaoliniteShard);
        registerItemModel(ItemsKaoliniteTest.wetCrucibleClay);
        registerItemModel(ItemsKaoliniteTest.unfiredCrucible);
        registerItemModel(ItemsKaoliniteTest.crucible);
        registerItemModel(ItemsKaoliniteTest.removalTool);
        registerIsolatedModel(CAULDRON_HANDLE);
    }

    private static void registerIsolatedModel(ResourceLocation resourceLocations) {
        ModelLoader.registerItemVariants(ItemsKaoliniteTest.removalTool, resourceLocations);
    }

    private static void registerFluidModel(Block block, Fluid fluid, String name) {
        Item item = Item.getItemFromBlock(block);
        if (item != null) {
            ModelLoader.registerItemVariants(item);
            final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, name), fluid.getName());
            ItemMeshDefinition mesh = new ItemMeshDefinition() {
                @Override
                public ModelResourceLocation getModelLocation(ItemStack stack) {
                    return modelResourceLocation;
                }
            };
            ModelLoader.setCustomMeshDefinition(item, mesh);
            StateMapperBase mapper = new StateMapperBase() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return modelResourceLocation;
                }
            };
            ModelLoader.setCustomStateMapper(block, mapper);
        }
    }

    private static void registerBlockModel(Block block) {
        Item item = Item.getItemFromBlock(block);
        if (item != null) {
            registerItemModelWithOptionalVariants(item, true);
        }
    }

    private static void registerItemModel(Item item) {
        registerItemModelWithOptionalVariants(item, false);
    }

    private static void registerItemModelWithOptionalVariants(Item item, boolean hasVariants) {
        String name = item.getRegistryName().toString();
        if (hasVariants) {
            ModelLoader.registerItemVariants(item, new ResourceLocation(name));
        }
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(name), "inventory"));
    }

}