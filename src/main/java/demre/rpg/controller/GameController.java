package demre.rpg.controller;

import java.io.IOException;

import demre.rpg.model.GameEngine;
import demre.rpg.model.GameEngine.Step;

public class GameController {
  private final GameEngine gameEngine;

  // Constructor

  public GameController(GameEngine gameEngine) {
    this.gameEngine = gameEngine;
  }

  // Methods

  public void onSplashScreenContinue(String input) {
    System.out.println("GameController > Splash screen continue pressed.");

    if (handleExitOrViewChange(input, false))
      return;

    gameEngine.setCurrentStep(Step.SELECT_HERO);
  }

  public void onSelectHeroContinue(String input) {
    System.out.println("GameController > Hero selection input: " + input);

    if (handleExitOrViewChange(input, false))
      return;

    if (gameEngine.isValidHeroSelection(input)) {
      gameEngine.selectHero(input);
      gameEngine.newMission("start");
      gameEngine.setCurrentStep(Step.NEW_MISSION);
    } else if (input.equalsIgnoreCase("new")
        || input.equalsIgnoreCase("n")) {
      gameEngine.setCurrentStep(Step.CREATE_HERO);
    } else {
      gameEngine.setCurrentStep(Step.INVALID_HERO_SELECTION);
    }
  }

  public void onCreateHeroContinue(String heroName, String heroClass, Boolean goBack) {
    System.out.println("GameController > Hero creation input: " + heroName);

    if (handleExitOrViewChange(heroClass, false))
      return;

    if (goBack || heroClass.equalsIgnoreCase("back")) {
      gameEngine.setCurrentStep(Step.SELECT_HERO);
      return;
    }
    if (gameEngine.isValidHeroName(heroName)
        && gameEngine.isValidHeroClass(heroClass)) {
      gameEngine.createHero(heroName, heroClass);
      gameEngine.newMission("start");
      gameEngine.setCurrentStep(Step.NEW_MISSION);
    } else {
      gameEngine.setCurrentStep(Step.INVALID_HERO_CREATION);
    }

  }

  public void onShowHeroInfoContinue(String input) {
    System.out.println("GameController > Info continue pressed.");

    if (handleExitOrViewChange(input, false))
      return;

    gameEngine.setCurrentStep(Step.PLAYING);
  }

  public void onMapInputContinue(String input)
      throws IOException {
    System.out.println("GameController > Player input: " + input);

    if (handleExitOrViewChange(input, false))
      return;

    if (input.equalsIgnoreCase("info")
        || input.equalsIgnoreCase("i")) {
      gameEngine.setCurrentStep(Step.INFO);
    }
    // If input direction valid, move player
    else if (gameEngine.isValidDirection(input)) {
      gameEngine.movePlayer(input);
    } else {
      gameEngine.setCurrentStep(Step.INVALID_ACTION);
    }
  }

  public void onEnemyEncounterContinue(String input) {
    System.out.println("GameController > Enemy encounter input: " + input);

    if (handleExitOrViewChange(input, false))
      return;

    if (input.equalsIgnoreCase("fight") || input.equalsIgnoreCase("f")) {
      gameEngine.fightEnemy();
    } else if (input.equalsIgnoreCase("run") || input.equalsIgnoreCase("r")) {
      gameEngine.runFromEnemy();
    } else {
      gameEngine.setCurrentStep(Step.ENEMY_INVALID_ACTION);
    }
  }

  public void onItemFoundContinue(String input) {
    System.out.println("GameController > Item found continue pressed.");

    if (handleExitOrViewChange(input, false))
      return;

    if (input.equalsIgnoreCase("keep") || input.equalsIgnoreCase("k")) {
      gameEngine.keepItem();
      gameEngine.setCurrentStep(Step.PLAYING);
    } else if (input.equalsIgnoreCase("leave")
        || input.equalsIgnoreCase("l")) {
      gameEngine.setCurrentStep(Step.PLAYING);
    } else {
      gameEngine.setCurrentStep(Step.ITEM_INVALID_ACTION);
    }
  }

  public void onVictoryScreenContinue(String input) {
    System.out.println("GameController > Victory screen continue pressed.");

    if (handleExitOrViewChange(input, true))
      return;

    if (input.equalsIgnoreCase("next")
        || input.equalsIgnoreCase("n")) {
      gameEngine.newMission("continue");
      gameEngine.setCurrentStep(Step.NEW_MISSION);
    } else {
      gameEngine.setCurrentStep(Step.VICTORY_INVALID_ACTION);
    }
  }

  public void onGameOverContinue(String input) {
    System.out.println("GameController > Game over continue pressed.");

    if (handleExitOrViewChange(input, true))
      return;

    if (input.equalsIgnoreCase("try")
        || input.equalsIgnoreCase("t")) {
      gameEngine.newMission("reset");
      gameEngine.setCurrentStep(Step.NEW_MISSION);
    } else {
      gameEngine.setCurrentStep(Step.GAME_OVER_INVALID_ACTION);
    }
  }

  private Boolean handleExitOrViewChange(String input, boolean allowShortExit) {
    if (input.equalsIgnoreCase("exit")
        || (allowShortExit && input.equalsIgnoreCase("e"))) {
      gameEngine.exitGame();
      return true;
    } else if (input.equalsIgnoreCase("gui")) {
      switchView("gui");
      return true;
    }
    return false;
  }

  public void switchView(String newGameView) {
    System.out.println("GameController > Switching to view: " + newGameView);

    gameEngine.setGameView(newGameView);
  }
}
