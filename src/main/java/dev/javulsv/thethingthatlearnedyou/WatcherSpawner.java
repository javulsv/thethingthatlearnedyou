package dev.javulsv.thethingthatlearnedyou;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * Rare, bounded Watcher spawning. The dimension check is intentional:
 * normal Overworld worlds and every other dimension are ignored.
 */
public final class WatcherSpawner {
    private static final long CHECK_INTERVAL_TICKS = 200L;
    private static final int SPAWN_CHANCE_DENOMINATOR = 24;
    private static final double MIN_DISTANCE = 24.0D;
    private static final double MAX_DISTANCE = 36.0D;

    private WatcherSpawner() {}

    public static void register() {
        ServerTickEvents.END_LEVEL_TICK.register(WatcherSpawner::onLevelTick);
    }

    private static void onLevelTick(ServerLevel level) {
        if (!level.dimension().equals(WatcherEntities.FAKE_OVERWORLD)) {
            return;
        }

        if (level.getGameTime() % CHECK_INTERVAL_TICKS != 0L || hasWatcher(level)) {
            return;
        }

        var players = level.getPlayers(player -> player.isAlive() && !player.isSpectator());
        if (players.isEmpty()) {
            return;
        }

        RandomSource random = level.getRandom();
        if (random.nextInt(SPAWN_CHANCE_DENOMINATOR) != 0) {
            return;
        }

        ServerPlayer player = players.get(random.nextInt(players.size()));
        trySpawnNearButBehind(level, player, random);
    }

    private static boolean hasWatcher(ServerLevel level) {
        for (Entity entity : level.getAllEntities()) {
            if (entity instanceof WatcherEntity) {
                return true;
            }
        }
        return false;
    }

    private static void trySpawnNearButBehind(ServerLevel level, ServerPlayer player, RandomSource random) {
        double lookX = player.getLookAngle().x;
        double lookZ = player.getLookAngle().z;
        double lookLength = Math.sqrt(lookX * lookX + lookZ * lookZ);
        if (lookLength < 0.001D) {
            return;
        }
        lookX /= lookLength;
        lookZ /= lookLength;

        // Pick a point in the rear half-plane, so it is more likely to be
        // noticed only when the player turns around.
        double angle = random.nextDouble() * (Math.PI * 2.0D);
        double directionX = Math.cos(angle);
        double directionZ = Math.sin(angle);
        if (directionX * lookX + directionZ * lookZ > -0.15D) {
            return;
        }

        double distance = MIN_DISTANCE + random.nextDouble() * (MAX_DISTANCE - MIN_DISTANCE);
        int x = Mth.floor(player.getX() + directionX * distance);
        int z = Mth.floor(player.getZ() + directionZ * distance);
        BlockPos reference = new BlockPos(x, player.blockPosition().getY(), z);

        // Never force-load a chunk just to create a horror encounter.
        if (!level.hasChunkAt(reference)) {
            return;
        }

        BlockPos spawnPos = level.getHeightmapPos(
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                reference
        );
        if (!level.isEmptyBlock(spawnPos)
                || !level.isEmptyBlock(spawnPos.above())
                || !level.getFluidState(spawnPos).isEmpty()) {
            return;
        }

        double spawnX = spawnPos.getX() + 0.5D;
        double spawnY = spawnPos.getY();
        double spawnZ = spawnPos.getZ() + 0.5D;
        double distanceToPlayer = player.distanceToSqr(spawnX, spawnY, spawnZ);
        if (distanceToPlayer < MIN_DISTANCE * MIN_DISTANCE
                || distanceToPlayer > 42.0D * 42.0D) {
            return;
        }

        // Prefer dim conditions. It may still appear in caves or at dusk.
        if (level.getMaxLocalRawBrightness(spawnPos) > 11) {
            return;
        }

        WatcherEntity watcher = new WatcherEntity(WatcherEntities.WATCHER, level);
        watcher.snapTo(spawnX, spawnY, spawnZ, random.nextFloat() * 360.0F, 0.0F);
        watcher.setPersistenceRequired();
        level.addFreshEntity(watcher);
    }
}
