# The Thing That Learned You

A standalone psychological-horror mod for **Minecraft Java Edition 26.1.2 + Fabric**.

The goal is slow-burn horror: the world subtly changes, strange structures echo the player's habits, and a watcher appears only rarely. This repository is separate from Overmind.

## Development requirements

- JDK 25
- Gradle 9.4.0
- Minecraft 26.1.2
- Fabric Loader and Fabric API for 26.1.2

## Build

Run `gradle build`. The mod JAR will be in `build/libs/`.

## Current implementation status

- Initial server-side soundscape prototype: occasional cave ambience, a distant stone-step cue, or a quiet Enderman sound in the Overworld.
- Per-player cooldowns keep the cues infrequent; sleeping players and spectators are skipped.
- Habit-memory groundwork records repeated stone/deepslate mining and chest interaction per player; recent observations can be ranked for future imitation behavior.
- A `thethingthatlearnedyou:fake_overworld` dimension data file now points at vanilla Overworld terrain settings and biomes, with a `Fake Overworld` translation key. It is only a scaffold: automatic player routing, reliable menu integration, and the subtle terrain anomalies are not implemented yet.
- Habit tracking is not yet connected to a Watcher AI, and torch placement/building are not yet observed.
- Sound cues currently reuse vanilla sounds. Original `.ogg` assets, the Watcher entity, safe world interactions, head-twitch animation, and gameplay escalation are not implemented yet.
- The current code still needs a successful automated build and in-game testing before it can be called playable.
