package demre.rpg.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.Point;

import demre.rpg.model.characters.Hero;
import demre.rpg.model.characters.Villain;
import demre.rpg.model.factories.CharacterFactory;
import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Item;
import demre.rpg.model.items.Weapon;
import demre.rpg.model.map.Tile;
import demre.rpg.model.factories.TileFactory;
import demre.rpg.storage.HeroLoader;
import demre.rpg.storage.HeroStorage;
import demre.rpg.view.GameView;

import jakarta.validation.constraints.NotNull;

public class GameEngine {
  public enum Step {
    SPLASH_SCREEN,
    SELECT_HERO, INVALID_HERO_SELECTION,
    CREATE_HERO, INVALID_HERO_CREATION,
    INFO,
    NEW_MISSION, PLAYING, INVALID_ACTION,
    ENEMY_ENCOUNTER, ENEMY_INVALID_ACTION,
    ENEMY_FIGHT_SUCCESS, ENEMY_RUN_SUCCESS, ENEMY_RUN_FAILURE, LEVEL_UP,
    ITEM_FOUND, ITEM_FOUND_AND_LEVEL_UP, ITEM_INVALID_ACTION,
    VICTORY_MISSION, VICTORY_INVALID_ACTION, GAME_OVER, GAME_OVER_INVALID_ACTION,
    EXIT_GAME
  }

  public enum Direction {
    NORTH, SOUTH, EAST, WEST
  }

  private Step step;
  // Hero index in heroes list: null = non loaded, >=0 hero selected
  private Integer selectedHeroIndex = null;
  private Hero hero;
  private Hero initialHeroState; // For resetting the hero
  private List<Hero> heroes;
  private List<Villain> villains;
  private Tile[][] map;
  private Tile[] fightTiles; // Tiles involved in a fight, Hero at 0, Enemy at 1
  private int mapSize = 9; // Level 1 map size
  private Item itemFound;

  // Constructor

  public GameEngine()
      throws FileNotFoundException, IOException {
    this.step = Step.SPLASH_SCREEN;
    this.hero = null;
    this.initialHeroState = null;
    this.heroes = null;
    this.villains = new ArrayList<>();
    this.fightTiles = new Tile[2]; // 0: Hero, 1: Enemy
    this.itemFound = null;
  }

  // Getters

  public Step getStep() {
    return step;
  }

  public Integer getSelectedHeroIndex() {
    return selectedHeroIndex;
  }

  public Hero getHero() {
    return hero;
  }

  public Hero getInitialHeroState() {
    return initialHeroState;
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

  public Tile[] getFightTiles() {
    return fightTiles;
  }

  public Item getItemFound() {
    return itemFound;
  }

  // Setters

  public void setCurrentStep(Step newStep) {
    this.step = newStep;
  }

  public void setSelectedHeroIndex(Integer index) {
    this.selectedHeroIndex = index;
  }

  public void setHero(Hero hero) {
    this.hero = hero;
  }

  public void setInitialHeroState(Hero hero) {
    this.initialHeroState = hero.copy();
  }

  public void setHeroes(List<Hero> heroes) {
    this.heroes = heroes;
  }

  public void setHeroInHeroesAtIndex(int index, Hero newHero) {
    if (heroes != null && index >= 0 && index < heroes.size()) {
      heroes.set(index, newHero);
    } else {
      throw new IndexOutOfBoundsException("Invalid hero index: " + index);
    }
  }

  public void addHero(Hero hero) {
    if (heroes == null) {
      heroes = new ArrayList<>();
    }
    heroes.add(hero);
  }

  public void setVillains(List<Villain> villains) {
    this.villains = villains;
  }

  public void setMapSize(int mapSize) {
    this.mapSize = mapSize;
  }

  public void setFightTiles(Tile heroTile, Tile enemyTile) {
    this.fightTiles[0] = heroTile;
    this.fightTiles[1] = enemyTile;
  }

  public void setItemFound(Item item) {
    this.itemFound = item;
  }

  // Methods

  public void initialise()
      throws IOException {
    HeroLoader.loadHeroesFromDatabase(this);
    System.out.println("GameEngine > Initialised heroes: " + (heroes != null ? heroes.size() : 0));
  }

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
      } else if (step == Step.NEW_MISSION
          || step == Step.INFO) {
        // Show hero info screen
        gameView.showHero();
      } else if (step == Step.PLAYING
          || step == Step.INVALID_ACTION
          || step == Step.ENEMY_FIGHT_SUCCESS
          || step == Step.LEVEL_UP
          || step == Step.ENEMY_RUN_SUCCESS) {
        // Update the view to reflect the current game state
        gameView.updateView();
      } else if (step == Step.ENEMY_ENCOUNTER
          || step == Step.ENEMY_INVALID_ACTION) {
        gameView.showEnemyEncounter();
      } else if (step == Step.ENEMY_RUN_FAILURE) {
        gameView.showEnemyRunFailure();
      } else if (step == Step.ITEM_FOUND
          || step == Step.ITEM_FOUND_AND_LEVEL_UP
          || step == Step.ITEM_INVALID_ACTION) {
        gameView.showItemFound();
      } else if (step == Step.VICTORY_MISSION
          || step == Step.VICTORY_INVALID_ACTION) {
        gameView.showVictoryScreen();
      } else if (step == Step.GAME_OVER
          || step == Step.GAME_OVER_INVALID_ACTION) {
        gameView.showGameOver();
      }
    }
    System.out.println("GameEngine > Ending game...");
  }

  public boolean isValidHeroSelection(@NotNull String selection) {
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
        || heroClass.equalsIgnoreCase("m")
        || heroClass.equalsIgnoreCase("Warrior")
        || heroClass.equalsIgnoreCase("w")
        || heroClass.equalsIgnoreCase("Rogue")
        || heroClass.equalsIgnoreCase("r"));
  }

  public void selectHero(@NotNull String selection) {
    System.out.println("GameEngine > Selecting hero: " + selection);

    int index = Integer.parseInt(selection) - 1;

    setHero(heroes.get(index));
    setInitialHeroState(heroes.get(index));
    selectedHeroIndex = index;
  }

  public void createHero(@NotNull String name, @NotNull String heroClass) {
    System.out.println("GameEngine > Creating hero: " + name);
    Hero newHero;
    CharacterFactory factory = CharacterFactory.getInstance();

    if (heroClass.equalsIgnoreCase("Mage")
        || heroClass.equalsIgnoreCase("m")) {
      newHero = factory.newHero("Mage", name, 1, 0, 5, 5, 20, 1, 1, 1);

    } else if (heroClass.equalsIgnoreCase("Warrior")
        || heroClass.equalsIgnoreCase("w")) {
      newHero = factory.newHero("Warrior", name, 1, 0, 5, 5, 20, 1, 1, 1);

    } else if (heroClass.equalsIgnoreCase("Rogue")
        || heroClass.equalsIgnoreCase("r")) {
      newHero = factory.newHero("Rogue", name, 1, 0, 5, 5, 20, 1, 1, 1);
    } else {
      throw new IllegalArgumentException("Invalid hero class: " + heroClass);
    }

    setHero(newHero);
    setInitialHeroState(newHero);
    addHero(newHero);
    selectedHeroIndex = heroes.size() - 1;
  }

  public void newMission(String reason) {
    System.out.println("GameEngine > Starting new mission...: " + reason);
    if (reason.equals("reset")) {
      setHero(getInitialHeroState().copy());
    }

    villains.clear();
    fightTiles = new Tile[2];
    initialiseGameState();
  }

  private void initialiseGameState() {
    int side = (hero.getLevel() - 1) * 5 + 10 - (hero.getLevel() % 2);
    setMapSize(side);

    hero.setXCoord(side / 2);
    hero.setYCoord(side / 2);

    generateVillains();
    // list all villains
    for (Villain villain : villains) {
      System.out.println(villain.toString());
    }
    System.out.println(hero.toString());

    generateMap();
  }

  private void generateMap() {
    map = new Tile[mapSize + 2][mapSize + 2];

    // Create the map with border
    for (int x = 0; x < mapSize + 2; x++) {
      for (int y = 0; y < mapSize + 2; y++) {
        String type = "Grass";
        String symbol = ".";
        Boolean visible = false;
        if (x == 0 || x == mapSize + 1 || y == 0 || y == mapSize + 1) {
          type = "Border";
          symbol = "#";
          visible = true;
        }
        map[y][x] = TileFactory.createTile(x, y, type, symbol, visible);
      }
    }

    // Assign hero to starting position
    Tile heroTile = map[hero.getYCoord() + 1][hero.getXCoord() + 1];
    heroTile.assignHero();

    // Assign villains to their respective tiles
    for (Villain villain : villains) {
      Tile enemyTile = map[villain.getYCoord() + 1][villain.getXCoord() + 1];
      enemyTile.assignEnemy();
      enemyTile.setVisible(true);
    }
  }

  private void generateVillains() {

    CharacterFactory factory = CharacterFactory.getInstance();

    Set<Point> coords = new HashSet<>();
    // Add hero coordinates to the set
    coords.add(new Point(hero.getXCoord(), hero.getYCoord()));

    int nVillains = (int) (getMapSize() * getMapSize() * 0.2);
    System.out.println("GameEngine > Generating " + nVillains + " villains...");

    for (int i = 0; i < nVillains; i++) {
      int x = (int) (Math.random() * getMapSize());
      int y = (int) (Math.random() * getMapSize());
      // Check coordinates are unique
      if (!coords.contains(new Point(x, y))) {
        coords.add(new Point(x, y));
      } else {
        i--;
        continue;
      }

      // Enemy level = hero level +/- 1
      int level = hero.getLevel() + getOffset(3, -1);
      if (level < 1) {
        level = 1;
      }
      int attack = getOffset(3, 4) + level; // 4-6 + level
      int defense = getOffset(3, 4) + level; // 4-6 + level
      // hp: 8-12 + level + (4-6) * level
      int hp = getOffset(4, 8) + level
          + getOffset(3, 4) * level;

      Villain villain = factory.newVillain(level, attack, defense, hp,
          level + getOffset(3, -1), level + getOffset(3, -1),
          level + getOffset(3, -1), x, y);
      villains.add(villain);
    }

  }

  private int getOffset(int max, int base) {
    return ((int) (Math.random() * max) + base);
  }

  public boolean isValidDirection(String input) {
    return (input.equalsIgnoreCase("N")
        || input.equalsIgnoreCase("S")
        || input.equalsIgnoreCase("E")
        || input.equalsIgnoreCase("W")
        || input.equalsIgnoreCase("North")
        || input.equalsIgnoreCase("South")
        || input.equalsIgnoreCase("East")
        || input.equalsIgnoreCase("West"));
  }

  public void movePlayer(String input)
      throws IOException {

    Direction direction = parseDirection(input);
    System.out.println("GameEngine > Detected direction: " + direction);

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

    // Check if target tile is an Enemy or a border
    Tile targetTile = map[newY + 1][newX + 1];
    if (targetTile.getType().equals("Enemy")) { // fight
      step = Step.ENEMY_ENCOUNTER;
      targetTile.setVisible(true);
      setFightTiles(currentHeroTile, targetTile);
      return;
    } else if (targetTile.getType().equals("Border")) { // victory
      step = Step.VICTORY_MISSION;
      setInitialHeroState(hero); // Replace with current hero state
      HeroStorage.saveToDatabase(this);
    } else { // move
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

  public void fightEnemy() {
    System.out.println("GameEngine > Fighting enemy...");

    Hero hero = getHero();
    Tile heroTile = getFightTiles()[0];
    Tile enemyTile = getFightTiles()[1];
    Villain villain = getVillainAtCoord(
        enemyTile.getX() - 1, enemyTile.getY() - 1);
    if (villain == null) {
      throw new IllegalStateException("No villain found at enemy tile coordinates.");
    }

    if (isHeroVictorious(villain)) {
      // Add experience and level up if needed
      Boolean levelUp = false;
      int prevExperience = hero.getExperience();
      int experienceReward = villain.getExperienceReward();
      if (villain.getLevel() == hero.getLevel() - 1) {
        experienceReward = (int) (experienceReward * 0.5);
      } else if (villain.getLevel() <= hero.getLevel() - 2) {
        experienceReward = (int) (experienceReward * 0.25);
      }
      hero.addExperience(experienceReward);
      levelUp = hero.checkLevelUp(prevExperience);

      // Assign the hero to the enemy tile and grass to the hero
      enemyTile.assignHero();
      heroTile.assignGrass();
      hero.setXCoord(enemyTile.getX() - 1);
      hero.setYCoord(enemyTile.getY() - 1);

      // Check if villain drops an item
      Boolean itemFound = checkForItemFound(villain);

      // Remove the enemy from the villains list
      villains.remove(villain);

      if (levelUp && itemFound)
        step = Step.ITEM_FOUND_AND_LEVEL_UP;
      else if (levelUp)
        step = Step.LEVEL_UP;
      else if (itemFound)
        step = Step.ITEM_FOUND;
      else
        step = Step.ENEMY_FIGHT_SUCCESS;

    } else {
      step = Step.GAME_OVER;
    }
  }

  private Boolean isHeroVictorious(Villain villain) {
    Hero hero = getHero();
    int heroAttack = hero.getAttack() + hero.getWeapon().getModifier();
    int heroDefense = hero.getDefense() + hero.getArmor().getModifier();
    int heroHitPoints = hero.getHitPoints() + hero.getHelm().getModifier();

    int villainAttack = villain.getAttack() + villain.getWeapon().getModifier();
    int villainDefense = villain.getDefense()
        + villain.getArmor().getModifier();
    int villainHitPoints = villain.getHitPoints()
        + villain.getHelm().getModifier();

    int turn = heroHitPoints * 2;
    System.out.println(hero.toString());
    System.out.println(villain.toString());

    while (heroHitPoints > 0 && villainHitPoints > 0 && turn > 0) {
      // Hero attacks first
      boolean heroCrit = Math.random() < 0.1; // 10% crit chance
      int heroAttackThisTurn = heroAttack;
      if (heroCrit) {
        heroAttackThisTurn *= 2;
      }
      int heroAttackRoll = heroAttackThisTurn + (int) (Math.random() * 4);
      int heroDamage = Math.max(1, heroAttackRoll - villainDefense);

      villainHitPoints -= heroDamage;
      System.out
          .println("GameEngine > Hero attacks for: " + heroAttackRoll + " - " + villainDefense + " = " + heroDamage
              + " damage. Villain HP: " + villainHitPoints);

      if (villainHitPoints <= 0) {
        System.out.println("GameEngine > Villain defeated!");
        System.out.println("GameEngine > Hero hit points after fight: " + heroHitPoints);
        // hero.setHitPoints(heroHitPoints);
        return true;
      }

      // Villain attacks back
      heroCrit = Math.random() < 0.1;
      int heroDefenseThisTurn = heroDefense;
      if (heroCrit) {
        heroDefenseThisTurn *= 2;
      }
      int villainAttackRoll = villainAttack + (int) (Math.random() * 2);
      int villainDamage = Math.max(1, villainAttackRoll - heroDefenseThisTurn);
      heroHitPoints -= villainDamage;
      System.out.println(
          "GameEngine > Villain attacks for: " + villainAttack + " - " + heroDefenseThisTurn + " = " + villainDamage
              + " damage. Hero HP: " + heroHitPoints);

      if (heroHitPoints <= 0) {
        System.out.println("GameEngine > Hero defeated!");
        return false;
      }

      turn--;
    }
    System.out.println("GameEngine > The enemy runs away!");
    System.out.println("GameEngine > Hero hit points after fight: " + heroHitPoints);
    return true;
  }

  public void runFromEnemy() {
    System.out.println("GameEngine > Running from enemy...");
    // 50% chance to run away
    if (Math.random() < 0.5) {
      System.out.println("GameEngine > Hero successfully ran away!");
      step = Step.ENEMY_RUN_SUCCESS;
    }
    step = Step.ENEMY_RUN_FAILURE;
  }

  private Boolean checkForItemFound(Villain villain) {
    System.out.println("GameEngine > Checking for item found...");
    // 20% chance to find an item
    if (Math.random() < 0.2) {
      System.out.println("GameEngine > Item found!");
      // Randomly select an item from the villain's items
      Item[] villainItems = {
          villain.getWeapon(),
          villain.getArmor(),
          villain.getHelm()
      };
      Item item = villainItems[(int) (Math.random() * villainItems.length)];
      setItemFound(item);
      System.out.println("GameEngine > Item found: " + item);
      return true;
    } else {
      System.out.println("GameEngine > No item found.");
      return false;
    }
  }

  public void keepItem() {
    System.out.println("GameEngine > Keeping item...");
    // Replace the item in the hero's inventory
    Item item = getItemFound();
    if (item != null) {
      if (item.getType().equals("Weapon")) {
        hero.setWeapon((Weapon) item);
      } else if (item.getType().equals("Armor")) {
        hero.setArmor((Armor) item);
      } else if (item.getType().equals("Helm")) {
        hero.setHelm((Helm) item);
      }
      System.out.println("GameEngine > Item added to inventory: " + item);
      setItemFound(null);
    } else {
      System.out.println("GameEngine > No item to keep.");
    }
  }

}
