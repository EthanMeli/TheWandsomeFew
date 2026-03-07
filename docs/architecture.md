# Architecture

## Overview

The project uses a **client-server architecture**.

The server acts as the authoritative source of truth for the game world.

All game logic runs on the server and the client acts primarily as a visualization and input layer.

---

# Server Architecture

The server is composed of several major systems.

## Tick Engine

The game runs on a fixed update loop called a **tick**.

Each tick represents one simulation step.

Example:
```
Tick Interval: 600ms
```

On each tick the server processes:

- Player actions
- NPC behavior
- Movement updates
- World events

The tick engine ensures the game world updates consistently.

---

## Game World

The world contains:

- Players
- NPCs
- Game objects
- Map regions

Each entity exists within the world and is updated every tick.

---

## Entity System

Entities represent all interactive objects in the game.

Examples:

- Players
- NPCs
- Items
- Objects

Each entity will eventually contain:

- Position
- State
- Behavior logic

---

## Networking

The server will communicate with clients using a custom packet-based protocol.

Typical packet types include:

Client → Server
- movement
- interaction
- login
- actions

Server → Client
- world updates
- entity updates
- animations
- chat

Networking architecture will be expanded as the project develops.

---

## Future Architecture Systems

Planned additions include:

- Region-based world loading
- Pathfinding
- Combat engine
- Inventory management
- Questing system