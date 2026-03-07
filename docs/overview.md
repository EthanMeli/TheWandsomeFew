# Project Overview

## Purpose

This project aims to recreate the architecture of a classic MMORPG using a custom client and server.

The goal is to explore and understand the underlying systems that power large-scale multiplayer games.

The project focuses primarily on backend architecture including:

- Game loops
- Networking
- Entity systems
- World simulation
- Data synchronization

---

## High-Level Design

The game uses a **client-server architecture**.

Server responsibilities:

- Maintain authoritative world state
- Process player actions
- Run game logic
- Manage NPCs and entities
- Synchronize world updates

Client respobilities:

- Render the game world
- Capture player input
- Display UI
- Send actions to the server

The server operates on a **fixed tick system**, meaning all game logic runs at a constant interval.

---

## Long-Term Systems

Planned systems include:

World Systems
- Map regions
- Tile-based movement
- Collision detection

Gameplay Systems
- Inventory
- Items
- Skills
- Combat
- Quests

Networking Systems
- Packet serialization
- Client-server protocol
- Player synchronization

Entity Systems
- Players
- NPCs
- Game objects

---

## Project Philosophy

The goal of this project is to:

- Build systems incrementally
- Document architectural decisions
- Maintain clean, understandable code
- Focus on core gameplay systems before polish

The emphasis is on **learning and experimentation** rather than speed of development.