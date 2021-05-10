package io.github.haykam821.potionsickness.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.entity.PlayerComponent;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class PotionCooldownComponent implements PlayerComponent<Component>, AutoSyncedComponent, CommonTickingComponent {
	private final PlayerEntity player;
	private final Object2IntMap<Potion> cooldowns = new Object2IntOpenHashMap<>();

	public PotionCooldownComponent(PlayerEntity player) {
		this.player = player;
	}

	public boolean isCoolingDown(ItemStack stack) {
		Potion potion = PotionUtil.getPotion(stack);
		return potion == null ? false : this.isCoolingDown(potion);
	}

	public boolean isCoolingDown(Potion potion) {
		return this.cooldowns.getInt(potion) > 0;
	}

	public int setCooldown(ItemStack stack, int cooldown) {
		Potion potion = PotionUtil.getPotion(stack);
		return potion == null ? -1 : this.setCooldown(potion, cooldown);
	}

	public int setCooldown(Potion potion, int cooldown) {
		return this.cooldowns.put(potion, cooldown);
	}

	// Serialization
	@Override
	public void readFromNbt(CompoundTag tag) {
		this.cooldowns.clear();

		CompoundTag cooldownsTag = tag.getCompound("Cooldowns");
		for (String key : cooldownsTag.getKeys()) {
			Identifier id = Identifier.tryParse(key);
			if (id == null) continue;

			Potion potion = Registry.POTION.get(id);
			if (potion == null) continue;

			this.cooldowns.put(potion, cooldownsTag.getInt(key));
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag) {
		CompoundTag cooldownsTag = new CompoundTag();
		for (Potion potion : this.cooldowns.keySet()) {
			Identifier id = Registry.POTION.getId(potion);
			cooldownsTag.putInt(id.toString(), this.cooldowns.getInt(potion));
		}

		tag.put("Cooldowns", cooldownsTag);
	}

	// Synchronization
	@Override
	public boolean shouldSyncWith(ServerPlayerEntity player) {
		return player == this.player;
	}
	
	@Override
    public void applySyncPacket(PacketByteBuf buf) {
		this.cooldowns.clear();

		int size = buf.readVarInt();
		for (int index = 0; index < size; index++) {
			Identifier id = buf.readIdentifier();
			Potion potion = Registry.POTION.get(id);

			this.cooldowns.put(potion, buf.readInt());
		}
	}

	@Override
	public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
		buf.writeVarInt(this.cooldowns.size());

		for (Potion potion : this.cooldowns.keySet()) {
			buf.writeIdentifier(Registry.POTION.getId(potion));
			buf.writeInt(this.cooldowns.getInt(potion));
		}
	}

	// Ticking
	@Override
	public void tick() {
		// Decrement the value for each potion
		for (Potion potion : this.cooldowns.keySet()) {
			int value = this.cooldowns.getInt(potion);
			if (value > 0) {
				value -= 1;
			}

			this.cooldowns.put(potion, value);
		}
	}
}
