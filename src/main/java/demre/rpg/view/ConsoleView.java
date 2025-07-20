package demre.rpg.view;

import java.util.List;
import java.util.Scanner;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;

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
  public void selectHero(GameEngine.Step step) {
    System.out.println("ConsoleView > Selecting hero...");
    try {
      clearConsole();

      // Display the list of heroes
      List<Hero> heroes = gameEngine.getHeroes();
      for (int i = 0; i < heroes.size(); i++) {
        Hero hero = heroes.get(i);
        System.out.println((i + 1) + ". " + hero.getName() + " (" + hero.getClass().getSimpleName() + ")");
      }
      System.out.println();

      if (step == GameEngine.Step.INVALID_HERO_SELECTION) {
        System.out.println("Invalid hero selection. Please try again.");
      }
      System.out
          .println("Enter a number to select your hero from the list, or type 'new' [n] to create a new hero:\n");

      String heroSelection = scanner.nextLine();
      controller.onSelectHeroContinue(heroSelection);

    } catch (Exception e) {
      System.err.println("Error during select hero: " + e.getMessage());
    }
  }

  @Override
  public void createHero(GameEngine.Step step) {
    System.out.println("ConsoleView > Creating hero...");
    try {
      clearConsole();
      if (step == GameEngine.Step.INVALID_HERO_CREATION) {
        System.out.println("Invalid hero creation. Please try again.");
        System.out.println(
            "Hero name must be 3-20 characters long and can only contain alphanumeric characters and spaces.");
      }
      System.out.println("Enter your hero's name:");
      String heroName = scanner.nextLine();
      System.out.println("Pick a class for your hero: mage [m], warrior [w], rogue [r]");
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

      System.out.println("Name: " + hero.getName());
      System.out.println("Class: " + hero.getClass().getSimpleName());
      System.out.println("Level: " + hero.getLevel());
      System.out.println("Experience: " + hero.getExperience());
      System.out.println("Attack: " + hero.getAttack());
      System.out.println("Defense: " + hero.getDefense());
      System.out.println("Hit Points: " + hero.getHitPoints());
      System.out.println("Weapon: " + hero.getWeapon().getName());
      System.out.println("Armor: " + hero.getArmor().getName());
      System.out.println("Helm: " + hero.getHelm().getName());

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
    drawConsole();
  }

  public void drawConsole() {
    try {
      while (true) {
        clearConsole();
        drawMap();
        System.out.println(
            "(N)orth, (S)outh, (E)ast, (W)est, 'info' or 'exit'.");
        String input = scanner.nextLine();
        if ("exit".equalsIgnoreCase(input.trim())) {
          System.out.println("Goodbye!");
          break;
        }
        System.out.println("You entered: " + input);
        pause();
      }
    } catch (Exception e) {
      System.err.println("Error during console drawing: " + e.getMessage());

    }
  }

  private static void clearConsole() {
    System.out.print("\033[H\033[2J");
    System.out.flush();
  }

  // Optional pause so the user can see the output before the screen clears
  private static void pause() {
    System.out.println("\nPress Enter to continue...");
    new Scanner(System.in).nextLine();
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

    int level = 3;
    int side = (level - 1) * 5 + 10 - (level % 2);

    char[][] map = new char[side + 2][side + 2];

    for (int i = 0; i < side + 2; i++) {
      for (int j = 0; j < side + 2; j++) {
        if (i == 0 || i == side + 1 || j == 0 || j == side + 1) {
          map[i][j] = '#';
        } else {
          map[i][j] = '.';
        }
      }
    }

    int heroX = (side + 1) / 2;
    int heroY = (side + 1) / 2;
    map[heroY][heroX] = '@';

    for (int i = 0; i < side + 2; i++) {
      for (int j = 0; j < side + 2; j++) {
        System.out.print(map[i][j] + " ");
      }
      System.out.println();
    }
    System.out.println();
  }

}
