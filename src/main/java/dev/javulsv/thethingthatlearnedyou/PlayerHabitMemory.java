package dev.javulsv.thethingthatlearnedyou;

import java.util.EnumMap;
import java.util.Map;

/**
 * Small, bounded memory of a player's repeated habits.
 *
 * <p>This class deliberately does not perform world actions. Event hooks can
 * record observed actions, and the Watcher AI can consult the preferred habit
 * when deciding what to imitate.</p>
 */
public final class PlayerHabitMemory {
    private static final int MAX_OBSERVATIONS_PER_HABIT = 1_000;
    private static final long RECENCY_WINDOW_TICKS = 20L * 60L * 30L;

    public enum Habit {
        MINING_STONE,
        OPENING_CHEST,
        PLACING_TORCH,
        BUILDING
    }

    private final Map<Habit, Observation> observations = new EnumMap<>(Habit.class);

    /** Records one observed action at the supplied world-game tick. */
    public void record(Habit habit, long gameTick) {
        if (habit == null || gameTick < 0) {
            return;
        }

        Observation old = observations.get(habit);
        int count = old == null ? 1 : Math.min(MAX_OBSERVATIONS_PER_HABIT, old.count() + 1);
        observations.put(habit, new Observation(count, gameTick));
    }

    /**
     * Returns the most repeated recently observed habit, or null when there is
     * not enough recent evidence to imitate anything reliably.
     */
    public Habit preferredHabit(long currentGameTick) {
        if (currentGameTick < 0) {
            return null;
        }

        Habit bestHabit = null;
        double bestScore = 0.0;

        for (Map.Entry<Habit, Observation> entry : observations.entrySet()) {
            Observation observation = entry.getValue();
            long age = currentGameTick - observation.lastSeenTick();
            if (age < 0 || age > RECENCY_WINDOW_TICKS) {
                continue;
            }

            // Recent, repeated actions score higher; old habits gradually fade.
            double recency = 1.0 - ((double) age / RECENCY_WINDOW_TICKS);
            double score = observation.count() * recency;
            if (score > bestScore) {
                bestScore = score;
                bestHabit = entry.getKey();
            }
        }

        return bestHabit;
    }

    /** Clears learned observations, for example when a player's data is reset. */
    public void clear() {
        observations.clear();
    }

    private record Observation(int count, long lastSeenTick) {}
}
