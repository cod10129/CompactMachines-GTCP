package org.dave.cm2.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import org.dave.cm2.misc.CreativeTabCM2;
import org.dave.cm2.init.Fluidss;
import org.dave.cm2.init.Potionss;

import java.util.Random;

public class BlockMiniaturizationFluid extends BlockFluidClassic {

    public BlockMiniaturizationFluid() {
        super(Fluidss.miniaturizationFluid, Material.WATER);
        this.setUnlocalizedName("miniaturization_fluid_block");
        this.setCreativeTab(CreativeTabCM2.CM2_TAB);

        this.quantaPerBlock = 4;
        this.quantaPerBlockFloat = 4F;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        super.onEntityCollidedWithBlock(world, pos, state, entity);

        if(!(entity instanceof EntityLivingBase)) {
            return;
        }

        // TODO: Make this configurable, i.e. disable the effect, change duration, amplifier
        int duration = 20;
        int amplifier = 1;

        EntityLivingBase living = (EntityLivingBase) entity;
        PotionEffect active = living.getActivePotionEffect(Potionss.tinyPlayerPotion);

        PotionEffect effect = new PotionEffect(Potionss.tinyPlayerPotion, duration, amplifier, false, false);
        if(active != null) {
            active.combine(effect);
        } else {
            living.addPotionEffect(effect);
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        BlockPos adjacent[] = {
                pos.add(  1,  0,  0),
                pos.add( -1,  0,  0),
                pos.add(  0,  0,  1),
                pos.add(  0,  0, -1),

                pos.add(  1,  0,  1),
                pos.add(  1,  0, -1),
                pos.add( -1,  0, -1),
                pos.add( -1,  0,  1)
        };

        if(isSourceBlock(world, pos)) {
            // Flow sideways
            for(BlockPos adjPos : adjacent) {
                flowIntoBlock(world, adjPos, 4);
            }
        } else {
            // Find all nearby source blocks
            boolean hasAdjacentSourceBlock = false;
            for(BlockPos adjPos : adjacent) {
                if(this.isSourceBlock(world, adjPos)) {
                    hasAdjacentSourceBlock = true;
                    break;
                }
            }

            // Drain if no adjacent source block and no miniaturization fluid above
            if(!hasAdjacentSourceBlock && world.getBlockState(pos.up()).getBlock() != this) {
                int quantaRemaining = quantaPerBlock - state.getValue(LEVEL);
                if(quantaRemaining > 0) {
                    world.setBlockState(pos, this.getBlockState().getBaseState().withProperty(LEVEL, state.getValue(LEVEL) + 1));
                } else if(quantaRemaining <= 1) {
                    world.setBlockToAir(pos);
                }
            }
        }

        // Flow downwards
        if (canDisplace(world, pos.down())) {
            flowIntoBlock(world, pos.down(), 1);
            return;
        }
    }
}