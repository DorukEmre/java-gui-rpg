package demre.rpg.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import demre.rpg.model.characters.Hero;
import demre.rpg.util.CharacterFactory;
import demre.rpg.view.GameView;
import jakarta.validation.constraints.NotNull;

public class GameEngine {
  public enum Step {
    SPLASH_SCREEN,
    SELECT_HERO, INVALID_HERO_SELECTION,
    CREATE_HERO, INVALID_HERO_CREATION,
    INFO,
    PLAYING, INVALID_ACTION,
    EXIT_GAME
  }

  public enum Direction {
    NORTH, SOUTH, EAST, WEST
  }

  public enum Action {
    FIGHT, RUN, KEEP, DROP
  }

  private Step step;
  private Hero hero;
  private List<Hero> heroes;

  private int mapSize = 9; // Level 1 map size

  // Constructor

  public GameEngine()
      throws FileNotFoundException, IOException {
    this.step = Step.SPLASH_SCREEN;
    this.heroes = HeroLoader.loadHeroes();
    this.hero = null;
    System.out.println("GameEngine initialised.");
  }

  // Getters

  public Step getStep() {
    return step;
  }

  public Hero getHero() {
    return hero;
  }

  public List<Hero> getHeroes() {
    return heroes;
  }

  public int getMapSize() {
    return mapSize;
  }

  // Setters

  public void setCurrentStep(Step newStep) {
    this.step = newStep;
  }

  public void setHero(Hero hero) {
    this.hero = hero;
  }

  public void setHeroes(List<Hero> heroes) {
    this.heroes = heroes;
  }

  public void setMapSize(int mapSize) {
    this.mapSize = mapSize;
  }

  // Methods

  public void startGame(@NotNull GameView gameView) {
    System.out.println("GameEngine > Starting game...");

    while (step != Step.EXIT_GAME) {

      if (step == Step.SPLASH_SCREEN) {
        // Show splash screen
        gameView.splashScreen();
      } else if (step == Step.SELECT_HERO
          || step == Step.INVALID_HERO_SELECTION) {
        // Show hero selection screen
        gameView.selectHero();
      } else if (step == Step.CREATE_HERO
          || step == Step.INVALID_HERO_CREATION) {
        // Show hero creation screen
        gameView.createHero();
      } else if (step == Step.INFO) {
        // Show hero info screen
        gameView.showHero();
      } else if (step == Step.PLAYING
          || step == Step.INVALID_ACTION) {
        // Update the view to reflect the current game state
        gameView.updateView();
      }
    }
    System.out.println("GameEngine > Ending game...");
  }

  public boolean isValidHeroSelection(@NotNull String selection) {
    if (selection.equalsIgnoreCase("valid"))
      return true;
    if (heroes == null || heroes.size() == 0) {
      return false;
    }

    // Convert selection to int and check if it exists in the heroes array
    try {
      int index = Integer.parseInt(selection) - 1;
      if (index >= 0 && index < heroes.size()) {
        return true;
      }
    } catch (NumberFormatException e) {
      // Ignore, will return false below
    }
    return false;
  }

  public boolean isValidHeroName(String name) {
    if (name == null || name.trim().isEmpty()
        || name.length() < 3 || name.length() > 20
        || !name.matches("[a-zA-Z0-9 ]+")) {
      return false;
    }
    return true;
  }

  public boolean isValidHeroClass(String heroClass) {
    return (heroClass.equalsIgnoreCase("Mage")
        || heroClass.equalsIgnoreCase("mage")
        || heroClass.equalsIgnoreCase("m")
        || heroClass.equalsIgnoreCase("Warrior")
        || heroClass.equalsIgnoreCase("warrior")
        || heroClass.equalsIgnoreCase("w")
        || heroClass.equalsIgnoreCase("Rogue")
        || heroClass.equalsIgnoreCase("rogue")
        || heroClass.equalsIgnoreCase("r"));
  }

  public void selectHero(@NotNull String selection) {
    System.out.println("GameEngine > Selecting hero: " + selection);

    int index = Integer.parseInt(selection) - 1;
    setHero(heroes.get(index));

    initialiseGameState();

    System.out.println("Hero selected: " + hero.getName());
  }

  public void createHero(@NotNull String name, @NotNull String heroClass) {
    System.out.println("GameEngine > Creating hero: " + name);
    Hero newHero;
    CharacterFactory factory = CharacterFactory.getInstance();

    if (heroClass.equalsIgnoreCase("Mage")
        || heroClass.equalsIgnoreCase("mage")
        || heroClass.equalsIgnoreCase("m")) {
      newHero = factory.newHero("Mage", name, 1, 0, 5, 5, 10, "Wooden stick", "Cloth armor", "Paper hat");

    } else if (heroClass.equalsIgnoreCase("Warrior")
        || heroClass.equalsIgnoreCase("warrior")
        || heroClass.equalsIgnoreCase("w")) {
      newHero = factory.newHero("Warrior", name, 1, 0, 5, 5, 10, "Wooden stick", "Cloth armor", "Paper hat");

    } else if (heroClass.equalsIgnoreCase("Rogue")
        || heroClass.equalsIgnoreCase("rogue")
        || heroClass.equalsIgnoreCase("r")) {
      newHero = factory.newHero("Rogue", name, 1, 0, 5, 5, 10, "Wooden stick", "Cloth armor", "Paper hat");
    } else {
      throw new IllegalArgumentException("Invalid hero class: " + heroClass);
    }

    setHero(newHero);

    initialiseGameState();

  }

  private void initialiseGameState() {
    int side = (hero.getLevel() - 1) * 5 + 10 - (hero.getLevel() % 2);
    setMapSize(side);

    hero.setXCoord(side / 2);
    hero.setYCoord(side / 2);

    // generateEnnemies();
  }

  public boolean isValidDirection(String input) {
    return (input.equalsIgnoreCase("N")
        || input.equalsIgnoreCase("S")
        || input.equalsIgnoreCase("E")
        || input.equalsIgnoreCase("W")
        || input.equalsIgnoreCase("n")
        || input.equalsIgnoreCase("s")
        || input.equalsIgnoreCase("e")
        || input.equalsIgnoreCase("w")
        || input.equalsIgnoreCase("North")
        || input.equalsIgnoreCase("South")
        || input.equalsIgnoreCase("East")
        || input.equalsIgnoreCase("West"));
  }

  public void movePlayer(String input) {

    Direction direction = parseDirection(input);
    System.out.println("Detected direction: " + direction);

    if (direction == Direction.NORTH && hero.getYCoord() > 0) {
      hero.setYCoord(hero.getYCoord() - 1);
    } else if (direction == Direction.SOUTH) {
      hero.setYCoord(hero.getYCoord() + 1);
    } else if (direction == Direction.EAST) {
      hero.setXCoord(hero.getXCoord() + 1);
    } else if (direction == Direction.WEST) {
      hero.setXCoord(hero.getXCoord() - 1);
    }

  }

  private Direction parseDirection(String input) {
    switch (input.toUpperCase()) {
      case "N":
      case "NORTH":
        return Direction.NORTH;
      case "S":
      case "SOUTH":
        return Direction.SOUTH;
      case "E":
      case "EAST":
        return Direction.EAST;
      case "W":
      case "WEST":
        return Direction.WEST;
      default:
        throw new IllegalArgumentException("Invalid direction: " + input);
    }
  }

}
