# The Thing That Learned You

A standalone psychological-horror mod for **Minecraft Java Edition 26.1.2 + Fabric**.

The goal is slow-burn horror: the world subtly changes, strange structures echo the player's habits, and a watcher appears only rarely. This repository is separate from Overmind.

## Development requirements

- JDK 25
- Gradle 9.8.2
- Minecraft 26.1.2
- Fabric Loader and Fabric API for 26.1.2

## Build

Run `gradle build`. The mod JAR will be in `build/libs/`.

## Current implementation status

- Initial server-side soundscape prototype: occasional cave ambience, a distant stone-step cue, or a quiet amethyst chime in the Overworld.
- Per-player cooldowns keep the cues infrequent; sleeping players and spectators are skipped.
- Habit-memory groundwork records repeated stone/deepslate mining and chest interaction per player; recent observations can be ranked for future imitation behavior.
- A `thethingthatlearnedyou:fake_overworld` dimension data file points at vanilla Overworld terrain settings and biomes, with a `Fake Overworld` translation key. It is only a scaffold: automatic player routing, reliable menu integration, and subtle terrain anomalies are not implemented yet.
- The first Watcher entity is now registered as a stationary, passive humanoid observer. It stares at nearby players, is removed when approached, and receives rare head-only twitch movements when viewed from a distance.
- The Watcher spawner checks for the custom Fake Overworld dimension key and never spawns in the normal Overworld. Spawns are deliberately rare and do not force-load chunks.
- The first renderer reuses Minecraft's full zombie model hierarchy so the head, torso, arms, and legs remain connected. It uses a muted vanilla zombie texture tint for this prototype.
- The Watcher does not yet imitate mining or chest interactions. It also does not yet have a custom texture or custom audio.
- The subtle Fake Overworld terrain anomalies and complete menu/entry integration are not implemented yet.
- The build workflow validates JSON, compiles the mod, checks that the installable JAR contains a valid `fabric.mod.json`, and uploads the JAR as an artifact.
- A successful build and in-game test are still required before calling this version playable.
