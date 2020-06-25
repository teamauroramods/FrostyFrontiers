package com.teamaurora.frostburnexpansion.core.event;

import com.teamaurora.frostburnexpansion.core.registry.FrostburnExpansionEntities;
import com.teamaurora.frostburnexpansion.core.registry.FrostburnExpansionSounds;
import com.teamabnormals.abnormals_core.core.utils.NetworkUtil;
import com.teamaurora.frostburnexpansion.common.entity.BriskEntity;
import com.teamaurora.frostburnexpansion.core.FrostburnExpansion;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
@Mod.EventBusSubscriber(modid=FrostburnExpansion.MODID, bus=Bus.MOD, value=Dist.CLIENT)
public class ClientEvents {
	@SubscribeEvent
	public static void onClientSetup(final FMLClientSetupEvent event) {
		FrostburnExpansionEntities.registerRendering();
		Minecraft minecraft = Minecraft.getInstance();
		minecraft.getSoundHandler().addListener((sound, accessor) -> {
			if (sound.getSoundLocation().toString().contentEquals(FrostburnExpansionSounds.BRISKSONG.getRegistryName().toString())) {
				minecraft.world.getEntitiesWithinAABB(BriskEntity.class, new AxisAlignedBB(new BlockPos(sound.getX(), sound.getY(), sound.getZ())).grow(3.0D)).forEach(entity -> {
					System.out.println("pog");
					if (entity.isNoEndimationPlaying()) {
						entity.setPlayingEndimation(BriskEntity.DANCE);
					}
			    });
			}
		});
	}
}
