package io.github.haykam821.potionsickness.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import io.github.haykam821.potionsickness.PotionSickness;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class PotionSicknessComponentInitializer implements EntityComponentInitializer {
	private static final Identifier POTION_COOLDOWN_ID = new Identifier(PotionSickness.MOD_ID, "potion_cooldown");
	public static final ComponentKey<PotionCooldownComponent> POTION_COOLDOWN = ComponentRegistry.getOrCreate(POTION_COOLDOWN_ID, PotionCooldownComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerForPlayers(POTION_COOLDOWN, PotionCooldownComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
	}
}
