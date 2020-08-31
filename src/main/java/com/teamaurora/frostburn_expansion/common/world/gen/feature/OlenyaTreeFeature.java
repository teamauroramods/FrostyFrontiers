package com.teamaurora.frostburn_expansion.common.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teamaurora.frostburn_expansion.core.registry.FrostburnExpansionBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class OlenyaTreeFeature extends Feature<BaseTreeFeatureConfig> {
    public OlenyaTreeFeature(Codec<BaseTreeFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean func_230362_a_(ISeedReader worldIn, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos position, BaseTreeFeatureConfig config) {

        int type = rand.nextInt(2) + 2;

        if (position.getY() < 1 || position.getY() + type + 3 > worldIn.getHeight()) return false;

            boolean flag = true;
        for (int i=0; i < 1+type; i++) {
            if (!isAirOrLeaves(worldIn, position.up(i))) flag = false;
        }
        int plusX = rand.nextInt(3);
        int minusX = rand.nextInt(3);
        int plusZ = rand.nextInt(3);
        int minusZ = rand.nextInt(3);
        for (int i=1; i <= plusX; i++) {
            if (!isAirOrLeaves(worldIn, position.add(i, type+i, 0))) flag = false;
        }
        for (int i=1; i <= minusX; i++) {
            if (!isAirOrLeaves(worldIn, position.add(-i, type+i, 0))) flag = false;
        }
        for (int i=1; i <= plusZ; i++) {
            if (!isAirOrLeaves(worldIn, position.add(0, type+i, i))) flag = false;
        }
        for (int i=1; i <= minusZ; i++) {
            if (!isAirOrLeaves(worldIn, position.add(0, type+i, -i))) flag = false;
        }

        int canopies = 0;

        if (plusX > 0) {
            canopies++;
            if (checkCanopyBoxAt(worldIn, position.add(plusX, type + plusX, 0))) flag = false;
        }
        if (minusX > 0) {
            canopies++;
            if (checkCanopyBoxAt(worldIn, position.add(-minusX, type + minusX, 0))) flag = false;
        }
        if (plusZ > 0) {
            canopies++;
            if (checkCanopyBoxAt(worldIn, position.add(0, type + plusZ, plusZ))) flag = false;
        }
        if (minusZ > 0) {
            canopies++;
            if (checkCanopyBoxAt(worldIn, position.add(0, type + minusZ, minusZ))) flag = false;
        }
        if (canopies == 0) {
            if (checkCanopyBoxAt(worldIn, position.up(type))) flag = false;
        }

        if (!flag) {
            return false;
        } else if (isValidGround(worldIn, position.down())) {
            for (int i=0; i < 1+type; i++) {
                placeLogAt(worldIn, position.up(i), rand, config);
            }

            for (int i=1; i <= plusX; i++) {
                placeLogAt(worldIn, position.add(i, type+i, 0), rand, config);
            }
            for (int i=1; i <= minusX; i++) {
                placeLogAt(worldIn, position.add(-i, type+i, 0), rand, config);
            }
            for (int i=1; i <= plusZ; i++) {
                placeLogAt(worldIn, position.add(0, type+i, i), rand, config);
            }
            for (int i=1; i <= minusZ; i++) {
                placeLogAt(worldIn, position.add(0, type+i, -i), rand, config);
            }

            if (plusX > 0) {
                placeCanopyAt(worldIn, position.add(plusX, type+plusX, 0), rand, config);
            }
            if (minusX > 0) {
                placeCanopyAt(worldIn, position.add(-minusX, type+minusX, 0), rand, config);
            }
            if (plusZ > 0) {
                placeCanopyAt(worldIn, position.add(0, type+plusZ, plusZ), rand, config);
            }
            if (minusZ > 0) {
                placeCanopyAt(worldIn, position.add(0, type+minusZ, -minusZ), rand, config);
            }
            if (canopies == 0) {
                placeCanopyAt(worldIn, position.up(type), rand, config);
            }

            return true;
        } else {
            return false;
        }
    }

    private void placeCanopyAt(ISeedReader worldIn, BlockPos pos, Random rand, BaseTreeFeatureConfig config) {
        placeLeafAt(worldIn, pos.add(0, -1, 0), rand, config);
        placeLeafAt(worldIn, pos.add(-1, -1, 0), rand, config);
        placeLeafAt(worldIn, pos.add(1, -1, 0), rand, config);
        placeLeafAt(worldIn, pos.add(0, -1, -1), rand, config);
        placeLeafAt(worldIn, pos.add(0, -1, 1), rand, config);

        for (int x=-3; x<=3; x++) {
            for (int z=-3; z<=3; z++) {
                if ((Math.abs(x) == 3 && Math.abs(z) <= 1) || (Math.abs(z) == 3 && Math.abs(x) <= 1)) {
                    placeRandomLeafAt(worldIn, pos.add(x, 0, z), rand, config, 3);
                } else if (Math.abs(x) != 3 || Math.abs(z) != 3) {
                    placeLeafAt(worldIn, pos.add(x, 0, z), rand, config);
                }
                if ((Math.abs(x) == 3 && Math.abs(z) == 2) || (Math.abs(z) == 3 && Math.abs(x) == 2)) {
                    placeRandomLeafAt(worldIn, pos.add(x, 1, z), rand, config, 2);
                } else if (Math.abs(x) != 3 || Math.abs(z) != 3) {
                    placeLeafAt(worldIn, pos.add(x, 1, z), rand, config);
                }
            }
        }

        placeLeafAt(worldIn, pos.add(0, 2, 0), rand, config);
        placeLeafAt(worldIn, pos.add(-1, 2, -1), rand, config);
        placeLeafAt(worldIn, pos.add(-1, 2, 1), rand, config);
        placeLeafAt(worldIn, pos.add(1, 2, -1), rand, config);
        placeLeafAt(worldIn, pos.add(1, 2, 1), rand, config);
        placeRandomLeafAt(worldIn, pos.add(-1, 2, 0), rand, config, 2);
        placeRandomLeafAt(worldIn, pos.add(1, 2, 0), rand, config, 2);
        placeRandomLeafAt(worldIn, pos.add(0, 2, -1), rand, config, 2);
        placeRandomLeafAt(worldIn, pos.add(0, 2, 1), rand, config, 2);

    }

    private boolean checkCanopyBoxAt(ISeedReader worldIn, BlockPos pos) {
        for (BlockPos blockPos : BlockPos.getAllInBoxMutable(pos.add(-3, -1, -3), pos.add(3, 2, 3))) {
            if (!isAirOrLeaves(worldIn, blockPos)) return true;
        }
        return false;
    }

    private void placeLogAt(IWorldWriter worldIn, BlockPos pos, Random rand, BaseTreeFeatureConfig config)
    {
        this.setLogState(worldIn, pos, config.trunkProvider.getBlockState(rand, pos));
    }

    private void placeRandomLeafAt(IWorldGenerationReader world, BlockPos pos, Random rand, BaseTreeFeatureConfig config, int chance) {
        if (rand.nextInt(chance) > 0) {
            placeLeafAt(world, pos, rand, config);
        }
    }

    private void placeLeafAt(IWorldGenerationReader world, BlockPos pos, Random rand, BaseTreeFeatureConfig config)
    {
        if (isAirOrLeaves(world, pos))
        {
            this.setLogState(world, pos, config.leavesProvider.getBlockState(rand, pos).with(LeavesBlock.DISTANCE, 1));
        }
    }

    protected final void setLogState(IWorldWriter worldIn, BlockPos pos, BlockState state) {
        worldIn.setBlockState(pos, state, 18);
    }

    public static boolean isAir(IWorldGenerationBaseReader worldIn, BlockPos pos)
    {
        if (!(worldIn instanceof net.minecraft.world.IBlockReader))
        {
            return worldIn.hasBlockState(pos, BlockState::isAir);
        }
        else
        {
            return worldIn.hasBlockState(pos, state -> state.isAir((net.minecraft.world.IBlockReader) worldIn, pos));
        }
    }

    public static boolean isAirOrLeaves(IWorldGenerationBaseReader worldIn, BlockPos pos)
    {
        if (worldIn instanceof net.minecraft.world.IWorldReader)
        {
            return worldIn.hasBlockState(pos, state -> state.canBeReplacedByLeaves((net.minecraft.world.IWorldReader) worldIn, pos));
        }
        return worldIn.hasBlockState(pos, (state) -> {
            return state.isAir() || state.isIn(BlockTags.LEAVES);
        });
    }

    public static void setDirtAt(IWorld worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.GRASS_BLOCK || block == Blocks.FARMLAND)
        {
            worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState(), 18);
        }
    }

    public static boolean isValidGround(IWorld world, BlockPos pos)
    {
        return world.getBlockState(pos).canSustainPlant(world, pos, Direction.UP, (IPlantable) FrostburnExpansionBlocks.OLENYA_SAPLING.get());
    }
}
