# Architecture Diagram

This diagram shows the current and planned high-level architecture of the projec.

## High-Level System Architecture

```mermaid
flowchart TD
  A[Player Client] --> B[Network Layer]
  B --> C[Game Server]

  C --> D[Tick Engine]
  D --> E[World Manager]
  E --> F[Entity System]
  E --> G[Region / Map System]
  E --> H[Collision / Pathfinding]

  F --> I[Player Entities]
  F --> J[NPC Entities]
  F --> K[Game Objects]

  D --> L[Gameplay Systems]
  L --> M[Movement]
  L --> N[Combat]
  L --> O[Inventory]
  L --> P[Skills]
  L --> Q[Questing System]

  C --> R[Session / Login Manager]
  C --> S[Packet / Protocol Handler]

  E --> T[Persistence Layer]
  S --> U[Player Data]
  S --> V[World Data]

```
## Tick Update Flow

```mermaid
flowchart TD
  A[Server Starts] --> B[Tick Engine Begins Loop]
  B --> C{Next Tick Reached?}
  C -- No --> D[Sleep Briefly]
  D --> C
  C -- Yes --> E[Process Incoming Actions]
  E --> F[Update Players]
  F --> G[Update NPCs]
  G --> H[Update World Events]
  H --> I[Send State Updates to Clients]
  I --> J[Advance to Next Tick]
  J --> C