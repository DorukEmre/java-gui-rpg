package demre.rpg.controller;

import demre.rpg.model.GameEngine;

public class GameController {
  private final GameEngine gameEngine;

  public enum Direction {
    NORTH, SOUTH, EAST, WEST
  }

  public enum Action {
    FIGHT, RUN, KEEP, DROP
  }

  public GameController(GameEngine gameEngine) {
    this.gameEngine = gameEngine;
  }

  public void initialiseGame() {
    System.out.println("GameController initialised with engine: " + gameEngine);
  }

  // Receive user input and inform the game engine
  public void handleUserInput(String input) {
    System.out.println("GameController > Handling user input: " + input);

    try {
      Direction direction = Direction.valueOf(input.toUpperCase());
      System.out.println("Detected direction: " + direction);
      // Handle direction input
      gameEngine.movePlayer(direction);
      return;

    } catch (IllegalArgumentException e) {
      // Not a direction
    }

    try {
      Action action = Action.valueOf(input.toUpperCase());
      System.out.println("Detected action: " + action);
      // Handle action input
      gameEngine.doPlayerAction(action);
      return;

    } catch (IllegalArgumentException e) {
      // Not an action
    }

  }

}
