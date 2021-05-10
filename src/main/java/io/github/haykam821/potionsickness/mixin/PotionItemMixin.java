package io.github.haykam821.potionsickness.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import io.github.haykam821.potionsickness.component.PotionCooldownComponent;
import io.github.haykam821.potionsickness.component.PotionSicknessComponentInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(PotionItem.class)
public class PotionItemMixin {
	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void preventCooldownUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> ci) {
		ItemStack stack = user.getStackInHand(hand);

		PotionCooldownComponent component = PotionSicknessComponentInitializer.POTION_COOLDOWN.get(user);
		if (!user.abilities.invulnerable && component.isCoolingDown(stack)) {
			ci.setReturnValue(TypedActionResult.fail(stack));
		}
	}

	@Inject(method = "finishUsing", at = @At("RETURN"))
	private void updateCooldown(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> ci) {
		if (!(user instanceof PlayerEntity)) return;

		PotionCooldownComponent component = PotionSicknessComponentInitializer.POTION_COOLDOWN.get(user);
		component.updateCooldown(stack);
	}
}
