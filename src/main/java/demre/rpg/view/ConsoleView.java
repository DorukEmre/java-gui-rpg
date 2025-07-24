package demre.rpg.view;

import java.util.List;
import java.util.Scanner;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.GameEngineListener;
import demre.rpg.model.characters.Hero;
import demre.rpg.model.map.Tile;
import demre.rpg.view.console.AsciiArt;

public class ConsoleView
    implements GameView, GameEngineListener {

  private final GameEngine gameEngine;
  private final GameController controller;

  private final Scanner scanner = new Scanner(System.in);

  public ConsoleView(GameEngine gameEngine, GameController controller) {
    this.gameEngine = gameEngine;
    this.controller = controller;
    System.out.println("ConsoleView initialised with engine: " + gameEngine + " and controller: " + controller);
    discardBufferedInput();
  }

  @Override
  public void onStepChanged(GameEngine.Step newStep) {
    switch (newStep) {
      case SPLASH_SCREEN -> splashScreen();
      case SELECT_HERO, INVALID_HERO_SELECTION -> selectHero();
      case CREATE_HERO, INVALID_HERO_CREATION -> createHero();
      case INFO, NEW_MISSION -> showHeroInfo();
      case PLAYING, INVALID_ACTION, ENEMY_FIGHT_SUCCESS, LEVEL_UP, ENEMY_RUN_SUCCESS -> showMap();
      case ENEMY_ENCOUNTER, ENEMY_INVALID_ACTION -> showEnemyEncounter();
      case ENEMY_RUN_FAILURE -> showEnemyRunFailure();
      case ITEM_FOUND, ITEM_FOUND_AND_LEVEL_UP, ITEM_INVALID_ACTION -> showItemFound();
      case VICTORY_MISSION, VICTORY_INVALID_ACTION -> showVictoryScreen();
      case GAME_OVER, GAME_OVER_INVALID_ACTION -> showGameOver();
      case EXIT_GAME -> cleanup();
      default -> {
      }
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

  @Override
  public void splashScreen() {
    System.out.println("ConsoleView > Displaying splash screen...");
    try {
      clearConsole();
      System.out.println(AsciiArt.SPLASH);
      String input = scanner.nextLine();
      controller.onSplashScreenContinue(input);
    } catch (Exception e) {
      throw new RuntimeException(
          "Error during splash screen: " + e.getMessage());
    }
  }

  @Override
  public void selectHero() {
    System.out.println("ConsoleView > Selecting hero...");
    try {
      clearConsole();

      // Display the list of heroes
      List<Hero> heroes = gameEngine.getHeroes();
      if (heroes == null || heroes.isEmpty()) {
        if (gameEngine.getStep() == GameEngine.Step.INVALID_HERO_SELECTION) {
          System.out.println("Invalid hero selection. Please try again.");
        }
        System.out.println(
            "No heroes available. Type 'new' [n] to create a new hero:\n");

      } else {
        for (int i = 0; i < heroes.size(); i++) {
          Hero hero = heroes.get(i);
          System.out.println((i + 1) + ". " + hero.description() + "\n");
        }
        if (gameEngine.getStep() == GameEngine.Step.INVALID_HERO_SELECTION) {
          System.out.println("Invalid hero selection. Please try again.");
        }
        System.out
            .println("Enter a number to select your hero from the list, or type 'new' [n] to create a new hero:\n");
      }

      String heroSelection = scanner.nextLine();
      controller.onSelectHeroContinue(heroSelection);

    } catch (Exception e) {
      throw new RuntimeException(
          "Error during select hero: " + e.getMessage(), e);
    }
  }

  @Override
  public void createHero() {
    System.out.println("ConsoleView > Creating hero...");
    try {
      clearConsole();
      if (gameEngine.getStep() == GameEngine.Step.INVALID_HERO_CREATION) {
        System.out.println("Invalid hero creation. Please try again.");
        System.out.println(
            "Hero name must be 3-20 characters long and can only contain alphanumeric characters and spaces.\n");
      }
      System.out.println("Enter your hero's name:");
      String heroName = scanner.nextLine();

      System.out.println("\nPick a class for your hero:");
      System.out.println(
          "1. Warrior - A strong fighter, +10% experience gain");
      System.out.println(
          "2. Rogue - A stealthy assassin, +10% chance to dodge attacks");
      System.out.println(
          "3. Mage - A master of magic, +10% chance to find items");
      String heroClass = scanner.nextLine();

      controller.onCreateHeroContinue(heroName, heroClass);

    } catch (Exception e) {
      throw new RuntimeException(
          "Error during create hero: " + e.getMessage());
    }
  }

  @Override
  public void showHeroInfo() {
    System.out.println("ConsoleView > Showing hero information...");
    try {
      clearConsole();
      Hero hero = gameEngine.getHero();

      System.out.println("Hero Information:\n");

      System.out.println("Name:\t\t" + hero.getName());
      System.out.println("Class:\t\t" + hero.getClass().getSimpleName());
      System.out.println("Level:\t\t" + hero.getLevel());
      System.out.println("Experience:\t" + hero.getExperience());
      System.out.println("Attack:\t\t" + hero.getAttack());
      System.out.println("Defense:\t" + hero.getDefense());
      System.out.println("Hit Points:\t" + hero.getHitPoints());
      System.out.println("Weapon:\t\t" + hero.getWeapon().getName() +
          " +" + hero.getWeapon().getModifier());
      System.out.println("Armor:\t\t" + hero.getArmor().getName() +
          " +" + hero.getArmor().getModifier());
      System.out.println("Helm:\t\t" + hero.getHelm().getName() +
          " +" + hero.getHelm().getModifier());

      System.out.println("\nPress Enter to continue...");
      String input = scanner.nextLine();
      controller.onShowHeroInfoContinue(input);

    } catch (Exception e) {
      throw new RuntimeException(
          "Error during show hero: " + e.getMessage());
    }
  }

  @Override
  public void showMap() {
    System.out.println("ConsoleView > Showing game map...");

    GameEngine.Step step = gameEngine.getStep();

    try {
      clearConsole();
      drawMap();
      if (step == GameEngine.Step.INVALID_ACTION) {
        System.out.println("Invalid action. Please try again.");
      } else if (step == GameEngine.Step.ENEMY_FIGHT_SUCCESS) {
        System.out.println("You defeated the enemy!");
      } else if (step == GameEngine.Step.LEVEL_UP) {
        System.out.println("You defeated the enemy and leveled up!");
        System.out.println("You are now level "
            + gameEngine.getHero().getLevel() + " with "
            + gameEngine.getHero().getExperience() + " experience points.");
      } else if (step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
        System.out.println("You successfully ran away from the enemy!");
      }
      System.out.println(
          "(N)orth, (S)outh, (E)ast, (W)est, (i)nfo or 'exit'.");
      String input = scanner.nextLine();
      controller.onMapInputContinue(input);

    } catch (Exception e) {
      throw new RuntimeException(
          "Error during console drawing: " + e.getMessage());

    }
  }

  private static void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
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

    System.out.println("Map Size: " + side + "x" + side);
    System.out.println(gameEngine.getHero().description());

    for (int i = 0; i < side + 2; i++) {
      for (int j = 0; j < side + 2; j++) {
        if (map[i][j].isVisible()) {
          System.out.print(map[i][j].getSymbol() + " ");
        } else {
          System.out.print(" " + " ");
        }
      }
      System.out.println();
    }
    System.out.println();
  }

  @Override
  public void showEnemyEncounter() {
    System.out.println("ConsoleView > Enemy encounter screen...");
    try {
      clearConsole();
      drawMap();
      System.out.println("You encounter an enemy!");
      System.out.println("(f)ight or (r)un");
      String enemy_choice = scanner.nextLine();
      controller.onEnemyEncounterContinue(enemy_choice);
    } catch (Exception e) {
      throw new RuntimeException(
          "Error during enemy encounter: " + e.getMessage());
    }
  }

  @Override
  public void showEnemyRunFailure() {
    System.out.println("ConsoleView > Enemy run failure screen...");
    try {
      clearConsole();
      drawMap();
      System.out.println("You failed to run away from the enemy!");
      System.out.println("You have to fight. Press Enter.");
      scanner.nextLine();
      controller.onEnemyEncounterContinue("fight");
    } catch (Exception e) {
      throw new RuntimeException(
          "Error during enemy run failure: " + e.getMessage());
    }
  }

  @Override
  public void showItemFound() {
    System.out.println("ConsoleView > Item found screen...");

    GameEngine.Step step = gameEngine.getStep();

    try {
      clearConsole();
      drawMap();
      if (step == GameEngine.Step.ITEM_INVALID_ACTION) {
        System.out.println("Invalid action. Please try again.");
      } else if (step == GameEngine.Step.ITEM_FOUND_AND_LEVEL_UP) {
        System.out.println("You defeated the enemy and leveled up!");
        System.out.println("You are now level "
            + gameEngine.getHero().getLevel() + " with "
            + gameEngine.getHero().getExperience() + " experience points.");
      } else if (step == GameEngine.Step.ITEM_FOUND) {
        System.out.println("You defeated the enemy!");
      }
      System.out.println("You found an item. " + gameEngine.getItemFound());
      System.out.println("Your current items are: "
          + gameEngine.getHero().getWeapon() + ", "
          + gameEngine.getHero().getArmor() + ", "
          + gameEngine.getHero().getHelm());
      System.out.println("(k)eep or (l)eave");
      String item_choice = scanner.nextLine();
      controller.onItemFoundContinue(item_choice);
    } catch (Exception e) {
      throw new RuntimeException(
          "Error during item found: " + e.getMessage());
    }
  }

  @Override
  public void showVictoryScreen() {
    try {
      clearConsole();
      System.out.println(AsciiArt.VICTORY);
      System.out.println();
      if (gameEngine.getStep() == GameEngine.Step.VICTORY_INVALID_ACTION) {
        System.out.println("Invalid action. Please try again.");
      }
      System.out.println("(n)ext mission or (e)xit game");
      String choice = scanner.nextLine();
      controller.onVictoryScreenContinue(choice);
    } catch (Exception e) {
      throw new RuntimeException(
          "Error during victory screen: " + e.getMessage());
    }
  }

  @Override
  public void showGameOver() {
    try {
      clearConsole();
      System.out.println(AsciiArt.DIED);
      System.out.println();
      if (gameEngine.getStep() == GameEngine.Step.GAME_OVER_INVALID_ACTION) {
        System.out.println("Invalid action. Please try again.");
      }
      System.out.println("(t)ry again or (e)xit game");
      String choice = scanner.nextLine();
      controller.onGameOverContinue(choice);
    } catch (Exception e) {
      throw new RuntimeException(
          "Error during game over screen: " + e.getMessage());
    }
  }

  @Override
  public void cleanup() {
    System.out.println("ConsoleView > Cleaning up resources...");
    try {
      scanner.close();
    } catch (Exception e) {
      throw new RuntimeException(
          "Error during cleanup: " + e.getMessage());
    }
  }

}
