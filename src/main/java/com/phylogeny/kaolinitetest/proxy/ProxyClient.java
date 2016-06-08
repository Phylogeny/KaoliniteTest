package com.phylogeny.kaolinitetest.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;
import com.phylogeny.kaolinitetest.init.ModelRegistration;

public class ProxyClient extends ProxyCommon
{
    @Override
    public void preInit()
    {
        super.preInit();
        register(ItemsKaoliniteTest.aluminumPowder);
        register(ItemsKaoliniteTest.silicaPowder);
        register(ItemsKaoliniteTest.kaolinitePowder);
        register(ItemsKaoliniteTest.crucibleClayPowder);
        register(ItemsKaoliniteTest.supernatantAndPrecipitateBucket);
        register(ItemsKaoliniteTest.precipitateBucket);
        register(ItemsKaoliniteTest.kaoliniteBall);
        register(ItemsKaoliniteTest.kaoliniteBrick);
        register(ItemsKaoliniteTest.wetCrucibleClay);
        register(ItemsKaoliniteTest.unfiredCrucible);
        register(ItemsKaoliniteTest.crucible);
        ModelRegistration.registerFluidModel();
    }

    @Override
    public void init()
    {
        super.init();
    }

    private void register(Item item)
    {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(
                new ResourceLocation(item.getRegistryName().toString()), "inventory"));
    }

}