# java-gui-rpg

# Structure
MVC (Model-View-Controller) architecture, the Model (game engine and logic) is independent of the View (console or GUI). This allows to switch between the different views without changing the core game logic.

- Model: Game logic, rules, and state management (game engine)
- View: Displays the game state to the user (user interface):
    - ConsoleView for terminal output
    - GUIView for graphical output
- Controller: Handles user input and updates the model/view

# Dependencies
- jakarta.validation-api — Provides standard validation annotations and interfaces as part of the Jakarta Bean Validation specification (e.g., @NotNull, @Size).

- hibernate-validator — Reference implementation of Jakarta Bean Validation that executes validation logic at runtime and supports custom constraints.

- expressly — Reference implementation of Jakarta Expression Language (EL) used for dynamic message interpolation in validation error messages.