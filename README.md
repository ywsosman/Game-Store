# 🎮 Game Store

A console-based Java application for a game store, demonstrating core Java and Object-Oriented Programming principles.

## 📋 Project Overview

This application simulates a game store where you can manage games across different platforms, handle player accounts with tier-based discounts, create orders, and run tournaments.

## 🏗️ Project Structure

```
src/main/java/org/example/
├── Main.java                              # Application entry point
│
├── enums/
│   ├── Genre.java                         # Game genres (ACTION, RPG, SPORTS, etc.)
│   └── OrderStatus.java                   # Order lifecycle (PENDING, CONFIRMED, CANCELLED)
│
├── interfaces/
│   └── Searchable.java                    # Generic search contract for catalogs
│
├── exception/
│   ├── GameStoreException.java            # Base exception for all domain errors
│   ├── InvalidPriceException.java         # Thrown when game price < 0
│   ├── InvalidRatingException.java        # Thrown when rating outside 0.0–5.0
│   ├── TournamentFullException.java       # Thrown when tournament at max capacity
│   ├── DuplicateRegistrationException.java # Thrown when player already registered
│   └── EmptyOrderException.java           # Thrown when checking out empty order
│
├── model/
│   ├── game/
│   │   ├── Game.java                      # Abstract base class for all games
│   │   ├── PCGame.java                    # PC platform games
│   │   ├── ConsoleGame.java               # Console platform games
│   │   └── MobileGame.java               # Mobile platform games
│   │
│   ├── player/
│   │   ├── Player.java                    # Abstract base class for all players
│   │   ├── RegularPlayer.java             # Standard player (no discount)
│   │   └── VIPPlayer.java                 # Premium player (configurable discount)
│   │
│   ├── order/
│   │   └── Order.java                     # Shopping cart with checkout & discount logic
│   │
│   └── tournament/
│       └── Tournament.java                # Competition with elimination bracket
│
├── service/                               # (Phase 4 — upcoming)
├── data/                                  # (Phase 5 — upcoming)
└── ui/                                    # (Phase 6 — upcoming)
```

## 🧩 What's Been Implemented

### Phase 1 — Foundation

**Enums:**
- `Genre` — 8 game genres (ACTION, ADVENTURE, RPG, SPORTS, STRATEGY, PUZZLE, SIMULATION, HORROR) with display names and case-insensitive lookup.
- `OrderStatus` — Order lifecycle states: PENDING → CONFIRMED / CANCELLED.

**Interfaces:**
- `Searchable<T>` — Generic search contract with `searchByName()` and `searchByGenre()` methods.

**Custom Exceptions (all extend `GameStoreException`):**
| Exception | Trigger |
|---|---|
| `InvalidPriceException` | Game price set to a negative value |
| `InvalidRatingException` | Game rating set outside 0.0–5.0 range |
| `TournamentFullException` | Player registration when tournament is at capacity |
| `DuplicateRegistrationException` | Player already registered in the same tournament |
| `EmptyOrderException` | Attempting to checkout an order with no games |

---

### Phase 2 — Domain Models

**Game Hierarchy:**

| Class | Type | Key Field | Description |
|---|---|---|---|
| `Game` | Abstract | — | Base class with `id`, `name`, `price`, `genre` (enum), `rating`. Validates price ≥ 0 and rating 0–5 |
| `PCGame` | Concrete | `operatingSystem` | Games for PC (Windows, Linux, macOS) |
| `ConsoleGame` | Concrete | `consoleBrand` | Games for consoles (PlayStation, Xbox, Nintendo) |
| `MobileGame` | Concrete | `freeToPlay` | Mobile games with free-to-play flag |

**Player Hierarchy:**

| Class | Type | Discount | Description |
|---|---|---|---|
| `Player` | Abstract | — | Base class with `id`, `name`, `email` |
| `RegularPlayer` | Concrete | None | Standard player, pays full price |
| `VIPPlayer` | Concrete | 20% (configurable) | Premium player with configurable discount rate |

---

### Phase 3 — Business Logic

**Order:**
- Add/remove games from a cart.
- Calculate total price with VIP discount applied.
- Checkout validation — cannot checkout an empty order.
- Status tracking: PENDING → CONFIRMED / CANCELLED.
- Receipt-style `toString()` output with discount breakdown.

**Tournament:**
- Player registration with capacity limits and duplicate checks.
- Elimination bracket simulation with randomized matchups.
- Bye support for odd number of players.
- Match log recording every round and result.
- Winner determination.

---

## 🔧 OOP Concepts Used

| Concept | Implementation |
|---|---|
| **Encapsulation** | Private fields with getters/setters. Validation in setters (price, rating) |
| **Inheritance** | `Game` → `PCGame` / `ConsoleGame` / `MobileGame`. `Player` → `RegularPlayer` / `VIPPlayer` |
| **Abstract Classes** | `Game` and `Player` — cannot be instantiated directly |
| **Method Overriding** | `getPlatform()`, `getPlayerType()`, `toString()` overridden in each subclass |
| **Polymorphism** | `Order` works with any `Player` subtype; `Tournament` manages `List<Player>` |
| **Interface** | `Searchable<T>` — generic search contract |
| **Enums** | `Genre` (with display names + lookup), `OrderStatus` (order lifecycle) |
| **Custom Exceptions** | 5 domain exceptions extending `GameStoreException` → `RuntimeException` |
| **Java Collections** | `List<Game>`, `List<Player>`, `ArrayList` used throughout |
| **Lambda Expressions** | `removeIf(game -> game.getId() == gameId)` in Order |

## 🛡️ Edge Cases Handled

- ❌ Negative game prices → `InvalidPriceException`
- ❌ Rating outside 0.0–5.0 → `InvalidRatingException`
- ❌ Duplicate tournament registration → `DuplicateRegistrationException`
- ❌ Full tournament registration → `TournamentFullException`
- ❌ Empty order checkout → `EmptyOrderException`
- ❌ Starting a tournament twice → `IllegalStateException`
- ❌ Starting a tournament with < 2 players → `IllegalStateException`

## 🔜 Upcoming Phases

| Phase | Description | Status |
|---|---|---|
| Phase 4 | `GameStore` service — central manager with search & sort | ⬜ Planned |
| Phase 5 | XML persistence — save/load data to files | ⬜ Planned |
| Phase 6 | Console menu UI — interactive user interface | ⬜ Planned |
| Phase 7 | README finalization, testing, polish | ⬜ Planned |

## ⚙️ Prerequisites

- **Java 26** (OpenJDK 26+)
- No external frameworks or dependencies required

## 🚀 How to Run

### Compile
```bash
javac -d target/classes -sourcepath src/main/java src/main/java/org/example/Main.java
```

### Run
```bash
java -cp target/classes org.example.Main
```



## 📝 License

This project is for educational purposes as part of a Java Fullstack course assignment.
