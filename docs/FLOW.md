## Startup

Main creates:
  - model: GameEngine
  - controller: GameController
  - view: either GUIView or ConsoleView

Main calls gameEngine.initialise() to load heroes (via db).


## Observer-based notification loop (model → view)

GameEngine is the subject and GUIView/ConsoleView are listeners (concrete observers) following the Observer pattern.
There is only ever one GameEngineListener which is the active view.

- GameEngine keeps a list of GameEngineListener (addListener/removeListener).
- When the model state changes it iterates the registered listeners and calls listener.onStepChanged(newStep).
- Each view (ConsoleView or GUIView) implements GameEngineListener.onStepChanged and renders the appropriate UI by inspecting the new step.


## View renders and gathers input View → Controller (user intent)

Console: ConsoleView prints, reads from Scanner/ConsoleHelper and forwards choices to controller methods (e.g., onSelectHeroContinue, onMapInputContinue, onEnemyEncounterContinue).

GUI: GUIView constructs Swing panels. Map interaction uses MapViewPanel and ButtonsMap. GUI buttons call controller handlers (via action listeners in panel classes like PlayerControlPanel).


## Controller processes input and updates model (controller → model)

All user actions are forwarded to GameController methods.

The controller:
  - Handles global commands (exit / switch to GUI) via handleExitOrViewChange.
  - Validates and delegates domain actions to the model (e.g., gameEngine.selectHero(...), gameEngine.createHero(...), gameEngine.movePlayer(...), gameEngine.fightEnemy(), gameEngine.runFromEnemy()).
  - Sets model steps where appropriate (calls on GameEngine methods which will themselves set steps).


## GameEngine executes game logic and triggers state changes (model core)

The model executes game logic (map generation, villains, fight resolution, item drops, etc.).
After each logical outcome the engine sets a new step via setCurrentStep (PLAYING, ENEMY_ENCOUNTER, ITEM_FOUND, VICTORY_MISSION, GAME_OVER).


## View updates again via listener notifications

Each time GameEngine calls setCurrentStep, the active view gets onStepChanged and re-renders or prompts for user input, continuing the loop.


## View switching

Controller can request a view change (switchView in GameController) which calls GameEngine.setGameView — this disposes the old GUI, creates the new view, registers it, and immediately notifies it of the current step.


## Exit

Exiting sets EXIT_GAME, saves state (saveHeroToDatabase) and triggers view cleanup.