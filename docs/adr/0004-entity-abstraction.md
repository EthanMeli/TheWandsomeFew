# ADR 0004: Entity Abstraction

## Status

Accepted

---

## Context

Before this abstraction, players were the only entites on the world that were created. To introduce
NPCs, it was essential to create a sort of abstraction that both NPCs and Players could stem from,
putting in place the architecture for future entities and NPCs to be added.

---

## Decision

Several layers of abstraction were created for this, starting from the base `Entity` class. This sets
up the very basic features that should be included on all entities (including non living ones such as 
banks and other items in the future). From here, we introduced a `LivingEntity` class that implements
`Entity`, and adds specific functions and additional features such as `combatTarget`, `hp`, and pathing
(moved from the `Player` class). We then created two main classes stemming from this: `Npc` and `Player`.
`Npc` introduces specific features such as `NpcType` (indicating what sprite to render on the client side),
and `spawnTile` (helping with leashing implementation with combat and spawning the entity at different
locations). `Player` doesn't have any noticeable changes from `LivingEntity` as of this point.

---

## Consequences

Advantages:

- This abstraction sets the foundation for future entities to be created (both living and non-living)
- Organized structure so changes can easily be made to derivations of the base class

Potential downside:

- Any changes to base classes will have to be applied to all cascading entities (may introduce complications)