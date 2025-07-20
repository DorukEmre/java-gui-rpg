package demre.rpg.controller;

import demre.rpg.model.GameEngine;
import demre.rpg.model.GameEngine.Special;

public class GameController {
  private final GameEngine gameEngine;

  // Constructor

  public GameController(GameEngine gameEngine) {
    this.gameEngine = gameEngine;
  }

  // Methods

  public void initialiseGame() {
    System.out.println("GameController initialised with engine: " + gameEngine);
  }

  public void onSplashScreenContinue() {
    System.out.println("GameController > Splash screen continue pressed.");
    gameEngine.setCurrentStep(GameEngine.Step.SELECT_HERO);
  }

  public void onSelectHeroContinue(String heroSelection) {
    System.out.println("GameController > Hero selection input: " + heroSelection);
    if (heroSelection.equalsIgnoreCase("exit")) {
      gameEngine.setCurrentStep(GameEngine.Step.EXIT_GAME);
      return;
    }

    // if heroSelection = num that exists in the list, then set the hero
    if (gameEngine.isValidHeroSelection(heroSelection)) {
      gameEngine.selectHero(heroSelection);
      gameEngine.setCurrentStep(GameEngine.Step.INFO);
    }
    // heroSelection is 'new', set the step to CREATE_HERO
    else if (heroSelection.equalsIgnoreCase("new")
        || heroSelection.equalsIgnoreCase("n")) {
      gameEngine.setCurrentStep(GameEngine.Step.CREATE_HERO);
    } else {
      // heroSelection is invalid, set the step to INVALID_HERO_SELECTION
      gameEngine.setCurrentStep(GameEngine.Step.INVALID_HERO_SELECTION);
    }
  }

  public void onCreateHeroContinue(String heroName, String heroClass) {
    System.out.println("GameController > Hero creation input: " + heroName);

    if (heroClass.equalsIgnoreCase("exit")) {
      gameEngine.setCurrentStep(GameEngine.Step.EXIT_GAME);
      return;
    }

    if (gameEngine.isValidHeroName(heroName) && gameEngine.isValidHeroClass(heroClass)) {
      gameEngine.createHero(heroName, heroClass);
      gameEngine.setCurrentStep(GameEngine.Step.INFO);
      System.out.println(
          "onCreateHeroContinue > Hero " + heroClass + " '" + heroName + "' created successfully!");
    } else {
      // heroName is invalid, set the step to INVALID_HERO_CREATION
      gameEngine.setCurrentStep(GameEngine.Step.INVALID_HERO_CREATION);
    }

  }

  public void onShowHeroContinue() {
    System.out.println("GameController > Info continue pressed.");
    gameEngine.setCurrentStep(GameEngine.Step.PLAYING);
  }

  public void onMapInputContinue(String input) {
    System.out.println("GameController > Player input: " + input);

    if (input.equalsIgnoreCase("exit")) {
      gameEngine.setCurrentStep(GameEngine.Step.EXIT_GAME);
      return;
    } else if (input.equalsIgnoreCase("info")) {
      gameEngine.setCurrentStep(GameEngine.Step.INFO);
      return;
    } else if (gameEngine.isValidDirection(input)) {
      gameEngine.movePlayer(input);

      Special event = gameEngine.getEvent();
      System.out.println("GameController > Event after move: " + event);
      if (event == Special.ENEMY) {
        gameEngine.setCurrentStep(GameEngine.Step.ENEMY_ENCOUNTER);
      } else if (event == Special.VICTORY) {
        gameEngine.setCurrentStep(GameEngine.Step.VICTORY);
      }
    } else {
      gameEngine.setCurrentStep(GameEngine.Step.INVALID_ACTION);
    }
  }

  public void onInvalidActionContinue() {
    gameEngine.setCurrentStep(GameEngine.Step.PLAYING);
  }

  public void onSuccessfulActionContinue() {
    gameEngine.setCurrentStep(GameEngine.Step.PLAYING);
  }

  public void onEnemyEncounterContinue(String choice) {
    System.out.println("GameController > Enemy encounter choice: " + choice);
    Boolean success;
    if (choice.equalsIgnoreCase("fight") || choice.equalsIgnoreCase("f")) {
      success = gameEngine.fightEnemy();
      if (success) {
        gameEngine.setCurrentStep(GameEngine.Step.ENEMY_FIGHT_SUCCESS);
      } else {
        gameEngine.setCurrentStep(GameEngine.Step.GAME_OVER);
      }
    } else if (choice.equalsIgnoreCase("run") || choice.equalsIgnoreCase("r")) {
      success = gameEngine.runFromEnemy();
      if (success) {
        gameEngine.setCurrentStep(GameEngine.Step.ENEMY_RUN_SUCCESS);
      } else {
        gameEngine.setCurrentStep(GameEngine.Step.ENEMY_RUN_FAILURE);
      }
    } else if (choice.equalsIgnoreCase("exit")) {
      gameEngine.setCurrentStep(GameEngine.Step.EXIT_GAME);
    } else {
      gameEngine.setCurrentStep(GameEngine.Step.ENEMY_INVALID_ACTION);
    }
  }

  public void onItemFoundContinue() {
    System.out.println("GameController > Item found continue pressed.");
    gameEngine.setCurrentStep(GameEngine.Step.PLAYING);
  }

  public void onVictoryScreenContinue() {
    System.out.println("GameController > Victory screen continue pressed.");
    gameEngine.setCurrentStep(GameEngine.Step.EXIT_GAME);
  }

  public void onGameOverContinue() {
    System.out.println("GameController > Game over continue pressed.");
    gameEngine.setCurrentStep(GameEngine.Step.EXIT_GAME);
  }
}
