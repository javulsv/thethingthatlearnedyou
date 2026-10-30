package dev.javulsv.thethingthatlearnedyou;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.renderer.entity.EntityRenderers;

public final class TheThingThatLearnedYouClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRenderers.register(WatcherEntities.WATCHER, WatcherRenderer::new);
    }
}
