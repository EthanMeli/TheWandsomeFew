# ADR 0001: Initial Project Structure

## Status

Accepted

---

## Context

The project requires a clear separation between server code, client code, and documentation.

Without a structured repository layout the project could become difficult to maintain as it grows.

---

## Decision

The repository will be structured as:

```
/server
/client
/docs
/assets
```

Documentation will live inside `/docs` to ensure it is versioned with the code.

---

## Consequences

Advantages:

- Clear separation of concerns
- Easier onboarding for new contributors
- Documentation evolves with the code

Potential downside:

- Slightly more files to manage early in development