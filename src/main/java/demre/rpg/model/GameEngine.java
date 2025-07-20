package demre.rpg.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import demre.rpg.model.characters.Hero;
import demre.rpg.model.characters.Villain;
import demre.rpg.model.map.Tile;
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
    ENEMY_ENCOUNTER, ENEMY_INVALID_ACTION,
    ENEMY_FIGHT_SUCCESS, ENEMY_RUN_SUCCESS, ENEMY_RUN_FAILURE,
    ITEM_FOUND, ITEM_INVALID_ACTION,
    VICTORY, GAME_OVER,
    EXIT_GAME
  }

  public enum Direction {
    NORTH, SOUTH, EAST, WEST
  }

  public enum Action {
    FIGHT, RUN, KEEP, DROP
  }

  public enum Special {
    NONE, ENEMY, VICTORY
  }

  private Step step;
  private Hero hero;
  private List<Hero> heroes;
  private List<Villain> villains;
  private Special event;
  private Tile[][] map;
  private Tile[] fightTiles; // Tiles involved in a fight, Hero at 0, Enemy at 1

  private int mapSize = 9; // Level 1 map size

  // Constructor

  public GameEngine()
      throws FileNotFoundException, IOException {
    this.step = Step.SPLASH_SCREEN;
    this.heroes = HeroLoader.loadHeroes();
    this.hero = null;
    this.event = Special.NONE;
    this.fightTiles = new Tile[2]; // 0: Hero, 1: Enemy
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

  public List<Villain> getVillains() {
    return villains;
  }

  public Villain getVillainAtCoord(int x, int y) {
    for (Villain villain : villains) {
      if (villain.getXCoord() == x && villain.getYCoord() == y) {
        return villain;
      }
    }
    return null;
  }

  public int getMapSize() {
    return mapSize;
  }

  public Tile[][] getMap() {
    return map;
  }

  public Special getEvent() {
    return event;
  }

  public Tile[] getFightTiles() {
    return fightTiles;
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

  public void setVillains(List<Villain> villains) {
    this.villains = villains;
  }

  public void setMapSize(int mapSize) {
    this.mapSize = mapSize;
  }

  public void setEvent(Special event) {
    this.event = event;
  }

  public void setFightTiles(Tile heroTile, Tile enemyTile) {
    this.fightTiles[0] = heroTile;
    this.fightTiles[1] = enemyTile;
  }

  // Methods

  public void startGame(@NotNull GameView gameView) {
    System.out.println("GameEngine > Starting game...");

    while (step != Step.EXIT_GAME) {
      System.out.println("GameEngine > Current step: " + step);

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
          || step == Step.INVALID_ACTION
          || step == Step.ENEMY_FIGHT_SUCCESS
          || step == Step.ENEMY_RUN_SUCCESS) {
        // Update the view to reflect the current game state
        gameView.updateView();
      } else if (step == Step.ENEMY_ENCOUNTER
          || step == Step.ENEMY_INVALID_ACTION) {
        gameView.showEnemyEncounter();
      } else if (step == Step.ENEMY_RUN_FAILURE) {
        gameView.showEnemyRunFailure();
      } else if (step == Step.ITEM_FOUND
          || step == Step.ITEM_INVALID_ACTION) {
        gameView.showItemFound();
      } else if (step == Step.VICTORY) {
        gameView.showVictoryScreen();
      } else if (step == Step.GAME_OVER) {
        gameView.showGameOver();
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

    generateMap();
  }

  private void generateMap() {
    map = new Tile[mapSize + 2][mapSize + 2];
    for (int x = 0; x < mapSize + 2; x++) {
      for (int y = 0; y < mapSize + 2; y++) {
        String type = "Grass";
        String symbol = ".";
        Boolean visible = false;
        if (x == 0 || x == mapSize + 1 || y == 0 || y == mapSize + 1) {
          type = "Wall";
          symbol = "#";
          visible = true;
        }
        map[y][x] = new Tile(x, y, type, symbol, visible);
      }
    }

    // Set the hero's starting position
    Tile heroTile = map[hero.getYCoord() + 1][hero.getXCoord() + 1];
    heroTile.assignHero();

    // Hard coded one enemy
    Tile enemyTile = map[3][5];
    enemyTile.assignEnemy();
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

    int heroX = hero.getXCoord();
    int heroY = hero.getYCoord();
    Tile currentHeroTile = map[heroY + 1][heroX + 1];

    int newX = heroX;
    int newY = heroY;

    if (direction == Direction.NORTH) {
      newY = heroY - 1;
    } else if (direction == Direction.SOUTH) {
      newY = heroY + 1;
    } else if (direction == Direction.EAST) {
      newX = heroX + 1;
    } else if (direction == Direction.WEST) {
      newX = heroX - 1;
    }

    Tile targetTile = map[newY + 1][newX + 1];
    if (targetTile.getType().equals("Enemy")) {
      event = Special.ENEMY;
      targetTile.setVisible(true);
      setFightTiles(currentHeroTile, targetTile);
      return;
    } else if (targetTile.getType().equals("Wall")) {
      event = Special.VICTORY;
      hero.setXCoord(newX);
      hero.setYCoord(newY);
    } else {
      event = Special.NONE;
      currentHeroTile.assignGrass();
      hero.setXCoord(newX);
      hero.setYCoord(newY);
    }
    targetTile.assignHero();

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

  public boolean fightEnemy() {
    System.out.println("GameEngine > Fighting enemy...");

    Hero hero = getHero();
    Tile heroTile = getFightTiles()[0];
    Tile enemyTile = getFightTiles()[1];
    // Villain villain = getVillainAtCoord(enemyTile.getX(), enemyTile.getY());
    // if (villain == null) {
    // System.out.println("No enemy found at the specified coordinates.");
    // return false;
    // }
    // villain.getHitPoints();

    hero.setHitPoints(hero.getHitPoints() - 1); // Example: reduce HP by 1
    if (hero.getHitPoints() <= 0) {
      return false;
    } else {
      // else if (villain.getHitPoints() <= 0) {
      System.out.println("Enemy defeated!");
      // Move the hero to the enemy's tile
      System.out.println("Herotile coordinates: " + heroTile.getX() + ", " + heroTile.getY());
      System.out.println("Enemytile coordinates: " + enemyTile.getX() + ", " + enemyTile.getY());
      System.out.println("Hero coordinates: " + hero.getXCoord() + ", " + hero.getYCoord());
      // Assign the hero to the enemy tile and grass to the hero
      enemyTile.assignHero();
      heroTile.assignGrass();
      hero.setXCoord(enemyTile.getX() - 1);
      hero.setYCoord(enemyTile.getY() - 1);
      // Remove the enemy from the villains list
      // villains.remove(villain);
      return true;
    }
    // return true;
    // return false;
  }

  public boolean runFromEnemy() {
    System.out.println("GameEngine > Running from enemy...");
    // return true;
    return false;
  }

}
