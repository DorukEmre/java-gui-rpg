package demre.rpg.view;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import demre.rpg.Main;
import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.GameEngineListener;
import demre.rpg.model.characters.Hero;
import demre.rpg.model.items.Item;
import demre.rpg.model.map.Tile;
import demre.rpg.view.console.AsciiArt;
import demre.rpg.view.console.ConsoleHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleView
    implements GameView, GameEngineListener {

  private static final Logger logger = LoggerFactory.getLogger(ConsoleView.class);

  private final GameEngine gameEngine;
  private final GameController controller;

  private final Scanner scanner = new Scanner(System.in);

  public ConsoleView(GameEngine gameEngine, GameController controller) {
    this.gameEngine = gameEngine;
    this.controller = controller;
    logger.info("ConsoleView initialised with engine: " + gameEngine + " and controller: " + controller);
    discardBufferedInput();
  }

  @Override
  public void onStepChanged(GameEngine.Step newStep) {

    try {
      switch (newStep) {
        case SPLASH_SCREEN -> splashScreen();
        case SELECT_HERO, INVALID_HERO_SELECTION -> selectHero();
        case CREATE_HERO, INVALID_HERO_CREATION -> createHero();
        case INFO, NEW_MISSION -> showHeroInfo();
        case PLAYING, INVALID_ACTION, ENEMY_FIGHT_SUCCESS, LEVEL_UP,
            ENEMY_RUN_SUCCESS ->
          showMap();
        case ENEMY_ENCOUNTER, ENEMY_INVALID_ACTION, ENEMY_RUN_FAILURE ->
          showMap();
        case ITEM_FOUND, ITEM_FOUND_AND_LEVEL_UP, ITEM_INVALID_ACTION ->
          showMap();
        case VICTORY_MISSION, VICTORY_INVALID_ACTION -> showVictoryScreen();
        case GAME_OVER, GAME_OVER_INVALID_ACTION -> showGameOver();
        case EXIT_GAME -> cleanup();
        default -> {
        }
      }

    } catch (Exception e) {
      Main.errorAndExit(e, e.getMessage());
    }
  }

  private void discardBufferedInput() {
    try {
      while (System.in.available() > 0) {
        System.in.read();
      }
    } catch (Exception ignored) {
    }
  }

  // Call helper and handle EOF
  private String readLine() {

    String line = ConsoleHelper.readLine(scanner);

    if (line == null) {
      logger.warn("ConsoleView > EOF on input. Exiting.");

      cleanup();
      Main.exit();
    }
    return line;
  }

  @Override
  public void splashScreen() {
    logger.info("ConsoleView > Displaying splash screen...");

    clearConsole();

    ConsoleHelper.println(AsciiArt.SPLASH);
    String input = readLine();

    controller.onSplashScreenContinue(input);
  }

  @Override
  public void selectHero() {
    logger.info("ConsoleView > Selecting hero...");

    clearConsole();

    // Display the list of heroes
    List<Hero> heroes = gameEngine.getHeroes();
    if (heroes == null || heroes.isEmpty()) {
      if (gameEngine.getStep() == GameEngine.Step.INVALID_HERO_SELECTION) {
        ConsoleHelper.println("Invalid hero selection. Please try again.");
      }
      ConsoleHelper.println(
          "No heroes available. Type 'new' [n] to create a new hero:\n");

    } else {
      for (int i = 0; i < heroes.size(); i++) {
        Hero hero = heroes.get(i);
        ConsoleHelper.println((i + 1) + ". " + hero.description() + "\n");
      }
      if (gameEngine.getStep() == GameEngine.Step.INVALID_HERO_SELECTION) {
        ConsoleHelper.println("Invalid hero selection. Please try again.");
      }
      ConsoleHelper.println(
          "Enter a number to select your hero from the list, or type 'new' [n] to create a new hero:\n");
    }

    String heroSelection = readLine();

    try {
      controller.onSelectHeroContinue(heroSelection);
    } catch (IOException e) {
      throw new RuntimeException(
          "IOException during select hero: " + e.getMessage(), e);
    }
  }

  @Override
  public void createHero() {
    logger.info("ConsoleView > Creating hero...");

    clearConsole();
    if (gameEngine.getStep() == GameEngine.Step.INVALID_HERO_CREATION) {
      ConsoleHelper.println("Invalid hero creation. Please try again.");
      ConsoleHelper.println(
          "Hero name must be 3-20 characters long and can only contain alphanumeric characters and spaces.\n");
    }
    ConsoleHelper.println("Enter your hero's name:");

    String heroName = readLine();

    ConsoleHelper.println("\nPick a class for your hero:");
    ConsoleHelper.println(
        "1. Warrior - A strong fighter, +10% experience gain");
    ConsoleHelper.println(
        "2. Rogue - A stealthy assassin, +10% chance to dodge attacks");
    ConsoleHelper.println(
        "3. Mage - A master of magic, +10% chance to find items");

    String heroClass = readLine();

    controller.onCreateHeroContinue(heroName, heroClass, false);
  }

  @Override
  public void showHeroInfo() {
    logger.info("ConsoleView > Showing hero information...");

    clearConsole();
    Hero hero = gameEngine.getHero();

    ConsoleHelper.println("Hero Information:\n");

    ConsoleHelper.println("Name:\t\t" + hero.getName());
    ConsoleHelper.println("Class:\t\t" + hero.getClass().getSimpleName());
    ConsoleHelper.println("Level:\t\t" + hero.getLevel());
    ConsoleHelper.println("Experience:\t" + hero.getExperience());
    ConsoleHelper.println("Attack:\t\t" + hero.getAttack());
    ConsoleHelper.println("Defense:\t" + hero.getDefense());
    ConsoleHelper.println("Hit Points:\t" + hero.getHitPoints());
    ConsoleHelper.println("Weapon:\t\t" + hero.getWeapon().getFormattedName());
    ConsoleHelper.println("Armor:\t\t" + hero.getArmor().getFormattedName());
    ConsoleHelper.println("Helm:\t\t" + hero.getHelm().getFormattedName());

    ConsoleHelper.println("\nPress Enter to continue...");

    String input = readLine();

    controller.onShowHeroInfoContinue(input);
  }

  @Override
  public void showMap() {
    logger.info("ConsoleView > Showing game map...");

    GameEngine.Step step = gameEngine.getStep();

    clearConsole();
    drawMap();
    controller.onMapDisplayed();

    if (step == GameEngine.Step.PLAYING
        || step == GameEngine.Step.INVALID_ACTION
        || step == GameEngine.Step.ENEMY_FIGHT_SUCCESS
        || step == GameEngine.Step.LEVEL_UP
        || step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
      showPlayerControl();

    } else if (step == GameEngine.Step.ENEMY_ENCOUNTER
        || step == GameEngine.Step.ENEMY_INVALID_ACTION
        || step == GameEngine.Step.ENEMY_RUN_FAILURE) {
      showEnemyEncounter();

    } else if (step == GameEngine.Step.ITEM_FOUND
        || step == GameEngine.Step.ITEM_FOUND_AND_LEVEL_UP
        || step == GameEngine.Step.ITEM_INVALID_ACTION) {
      showItemFound();
    }

  }

  private void showPlayerControl() {

    GameEngine.Step step = gameEngine.getStep();

    if (step == GameEngine.Step.INVALID_ACTION) {
      ConsoleHelper.println("Invalid action. Please try again.");
    } else if (step == GameEngine.Step.ENEMY_FIGHT_SUCCESS) {
      ConsoleHelper.println("You defeated the enemy!");
    } else if (step == GameEngine.Step.LEVEL_UP) {
      ConsoleHelper.println("You defeated the enemy and leveled up!");
      ConsoleHelper.println("You are now level "
          + gameEngine.getHero().getLevel() + " with "
          + gameEngine.getHero().getExperience() + " experience points.");
    } else if (step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
      ConsoleHelper.println("You successfully ran away from the enemy!");
    }
    ConsoleHelper.println(
        "(N)orth, (S)outh, (E)ast, (W)est, (i)nfo or 'exit'.");

    String input = readLine();

    try {
      controller.onMapInputContinue(input);
    } catch (IOException e) {
      throw new RuntimeException(
          "IOException during map input: " + e.getMessage(), e);
    }

  }

  public void showEnemyEncounter() {

    GameEngine.Step step = gameEngine.getStep();

    if (step == GameEngine.Step.ENEMY_INVALID_ACTION) {
      ConsoleHelper.println("Invalid action. Please try again.");
    }

    if (step == GameEngine.Step.ENEMY_ENCOUNTER
        || step == GameEngine.Step.ENEMY_INVALID_ACTION) {
      ConsoleHelper.println("You encounter an enemy!");
      ConsoleHelper.println("(f)ight or (r)un");

      String input = readLine();
      if (input == null)
        return;
      controller.onEnemyEncounterContinue(input, false);

    } else if (step == GameEngine.Step.ENEMY_RUN_FAILURE) {
      ConsoleHelper.println("You failed to run away from the enemy!");
      ConsoleHelper.println("You have to fight. Press Enter.");

      String input = readLine();
      if (input == null)
        return;
      controller.onEnemyEncounterContinue(input, true);
    }

  }

  public void showItemFound() {

    GameEngine.Step step = gameEngine.getStep();
    Hero hero = gameEngine.getHero();
    Item.Type foundItemType = gameEngine.getItemFound().getType();

    if (step == GameEngine.Step.ITEM_INVALID_ACTION) {
      ConsoleHelper.println("Invalid action. Please try again.");

    } else if (step == GameEngine.Step.ITEM_FOUND_AND_LEVEL_UP) {
      ConsoleHelper.println("You defeated the enemy and leveled up!");
      ConsoleHelper.println(
          "You are now level " + hero.getLevel() + " with "
              + hero.getExperience() + " experience points.");

    } else if (step == GameEngine.Step.ITEM_FOUND) {
      ConsoleHelper.println("You defeated the enemy!");
    }

    ConsoleHelper.println("You found an item. " + gameEngine.getItemFound());

    if (foundItemType == Item.Type.WEAPON) {
      ConsoleHelper.println("Your current weapon is: "
          + hero.getWeapon().getFormattedName());
    } else if (foundItemType == Item.Type.ARMOR) {
      ConsoleHelper.println("Your current armor is: "
          + hero.getArmor().getFormattedName());
    } else if (foundItemType == Item.Type.HELM) {
      ConsoleHelper.println("Your current helm is: "
          + hero.getHelm().getFormattedName());
    }
    ConsoleHelper.println("(k)eep or (l)eave");

    String item_choice = readLine();

    controller.onItemFoundContinue(item_choice);
  }

  @Override
  public void showVictoryScreen() {

    clearConsole();
    ConsoleHelper.println(AsciiArt.VICTORY);
    ConsoleHelper.println();
    if (gameEngine.getStep() == GameEngine.Step.VICTORY_INVALID_ACTION) {
      ConsoleHelper.println("Invalid action. Please try again.");
    }
    ConsoleHelper.println("(n)ext mission or (e)xit game");

    String choice = readLine();

    controller.onVictoryScreenContinue(choice);
  }

  @Override
  public void showGameOver() {

    clearConsole();
    ConsoleHelper.println(AsciiArt.DIED);
    ConsoleHelper.println();
    if (gameEngine.getStep() == GameEngine.Step.GAME_OVER_INVALID_ACTION) {
      ConsoleHelper.println("Invalid action. Please try again.");
    }
    ConsoleHelper.println("(t)ry again or (e)xit game");

    String choice = readLine();

    controller.onGameOverContinue(choice);
  }

  //

  private static void clearConsole() {
    ConsoleHelper.print("\033[H\033[2J");
    ConsoleHelper.flush();
  }

  private void drawMap() {
    // Level: 1, Map Size: 9x9
    // Level: 2, Map Size: 15x15
    // Level: 3, Map Size: 19x19
    // Level: 4, Map Size: 25x25
    // Level: 5, Map Size: 29x29
    // Level: 6, Map Size: 35x35
    // Level: 7, Map Size: 39x39
    // Level: 8, Map Size: 45x45
    // Level: 9, Map Size: 49x49

    int side = gameEngine.getMapSize();
    Tile[][] map = gameEngine.getMap();

    ConsoleHelper.println(gameEngine.getHero().description());

    for (int i = 0; i < side + 2; i++) {
      for (int j = 0; j < side + 2; j++) {
        if (map[i][j].isVisible()) {
          ConsoleHelper.print(map[i][j].getSymbol() + " ");
        } else {
          ConsoleHelper.print(" " + " ");
        }
      }
      ConsoleHelper.println();
    }
    ConsoleHelper.println();
  }

  @Override
  public void cleanup() {
    logger.info("ConsoleView > Cleaning up resources...");

    scanner.close();
  }

}
