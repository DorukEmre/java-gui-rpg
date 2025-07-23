package demre.rpg.view;

import java.util.List;
import java.util.Scanner;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;
import demre.rpg.model.map.Tile;

public class ConsoleView extends GameView {
  private final Scanner scanner = new Scanner(System.in);

  public ConsoleView(GameEngine gameEngine, GameController controller) {
    super(gameEngine, controller);
    System.out.println("ConsoleView initialised with engine: " + gameEngine + " and controller: " + controller);
    initialiseConsoleComponents();
  }

  private void initialiseConsoleComponents() {
    // Logic to Initialise console components
    System.out.println("ConsoleView > Initialising console components...");
    // e.g., set up input/output streams, etc.
  }

  @Override
  public void splashScreen() {
    System.out.println("ConsoleView > Displaying splash screen...");
    try {
      clearConsole();
      System.out.println(AsciiArt.SPLASH);
      scanner.nextLine();
      controller.onSplashScreenContinue();
    } catch (Exception e) {
      System.err.println("Error during splash screen: " + e.getMessage());
    }
  }

  @Override
  public void selectHero() {
    System.out.println("ConsoleView > Selecting hero...");
    try {
      clearConsole();

      // Display the list of heroes
      List<Hero> heroes = gameEngine.getHeroes();
      if (heroes.isEmpty()) {
        if (gameEngine.getStep() == GameEngine.Step.INVALID_HERO_SELECTION) {
          System.out.println("Invalid hero selection. Please try again.");
        }
        System.out.println(
            "No heroes available. Type 'new' [n] to create a new hero:\n");

      } else {
        for (int i = 0; i < heroes.size(); i++) {
          Hero hero = heroes.get(i);
          System.out.println((i + 1) + ". " + hero.getName() + " (" + hero.getClass().getSimpleName() + ")");
        }
        System.out.println();
        if (gameEngine.getStep() == GameEngine.Step.INVALID_HERO_SELECTION) {
          System.out.println("Invalid hero selection. Please try again.");
        }
        System.out
            .println("Enter a number to select your hero from the list, or type 'new' [n] to create a new hero:\n");
      }

      String heroSelection = scanner.nextLine();
      controller.onSelectHeroContinue(heroSelection);

    } catch (Exception e) {
      System.err.println("Error during select hero: " + e.getMessage());
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
            "Hero name must be 3-20 characters long and can only contain alphanumeric characters and spaces.");
      }
      System.out.println("Enter your hero's name:");
      String heroName = scanner.nextLine();
      System.out.println("Pick a class for your hero: (m)age, (w)arrior, (r)ogue");
      String heroClass = scanner.nextLine();
      controller.onCreateHeroContinue(heroName, heroClass);
    } catch (Exception e) {
      System.err.println("Error during create hero: " + e.getMessage());
    }
  }

  @Override
  public void showHero() {
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
      scanner.nextLine();
      controller.onShowHeroContinue();
    } catch (Exception e) {
      System.err.println("Error during show hero: " + e.getMessage());
    }
  }

  @Override
  public void updateView() {
    System.out.println("ConsoleView > Updating console view...");

    GameEngine.Step step = gameEngine.getStep();

    try {
      clearConsole();
      drawMap();
      if (step == GameEngine.Step.INVALID_ACTION) {
        System.out.println("Invalid action. Please try again.");
        controller.onInvalidActionContinue();
      } else if (step == GameEngine.Step.ENEMY_FIGHT_SUCCESS) {
        System.out.println("You defeated the enemy!");
        controller.onSuccessfulActionContinue();
      } else if (step == GameEngine.Step.LEVEL_UP) {
        System.out.println("You defeated the enemy and leveled up!");
        System.out.println("You are now level "
            + gameEngine.getHero().getLevel() + " with "
            + gameEngine.getHero().getExperience() + " experience points.");
        controller.onSuccessfulActionContinue();
      } else if (step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
        System.out.println("You successfully ran away from the enemy!");
        controller.onSuccessfulActionContinue();
      }
      System.out.println(
          "(N)orth, (S)outh, (E)ast, (W)est, (i)nfo or 'exit'.");
      String input = scanner.nextLine();
      controller.onMapInputContinue(input);

    } catch (Exception e) {
      System.err.println("Error during console drawing: " + e.getMessage());

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

    System.out.println("Map Size: " + side + "x" + side
        + ", Level: " + gameEngine.getHero().getLevel()
        + ", Exp: " + gameEngine.getHero().getExperience()
        + ", Att: " + gameEngine.getHero().getAttack()
        + ", Def: " + gameEngine.getHero().getDefense()
        + ", HP: " + gameEngine.getHero().getHitPoints()
        + ", " + gameEngine.getHero().getWeapon()
        + ", " + gameEngine.getHero().getArmor()
        + ", " + gameEngine.getHero().getHelm());

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
      System.err.println("Error during enemy encounter: " + e.getMessage());
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
      System.err.println("Error during enemy run failure: " + e.getMessage());
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
      System.err.println("Error during item found: " + e.getMessage());
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
      System.err.println("Error during victory screen: " + e.getMessage());
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
      System.err.println("Error during game over screen: " + e.getMessage());
    }
  }

}
