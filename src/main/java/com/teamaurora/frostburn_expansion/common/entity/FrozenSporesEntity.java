package com.teamaurora.frostburn_expansion.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class FrozenSporesEntity extends ProjectileItemEntity {
    public FrozenSporesEntity(EntityType<? extends FrozenSporesEntity> p_i50159_1_, World p_i50159_2_) {
        super(p_i50159_1_, p_i50159_2_);

    }

    public FrozenSporesEntity(World worldIn, LivingEntity throwerIn) {
        super(EntityType.SNOWBALL, throwerIn, worldIn);
    }

    public FrozenSporesEntity(World worldIn, double x, double y, double z) {
        super(EntityType.SNOWBALL, x, y, z, worldIn);
    }

    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    public void tick() {
        if (isInWater()) {
            this.onWaterImpact();
        }
        super.tick();
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData makeParticle() {
        ItemStack itemstack = this.func_213882_k();
        return (IParticleData) (itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleData(ParticleTypes.ITEM, itemstack));
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            IParticleData iparticledata = this.makeParticle();

            for (int i = 0; i < 8; ++i) {
                this.world.addParticle(iparticledata, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected void onImpact(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) result).getEntity();
            entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), 0);
        } else if (result.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos pos = this.getPosition();
            World worldIn = this.getEntityWorld();

            for (int x = -1; x <= 1; ++x) {
                for (int z = -1; z <= 1; ++z) {
                    BlockPos blockPos = pos.add(x, -1, z);
                    BlockState state = worldIn.getBlockState(blockPos);
                    if (state.getBlock() == Blocks.WATER) {
                        worldIn.setBlockState(blockPos, Blocks.ICE.getDefaultState());
                    } else if (state.getBlock() == Blocks.ICE && !(Math.abs(x) == 1 && Math.abs(z) == 1)) {
                        worldIn.setBlockState(blockPos, Blocks.PACKED_ICE.getDefaultState());
                    } else if (state.getBlock() == Blocks.PACKED_ICE && x == 0 && z == 0) {
                        worldIn.setBlockState(blockPos, Blocks.BLUE_ICE.getDefaultState());
                    }
                }
            }
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }

    protected void onWaterImpact() {
        BlockPos pos = this.getPosition();
        World worldIn = this.getEntityWorld();

        for (int x = -1; x <= 1; ++x) {
            for (int y = 1; y <= 3; ++y) {
                for (int z = -1; z <= 1; ++z) {
                    BlockPos blockPos = pos.add(x, y, z);
                    BlockState state = worldIn.getBlockState(blockPos);
                    if (worldIn.isAirBlock(blockPos.up())) {
                        if (state.getBlock() == Blocks.WATER) {
                            worldIn.setBlockState(blockPos, Blocks.ICE.getDefaultState());
                        } else if (state.getBlock() == Blocks.ICE && !(Math.abs(x) == 1 && Math.abs(z) == 1)) {
                            worldIn.setBlockState(blockPos, Blocks.PACKED_ICE.getDefaultState());
                        } else if (state.getBlock() == Blocks.PACKED_ICE && x == 0 && z == 0) {
                            worldIn.setBlockState(blockPos, Blocks.BLUE_ICE.getDefaultState());
                        }
                    }
                }
            }
        }

        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }
}
