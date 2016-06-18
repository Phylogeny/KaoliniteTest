package com.phylogeny.kaolinitetest.item;

import java.util.List;

import com.phylogeny.kaolinitetest.init.BlocksKaoliniteTest;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class ItemToolRemoval extends ItemKaoliniteTestBase {
    private static final String REMOVE_GRASS_NBT_KEY = "removeGrass";
    
    public ItemToolRemoval(String name) {
        super(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        
        if (!itemStackIn.hasTagCompound()) {
            itemStackIn.setTagCompound(new NBTTagCompound());
        }
        boolean removeGrass = itemStackIn.getTagCompound().getBoolean(REMOVE_GRASS_NBT_KEY);
        if (playerIn.isSneaking()) {
            if (!worldIn.isRemote) {
                itemStackIn.getTagCompound().setBoolean(REMOVE_GRASS_NBT_KEY, !removeGrass);
            }
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        }
        int semiDiameter = 30;
        AxisAlignedBB area = new AxisAlignedBB(playerIn.posX - semiDiameter, 0, playerIn.posZ - semiDiameter,
                playerIn.posX + semiDiameter, 255, playerIn.posZ + semiDiameter);
        for (int x = (int) area.minX; x < area.maxX; x++) {
            for (int y = (int) area.minY; y < area.maxY; y++) {
                for (int z = (int) area.minZ; z < area.maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Biome biome = worldIn.getBiomeGenForCoords(pos);
                    if ((biome == Biomes.JUNGLE || biome == Biomes.JUNGLE_HILLS || biome == Biomes.JUNGLE_EDGE)) {
                        Block block = worldIn.getBlockState(pos).getBlock();
                        if (block != BlocksKaoliniteTest.kaoliniteBlock && (removeGrass || block != Blocks.GRASS)) {
                            worldIn.setBlockToAir(pos);
                        }
                    }
                }
            }
        }
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String displayName = ("" + I18n.translateToLocal(getUnlocalizedNameInefficiently(stack) + ".name")).trim()
                + " - Leave Only Kaolinite" + (shouldRemoveGrass(stack) ? "" : " & Grass") + "";
        return displayName;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {
        tooltip.add("Removes all blocks in a 60 meter diameter square around you, except kaolinite blocks"
                + (shouldRemoveGrass(stack) ? "" : " and grass blocks") + ". This allows for visualization of kaolinite bed distribution.");
        tooltip.add("");
        tooltip.add("Right click to use.");
        tooltip.add("");
        tooltip.add("Shift-right click to toggle grass removal.");
    }

    private boolean shouldRemoveGrass(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getBoolean(REMOVE_GRASS_NBT_KEY) : false;
    }

}