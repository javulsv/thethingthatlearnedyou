package dev.javulsv.thethingthatlearnedyou;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TheThingThatLearnedYou implements ModInitializer {
    public static final String MOD_ID = "thethingthatlearnedyou";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        HorrorSoundscape.register();
        PlayerHabitTracker.register();
        LOGGER.info("The Thing That Learned You is watching.");
    }
}
