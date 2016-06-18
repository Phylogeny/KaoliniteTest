package com.phylogeny.kaolinitetest.item;

import com.phylogeny.kaolinitetest.entity.EntityItemKaolinitePrecursor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemKaolinitePrecursor extends ItemKaoliniteTestBase {
    public ItemKaolinitePrecursor(String name) {
        super(name);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity originalEntity, ItemStack stack) {
        return originalEntity instanceof EntityItem ? new EntityItemKaolinitePrecursor(world, (EntityItem) originalEntity) : null;
    }

}