package demre.rpg.controller;

import java.io.IOException;

import demre.rpg.model.GameEngine;
import demre.rpg.model.GameEngine.Special;

public class GameController {
  private final GameEngine gameEngine;

  // Constructor

  public GameController(GameEngine gameEngine) {
    this.gameEngine = gameEngine;
  }

  // Methods

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
      gameEngine.newMission("start");
      gameEngine.setCurrentStep(GameEngine.Step.NEW_MISSION);
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
      gameEngine.newMission("start");
      gameEngine.setCurrentStep(GameEngine.Step.NEW_MISSION);
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

  public void onMapInputContinue(String input)
      throws IOException {
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
    String encounter;
    if (choice.equalsIgnoreCase("fight") || choice.equalsIgnoreCase("f")) {
      encounter = gameEngine.fightEnemy();
      if (encounter.equals("victory")) {
        gameEngine.setCurrentStep(GameEngine.Step.ENEMY_FIGHT_SUCCESS);
      } else if (encounter.equals("item found and level up")) {
        gameEngine.setCurrentStep(GameEngine.Step.ITEM_FOUND_AND_LEVEL_UP);
      } else if (encounter.equals("level up")) {
        gameEngine.setCurrentStep(GameEngine.Step.LEVEL_UP);
      } else if (encounter.equals("item found")) {
        gameEngine.setCurrentStep(GameEngine.Step.ITEM_FOUND);
      } else {
        gameEngine.setCurrentStep(GameEngine.Step.GAME_OVER);
      }
    } else if (choice.equalsIgnoreCase("run") || choice.equalsIgnoreCase("r")) {
      encounter = gameEngine.runFromEnemy();
      if (encounter.equals("success")) {
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

  public void onItemFoundContinue(String choice) {
    System.out.println("GameController > Item found continue pressed.");
    if (choice.equalsIgnoreCase("keep") || choice.equalsIgnoreCase("k")) {
      gameEngine.keepItem();
      gameEngine.setCurrentStep(GameEngine.Step.PLAYING);
    } else if (choice.equalsIgnoreCase("leave")
        || choice.equalsIgnoreCase("l")) {
      gameEngine.setCurrentStep(GameEngine.Step.PLAYING);
    } else if (choice.equalsIgnoreCase("exit")) {
      gameEngine.setCurrentStep(GameEngine.Step.EXIT_GAME);
    } else {
      gameEngine.setCurrentStep(GameEngine.Step.ITEM_INVALID_ACTION);
    }
  }

  public void onVictoryScreenContinue(String choice) {
    System.out.println("GameController > Victory screen continue pressed.");
    if (choice.equalsIgnoreCase("next")
        || choice.equalsIgnoreCase("n")) {
      gameEngine.newMission("continue");
      gameEngine.setCurrentStep(GameEngine.Step.NEW_MISSION);
    } else if (choice.equalsIgnoreCase("exit")
        || choice.equalsIgnoreCase("e")) {
      gameEngine.setCurrentStep(GameEngine.Step.EXIT_GAME);
    } else {
      gameEngine.setCurrentStep(GameEngine.Step.VICTORY_INVALID_ACTION);
    }
  }

  public void onGameOverContinue(String choice) {
    System.out.println("GameController > Game over continue pressed.");
    if (choice.equalsIgnoreCase("try")
        || choice.equalsIgnoreCase("t")) {
      gameEngine.newMission("reset");
      gameEngine.setCurrentStep(GameEngine.Step.NEW_MISSION);
    } else if (choice.equalsIgnoreCase("exit")
        || choice.equalsIgnoreCase("e")) {
      gameEngine.setCurrentStep(GameEngine.Step.EXIT_GAME);
    } else {
      gameEngine.setCurrentStep(GameEngine.Step.GAME_OVER_INVALID_ACTION);
    }
  }
}
