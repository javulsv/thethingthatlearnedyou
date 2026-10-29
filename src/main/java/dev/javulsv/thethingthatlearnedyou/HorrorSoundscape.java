package dev.javulsv.thethingthatlearnedyou;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/** Early, deliberately subtle sound cues for the mod's slow-burn horror. */
public final class HorrorSoundscape {
    private static final int MIN_DELAY_TICKS = 20 * 50;
    private static final int EXTRA_DELAY_TICKS = 20 * 70;
    private static final Map<UUID, Integer> COOLDOWNS = new HashMap<>();
    private static final Random RANDOM = new Random();

    private HorrorSoundscape() {}

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                if (player.isSpectator() || player.isSleeping() || player.level().dimension() != Level.OVERWORLD) {
                    continue;
                }

                UUID id = player.getUUID();
                int remaining = COOLDOWNS.getOrDefault(id, MIN_DELAY_TICKS + RANDOM.nextInt(EXTRA_DELAY_TICKS + 1));
                if (remaining > 0) {
                    COOLDOWNS.put(id, remaining - 1);
                    continue;
                }

                playOneSubtleCue(player);
                COOLDOWNS.put(id, MIN_DELAY_TICKS + RANDOM.nextInt(EXTRA_DELAY_TICKS + 1));
            }
            COOLDOWNS.keySet().removeIf(id -> server.getPlayerList().getPlayer(id) == null);
        });
    }

    private static void playOneSubtleCue(ServerPlayer player) {
        var level = player.level();
        var lookBehind = player.getLookAngle().scale(-1.0);
        double x = player.getX() + lookBehind.x * 6.0;
        double y = player.getY() + Math.max(0.0, lookBehind.y * 2.0);
        double z = player.getZ() + lookBehind.z * 6.0;

        switch (RANDOM.nextInt(3)) {
            case 0 -> level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMBIENT_CAVE, SoundSource.AMBIENT, 0.45F, 0.7F + RANDOM.nextFloat() * 0.2F);
            case 1 -> level.playSound(null, x, y, z,
                    SoundEvents.BLOCK_STONE_STEP, SoundSource.AMBIENT, 0.22F, 0.55F + RANDOM.nextFloat() * 0.15F);
            default -> level.playSound(null, player.getX() + lookBehind.x * 3.0, player.getY(), player.getZ() + lookBehind.z * 3.0,
                    SoundEvents.ENTITY_ENDERMAN_STARE, SoundSource.AMBIENT, 0.18F, 0.65F);
        }
    }
}
