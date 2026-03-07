# Setup Guide

This guide explains how to run the project locally.

---

# Prerequisites

Install the following:

- Java 25
- Git

Optional tools:

- IntelliJ IDEA
- VSCode

---

# Clone the Repository

```
git clone https://github.com/ethanmeli/thewandsomefew.git
cd thewandsomefew
```

---

# Running the Server

Compile the server:
```
./gradlew build
```

Run the server:
```
./gradlew run
```

Or run directly from your IDE.

---

# Expected Behavior

When the server starts you should see:

- Tick engine initialization
- Server startup message
- Game loop running

---

# Common Issues

### Java version mismatch

Ensure Java 25 is installed.
```
java -version
```

---

### Port conflicts

If networking is added later, ensure the configured port is not already in use.

---

# Development Workflow

Recommended workflow:

1. Create feature branch
2. Implement system
3. Update documentation
4. Commit changes
5. Merge into main