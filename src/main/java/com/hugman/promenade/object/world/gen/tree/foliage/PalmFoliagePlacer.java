package com.hugman.promenade.object.world.gen.tree.foliage;

import com.hugman.promenade.init.CommonBundle;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Random;
import java.util.function.BiConsumer;

public class PalmFoliagePlacer extends FoliagePlacer {
	public static final Codec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
			PalmFoliagePlacer.fillFoliagePlacerFields(instance)
					.apply(instance, PalmFoliagePlacer::new));

	public PalmFoliagePlacer(IntProvider radius, IntProvider offset) {
		super(radius, offset);
	}

	@Override
	protected FoliagePlacerType<?> getType() {
		return CommonBundle.PALM_FOLIAGE_PLACER;
	}

	@Override
	protected void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
		boolean bl = treeNode.isGiantTrunk();
		BlockPos blockPos = treeNode.getCenter().down();

		int i = radius + treeNode.getFoliageRadius();
		if(i > 1) this.generateSquare(world, replacer, random, config, blockPos, i, 2, bl);
		this.generateSquare(world, replacer, random, config, blockPos, i + 1,  1, bl);
		this.generateSquare(world, replacer, random, config, blockPos, i + 2,  0, bl);
		this.generateSquare(world, replacer, random, config, blockPos, i + 2, -1, bl);
		this.generateSquare(world, replacer, random, config, blockPos, i + 1, -2, bl);
	}

	@Override
	public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
		return 0;
	}

	@Override
	protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
		int d = dx + dz;

		if(y == 2) {
			return (d <= radius - 2) || (d > radius);
		}
		else if(y == 1) {
			if(dx == radius) return dz != 0;
			if(dz == radius) return dx != 0;

			return false;
		}
		else if(y == 0) {
			if(d <= 2) return false;

			// NESW leafs
			if(dx == radius) return dz != 0;
			if(dz == radius) return dx != 0;

			// Corner leafs
			if(dx == radius - 2 && dz == radius - 2) return false;
			if(dx == radius - 2 && dz == radius - 1) return false;
			if(dx == radius - 1 && dz == radius - 2) return false;

			return true;
		}
		else if(y == -1) {
			if(d == 0) return false;
			if(d == 1) return radius <= 2;

			// NESW leafs
			if(dx == radius) return dz != 0;
			if(dz == radius) return dx != 0;

			// Corner leafs
			if(d == (radius - 1) * 2 && dx == dz) return false;

			return true;
		}
		else if(y == -2) {
			// Corner leafs
			if(d == radius * 2 && dx == dz) return false;

			return true;
		}
		return true;
	}
}