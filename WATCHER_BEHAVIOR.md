# The Watcher: learned-player activity

This document defines the next gameplay feature for **The Thing That Learned You**.

## Core idea

When the player leaves their home or mine, the Watcher may imitate the player's ordinary routines. It should feel as if another player is quietly using the world, not as if a standard hostile mob is stalking the player.

## Encounter beats

1. **Distant activity:** While the player is away from a familiar area, occasional stone-mining or chest-use sounds may come from behind the player or from the direction of home.
2. **Evidence:** On returning, the player may find a chest left open, a small amount of stone removed from a familiar tunnel, or a torch placed in a location that resembles their own habits. Changes should be rare, limited, and reversible where practical.
3. **Witnessed imitation:** Sometimes the player can spot the Watcher at a distance, mining or interacting with a chest. It should pause if noticed, then disappear after the player approaches or loses sight of it.
4. **Unnatural head movement:** While it is standing still and staring at the player from far away, its head may make one or two tiny, abrupt twitches. Keep these rare and brief, with long still periods between them. The twitch should affect only the head rotation, not make the whole body shake; it should look like an animation glitch rather than a cartoon movement. It must stop once the Watcher disappears or resumes an action.
5. **Learning:** Over time, it favors locations and actions associated with that player's routines. It should not know everything instantly or copy actions perfectly.
6. **Escalation:** Later encounters can imitate more specific habits, such as torch placement or the route through a mine. The creature should not immediately attack; uncertainty is the horror.

## Fairness and multiplayer

- Keep encounters uncommon and unpredictable, with long cooldowns.
- Never destroy valuable builds or silently take valuable items from inventories.
- Avoid changing protected/claimed areas and avoid dangerous block edits near the player.
- Keep each player's observations and activity cooldowns separate in multiplayer.
- Provide configuration or a way to disable world edits if the feature grows beyond sound cues.

## Implementation stages

- **Stage 1 — sound prototype:** rare, distant mining/chest cues when a player is away from a detected home area.
- **Stage 2 — visible Watcher:** a custom humanoid entity that can be observed performing a short imitation animation, with head-only twitch animation during rare distant staring encounters.
- **Stage 3 — limited world interactions:** carefully bounded chest and mining interactions, with checks that prevent griefing.
- **Stage 4 — habit learning:** track simple, local patterns such as frequently used mine entrances and torch placement.

The current repository contains only the early horror soundscape prototype. This document is a design target, not a claim that the Watcher already performs these actions in-game.
