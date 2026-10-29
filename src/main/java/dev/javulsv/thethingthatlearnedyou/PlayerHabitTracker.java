package dev.javulsv.thethingthatlearnedyou;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Observes a few ordinary player actions and stores a small per-player habit
 * profile for later use by the Watcher AI. This class never changes blocks.
 */
public final class PlayerHabitTracker {
    private static final Map<UUID, PlayerHabitMemory> MEMORIES = new HashMap<>();

    private PlayerHabitTracker() {}

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClientSide || player.isSpectator()) {
                return;
            }

            if (isStoneLike(state)) {
                memoryFor(player.getUUID()).record(
                        PlayerHabitMemory.Habit.MINING_STONE, world.getGameTime());
            }
        });

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClientSide || player.isSpectator()) {
                return InteractionResult.PASS;
            }

            BlockState state = world.getBlockState(hitResult.getBlockPos());
            if (state.is(Blocks.CHEST) || state.is(Blocks.TRAPPED_CHEST)) {
                memoryFor(player.getUUID()).record(
                        PlayerHabitMemory.Habit.OPENING_CHEST, world.getGameTime());
            } else if (state.is(Blocks.TORCH) || state.is(Blocks.WALL_TORCH)) {
                // Looking at or using an existing torch is not the same as placing one.
                // Placement is intentionally not inferred from this callback.
            }

            return InteractionResult.PASS;
        });
    }

    public static PlayerHabitMemory memoryFor(UUID playerId) {
        return MEMORIES.computeIfAbsent(playerId, ignored -> new PlayerHabitMemory());
    }

    public static void forget(UUID playerId) {
        MEMORIES.remove(playerId);
    }

    private static boolean isStoneLike(BlockState state) {
        return state.is(Blocks.STONE)
                || state.is(Blocks.DEEPSLATE)
                || state.is(Blocks.COBBLESTONE)
                || state.is(Blocks.COBBLED_DEEPSLATE);
    }
}
