Java role-playing game that can be played either through the terminal or via a graphical user interface built with Swing.

## Structure

#### MVC (Model-View-Controller) architecture

The Model (game engine and logic) is independent of the View (console or GUI). This allows to switch between the different views without changing the core game logic.

- Model: Game logic, rules, and state management (game engine)
- View: Displays the game state to the user (user interface):
    - ConsoleView for terminal output
    - GUIView for graphical output
- Controller: Handles user input and updates the model/view

#### Observer/listener pattern

Observer/listener pattern to decouple the game state (the model) from the user interface (the views).
- The GameEngine acts as the subject/observable.
- Views such as ConsoleView and GUIView implement the GameEngineListener interface and act as observers/listeners.

The GameEngine maintains a list of listeners and notifies them whenever the game state changes. In effect, only one listener (the active view) is registered at a time. 

The main point is the event-driven notification system eliminates the need for a blocking game loop and allows the use of Swing which is non-blocking.

## Java Swing

- One JFrame (GUIView itself as the class 'extends javax.swing.JFrame'). It is the entire game window.
- Multiple JPanels inside it:
    - A main contentPanel (e.g., CardLayout) where to swap each game stages
    - A fixed bottomPanel with a "Switch to Console View" button

## Annotation-based validation

Implemented at the Model level to ensures that the data is always valid, regardless of whether it comes from the console, GUI, or database.

- Validation annotations defined in model classes
- Validation can be triggered in the controller (validation before calling model methods)
- Errors displayed in the view 

## Dependencies

- jakarta.validation-api — Provides standard validation annotations and interfaces as part of the Jakarta Bean Validation specification (e.g., @NotNull, @Size).

- hibernate-validator — Reference implementation of Jakarta Bean Validation that executes validation logic at runtime and supports custom constraints.

- expressly — Reference implementation of Jakarta Expression Language (EL) used for dynamic message interpolation in validation error messages.

