# ADR 0002: Tile Map & Pathfinding Algorithm

## Status

Accepted

---

## Context

A key element of this project is pathfinding from a player's current position to a destination tile.
This isn't as straight forward as it seems as there are many consequences to consider (i.e., which
algorithm to implement, how to navigate around invalid tiles, the distance a player can walk from).
The idea of this document is to discuss the methods that will be implemented for pathfinding, as well
as the rationale behind certain decisions. We will also discuss the construction of the test tilemap,
where it lives, and how it is handled by different components of the project.

---

## Decision

The tile map will eventually be extensive, and include much more information that what is written here.
However, this initial implementation will be the basis that we expand upon, and felt that mentioning
what considerations were taken into account is worthwhile:

- A `Tile` record will simply store its coordinates and whether it can be walked to (important for navigating
  around walls, etc.)
- A `TileMap` class will hold the grid of Tiles, exposing functions such as `isWalkable(int x, int y)` and `inBounds(int x, int y)`.
  A static createTestMap() factory will also be created for now for testing purposes
- `TileMap` will be wired into `World`, constructed at startup and held as a field

A version of BFS will be implemented as the pathfinding algorithm for this project. There are a few
additional important factors that are worth considering for this implementation:

- Walk targets will be validated in `processActions()`, checking things such as `map.isWalkable(x, y)` before calling
  `setWalkTarget`. Invalid targets will be silently dropped for now (but will eventually be brought back to handle
  cases such as moving a player to the closest tile to their desired target destination)
- The destination distance must be limited from the player's current position (we will only look at)
  tiles within a 128x128 grid centered around the player
- `BfsPathFinder` class will implement a single pass using a bounded BFS (refer to limited area centered around player above).
  This will include a single function `findPath(from, Set<Tile> acceptableTargets)` and will return a path that is stored in
  the corresponding player (we use a set of acceptableTargets here in preparation for cases where we handle talking to NPCs, etc). 
  To match OSRS's style, the following order will be considered when exploring neighbording tiles: <strong>West, East, South, North, 
  South-west, South-east, North-west, North-east</strong>.
- `Player` is refactored to include its path, storing a `Deque<Tile> path` field and dropping its existing `targetX` and `targetY` fields.
  Create a new method `setPath(Deque<Tile> path)` which will be called in `tickMovement()` (polling one tile per tick).
- Putting it altogether in `World`: Walk handler validates target, runs pathfinder, calls `player.setPath(...)`
- <Strong>Note for future expansion:</Strong> There are two additional things that OSRS implements that we will save for the future.
  The first is a fallback on unreachable targets. OSRS implements a second pass to BFS in the event that a desired target destination
  is unreachable, which scans a grid of 21x21 centered around the destination target and returns the closest reachable tile based on
  tiles that were visited during BFS. Finally, a checkpoint extraction is also implemented where only the first 25 corners are returned
  as the path, and a player moves diagonally until they can reach the next path corner only moving in one of the four cardinal directions.
  This will significantly improve network bandwidth as the number of tiles returned from the pathfinder will greatly decrease, however, due
  to the additional complexity, we will only create this if necessary in the future.

---

## Consequences

Advantages:

- Simple implementation (uses well known algorithm and will be easy to debug with set order)
- Plenty of abstraction: tiles are separate records that are incorporated into a TileMap class,
  which is then passed and only altered by the World

Potential downside:

- Leaving more of the complex implementation for the future, however, should be setup relatively
  well to expand upon as needed