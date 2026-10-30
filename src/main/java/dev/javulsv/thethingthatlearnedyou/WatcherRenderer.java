package dev.javulsv.thethingthatlearnedyou;

import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.resources.Identifier;

/**
 * Uses Minecraft's complete vanilla zombie humanoid rig. Reusing the
 * connected vanilla model hierarchy avoids independently animated limbs.
 */
public final class WatcherRenderer extends MobRenderer<WatcherEntity, ZombieRenderState, ZombieModel<ZombieRenderState>> {
    private static final Identifier TEXTURE =
            Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png");

    public WatcherRenderer(EntityRendererProvider.Context context) {
        super(context, new ZombieModel<>(context.bakeLayer(ModelLayers.ZOMBIE)), 0.45F);
    }

    @Override
    public ZombieRenderState createRenderState() {
        return new ZombieRenderState();
    }

    @Override
    public void extractRenderState(WatcherEntity entity, ZombieRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.isAggressive = false;

        // One or two short head-only movements, and only while it is far
        // enough away to be mistaken for a rendering glitch.
        if (state.distanceToCameraSq > 225.0D
                && state.distanceToCameraSq < 3600.0D
                && state.walkAnimationSpeed < 0.08F) {
            int phase = Math.floorMod(entity.tickCount + entity.getId() * 31, 260);
            float direction = (entity.getId() & 1) == 0 ? 1.0F : -1.0F;
            if (phase == 226) {
                state.yRot += direction * 9.0F;
            } else if (phase == 227) {
                state.yRot -= direction * 5.0F;
            }
        }
    }

    @Override
    public Identifier getTextureLocation(ZombieRenderState state) {
        return TEXTURE;
    }

    @Override
    protected int getModelTint(ZombieRenderState state) {
        // A faded, desaturated-looking version of the familiar vanilla skin.
        return 0xFF929292;
    }
}
