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

  public void onSplashScreenContinue() {
    System.out.println("GameController > Splash screen continue pressed.");
    gameEngine.setCurrentStep(GameEngine.Step.SELECT_HERO);
  }

  public void onSelectHeroContinue(String heroSelection) {
    System.out.println("GameController > Hero selection input: " + heroSelection);
    // Logic to handle hero selection
    // if heroSelection = num that exists in the list, then set the hero
    if (gameEngine.isValidHeroSelection(heroSelection)) {
      gameEngine.setCurrentStep(GameEngine.Step.PLAYING);
    }
    // heroSelection is 'new' or 'create', set the step to CREATE_HERO
    else if (heroSelection.equalsIgnoreCase("new")) {
      gameEngine.setCurrentStep(GameEngine.Step.CREATE_HERO);
    } else {
      // heroSelection is invalid, set the step to INVALID_HERO
      gameEngine.setCurrentStep(GameEngine.Step.INVALID_HERO_SELECTION);
    }
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
