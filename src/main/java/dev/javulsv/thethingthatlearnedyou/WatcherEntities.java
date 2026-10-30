package dev.javulsv.thethingthatlearnedyou;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;

/** Registry entries used by the Watcher and its dimension-only spawner. */
public final class WatcherEntities {
    public static final Identifier WATCHER_ID =
            Identifier.fromNamespaceAndPath(TheThingThatLearnedYou.MOD_ID, "watcher");

    public static final ResourceKey<EntityType<?>> WATCHER_KEY =
            ResourceKey.create(Registries.ENTITY_TYPE, WATCHER_ID);

    public static final ResourceKey<Level> FAKE_OVERWORLD =
            ResourceKey.create(
                    Registries.DIMENSION,
                    Identifier.fromNamespaceAndPath(TheThingThatLearnedYou.MOD_ID, "fake_overworld")
            );

    public static final EntityType<WatcherEntity> WATCHER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            WATCHER_ID,
            EntityType.Builder.of(WatcherEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(10)
                    .updateInterval(2)
                    .notInPeaceful()
                    .build(WATCHER_KEY)
    );

    private WatcherEntities() {}

    public static void register() {
        FabricDefaultAttributeRegistry.register(WATCHER, WatcherEntity.createAttributes());
        TheThingThatLearnedYou.LOGGER.info("Registered the Watcher entity.");
    }
}
