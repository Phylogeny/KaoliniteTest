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

public class ModelRegistration
{
	public static void registerFluidModel()
	{
		Block block = FluidsKaoliniteTest.kaolinitePrecursorBlock;
        Item item = Item.getItemFromBlock(block);
        if (item != null)
        {
        	ModelLoader.registerItemVariants(item);
        	final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(new ResourceLocation(Reference.MOD_ID, "kaolinite_precursor"),
            		FluidsKaoliniteTest.kaolinitePrecursor.getName());
            ItemMeshDefinition mesh = new ItemMeshDefinition()
            {
                @Override
                public ModelResourceLocation getModelLocation(ItemStack stack)
                {
                    return modelResourceLocation;
                }
            };
            ModelLoader.setCustomMeshDefinition(item, mesh);
            StateMapperBase mapper = new StateMapperBase()
            {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state)
                {
                    return modelResourceLocation;
                }
            };
            ModelLoader.setCustomStateMapper(block, mapper);
        }
	}
	
}