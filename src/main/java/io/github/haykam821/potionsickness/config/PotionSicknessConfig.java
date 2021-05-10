package io.github.haykam821.potionsickness.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "potionsickness")
@Config.Gui.Background("minecraft:textures/block/nether_wart_block.png")
public class PotionSicknessConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	public int potionCooldown = 20 * 60;
}