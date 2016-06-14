package com.phylogeny.kaolinitetest.item;

import com.phylogeny.kaolinitetest.init.ItemsKaoliniteTest;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemSupernatantAndPrecipitateBucket extends ItemKaoliniteTestBase {
    public ItemSupernatantAndPrecipitateBucket(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, false);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemStackIn, raytraceresult);
        if (ret != null)
            return ret;
        if (raytraceresult == null) {
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        } else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult(EnumActionResult.PASS, itemStackIn);
        } else {
            BlockPos blockpos = raytraceresult.getBlockPos();
            if (!worldIn.isBlockModifiable(playerIn, blockpos)) {
                return new ActionResult(EnumActionResult.FAIL, itemStackIn);
            }
            boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
            BlockPos blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(raytraceresult.sideHit);
            if (!playerIn.canPlayerEdit(blockpos1, raytraceresult.sideHit, itemStackIn)) {
                return new ActionResult(EnumActionResult.FAIL, itemStackIn);
            } else if (tryPlaceContainedLiquid(playerIn, worldIn, blockpos1)) {
                playerIn.addStat(StatList.getObjectUseStats(this));
                return new ActionResult(EnumActionResult.SUCCESS, new ItemStack(ItemsKaoliniteTest.bucketPrecipitate));
            } else {
                return new ActionResult(EnumActionResult.FAIL, itemStackIn);
            }
        }
    }

    public boolean tryPlaceContainedLiquid(EntityPlayer player, World world, BlockPos pos) {
        IBlockState iblockstate = world.getBlockState(pos);
        Material material = iblockstate.getMaterial();
        boolean flag = !material.isSolid();
        boolean flag1 = iblockstate.getBlock().isReplaceable(world, pos);
        if (!world.isAirBlock(pos) && !flag && !flag1)
            return false;
        if (!world.isRemote && (flag || flag1) && !material.isLiquid()) {
            world.destroyBlock(pos, true);
        }
        world.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState(), 3);
        return true;
    }

}