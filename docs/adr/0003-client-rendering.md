# ADR 0003: Client Rendering

## Status

Accepted

---

## Context

Deciding between various rendering methods in 2D and 3D is integral to the feel of the game.
While 3D most closely resembles the current state of OSRS, creating the logic for such an
engine as well as the assets themselves will take a substantially more amount of time than
a 2D alternative. On the other hand, a 2D game will heavily limit what is possible within
the world, and will lead to a lot more creativity necessary to have an interactive experience.

---

## Decision

I ended up going with a 2D rendering system for the client. A majority of the rendering logic
lives withing the `GameWindow` class. The key components of this class are:

- `Camera`: Defines the size of the viewport, tiles, and position (centered on the player).
- `ClientState`: Holds a collection of players states that can be referenced by other players. Additionally,
a localPlayerId is held to notify the client how to render the local player differently from others,
and the position for the camera to be centered on
- `PlayerState`: A Record holding a player's id and position (client side representation of player's server state)
- `TileMapData`: Temporary test map replicating the same test map on the server side

This is being implemented primarily with JavaFX, which requires a separate thread to be created for the
networking logic as the `Application` blocks the current thread. Thus, what previously lived in `GameClient`
has been moved to `NetworkThread`, which is initialized a started within `GameWindow.start()`. `GameClient`
is now used to simply launch the `Application`.

---

## Consequences

Advantages:

- Much simpler to iterate sprite/asset design
- Quick implementation to get things rendering for the client

Potential downside:

- Less immersive experience
- Not as visually appealing to a large number of potential users