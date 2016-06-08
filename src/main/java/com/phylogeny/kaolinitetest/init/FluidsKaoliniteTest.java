package com.phylogeny.kaolinitetest.init;

import com.phylogeny.kaolinitetest.client.creativetab.CreativeTabKaoliniteTest;
import com.phylogeny.kaolinitetest.reference.Reference;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FluidsKaoliniteTest
{
	public static Fluid kaolinitePrecursor;
	public static BlockFluidClassic kaolinitePrecursorBlock;
	
	public static void registerFluid()
	{
		String name = "kaolinite_precursor";
		String texturePrefix = Reference.MOD_ID + ":fluids/";
		ResourceLocation still = new ResourceLocation(texturePrefix + name + "_still");
		ResourceLocation flowing = new ResourceLocation(texturePrefix + name + "_flowing");
		Fluid fluid = new Fluid(name, still, flowing);
		kaolinitePrecursor = fluid;
		FluidRegistry.addBucketForFluid(fluid);
		BlockFluidClassic block = new BlockFluidClassic(fluid, Material.water);
		kaolinitePrecursorBlock = block;
		block.setRegistryName(name);
		block.setUnlocalizedName(block.getRegistryName().toString());
		block.setCreativeTab(CreativeTabKaoliniteTest.CREATIVE_TAB);
		GameRegistry.register(block);
		GameRegistry.register((new ItemBlock(block)).setRegistryName(block.getRegistryName()));
	}
	
}