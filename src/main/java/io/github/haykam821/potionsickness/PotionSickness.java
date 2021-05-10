package io.github.haykam821.potionsickness;

import io.github.haykam821.potionsickness.config.PotionSicknessConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class PotionSickness implements ModInitializer {
	public static final String MOD_ID = "potionsickness";

	@Override
	public void onInitialize() {
		AutoConfig.register(PotionSicknessConfig.class, GsonConfigSerializer::new);
	}
}
