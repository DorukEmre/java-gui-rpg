package demre.rpg.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.awt.Point;

import demre.rpg.controller.GameController;
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
import demre.rpg.view.ConsoleView;
import demre.rpg.view.GUIView;
import demre.rpg.view.GameView;

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
    VICTORY_MISSION, VICTORY_INVALID_ACTION,
    GAME_OVER, GAME_OVER_INVALID_ACTION,
    EXIT_GAME
  }

  public enum Direction {
    NORTH, SOUTH, EAST, WEST
  }

  private GameView gameView;
  private GameController gameController;
  private final List<GameEngineListener> listeners = new ArrayList<>();

  private Step step;
  // Hero index in heroes list: null = non loaded, >=0 hero selected
  private Integer selectedHeroIndex = null;
  private Hero hero;
  private Hero initialHeroState; // For resetting the hero
  private List<Hero> heroes;
  private List<Villain> villains;
  private Tile[][] map;
  private Tile[] fightTiles; // Tiles involved in a fight, Hero at 0, Enemy at 1
  private Tile[] moveTiles; // Tiles involved in a move, Hero at 0, Target at 1
  private boolean isMoving; // True if last action is a move
  private Direction moveDirection; // Direction of the last move action
  private int mapSize = 9; // Level 1 map size
  private Item itemFound;

  // Constructor

  public GameEngine()
      throws FileNotFoundException, IOException {
    this.step = null;
    this.hero = null;
    this.initialHeroState = null;
    this.heroes = null;
    this.villains = new ArrayList<>();
    this.fightTiles = new Tile[2]; // 0: Hero, 1: Enemy
    this.moveTiles = new Tile[2]; // 0: Hero, 1: Target
    this.itemFound = null;
    this.isMoving = false;
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

  public Tile[] getMoveTiles() {
    return moveTiles;
  }

  public Direction getMoveDirection() {
    return moveDirection;
  }

  public Item getItemFound() {
    return itemFound;
  }

  public GameController getGameController() {
    return gameController;
  }

  public boolean isMoving() {
    return isMoving;
  }

  // Setters

  public void setCurrentStep(Step newStep) {
    this.step = newStep;
    for (GameEngineListener listener : listeners) {
      listener.onStepChanged(newStep);
    }
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

  public void setMoveTiles(Tile fromTile, Tile targetTile) {
    this.moveTiles[0] = fromTile;
    this.moveTiles[1] = targetTile;
  }

  public void setIsMoving(boolean isMoving) {
    this.isMoving = isMoving;
  }

  public void setMoveDirection(Direction direction) {
    this.moveDirection = direction;
  }

  public void setItemFound(Item item) {
    this.itemFound = item;
  }

  public void setGameView(String newGameView) {

    // Unregister current listener if needed
    if (this.gameView instanceof GameEngineListener) {
      removeListener((GameEngineListener) this.gameView);
    }

    if (this.gameView instanceof GUIView guiView) {
      guiView.dispose();
    }

    // Create new view
    if (newGameView.equals("gui")) {
      this.gameView = new GUIView(this, getGameController());
    } else { // (newGameView.equals("console"))
      this.gameView = new ConsoleView(this, getGameController());
    }

    // Register new view as listener and notify it of the current step
    if (this.gameView instanceof GameEngineListener) {
      GameEngineListener newListener = (GameEngineListener) this.gameView;
      addListener(newListener);
      newListener.onStepChanged(this.step);
    }
  }

  // Methods

  public void initialise(GameView gameView, GameController gameController)
      throws IOException {
    if (gameView == null || gameController == null) {
      throw new IllegalArgumentException(
          "Initialisation parameters cannot be null.");
    }
    this.gameView = gameView;
    this.gameController = gameController;

    addListener((GameEngineListener) gameView);

    loadHeroes();
  }

  // Listeners

  public void addListener(GameEngineListener listener) {
    listeners.add(listener);
  }

  public void removeListener(GameEngineListener listener) {
    listeners.remove(listener);
  }

  // Database operations

  public void loadHeroes() throws IOException {
    System.out.println("GameEngine > Loading heroes from database...");

    if (heroes != null) {
      heroes.clear();
    }

    HeroLoader.loadHeroesFromDatabase(this);

    if (heroes == null) {
      heroes = new ArrayList<>();
    }

    System.out.println("GameEngine > Heroes loaded: "
        + (heroes != null ? heroes.size() : 0));
  }

  public void saveHeroToDatabase() throws IOException {
    System.out.println("GameEngine > Saving hero to database...");

    if (hero == null) {
      throw new IllegalStateException("No hero to save.");
    }

    HeroStorage.saveToDatabase(this);
  }

  public void deleteAllHeroes() throws IOException {
    System.out.println("GameEngine > Deleting all heroes from database...");

    HeroStorage.deleteAllHeroes();
    heroes.clear();
    selectedHeroIndex = null;
    hero = null;
    initialHeroState = null;

    System.out.println("GameEngine > All heroes deleted.");
  }

  public void generateHeroes() throws IOException {
    System.out.println("GameEngine > Generating heroes...");

    HeroStorage.generateHeroes(this, 10);
    loadHeroes();
  }

  //

  public void startGame() {
    System.out.println("GameEngine > Starting game...");

    if (gameView == null || gameController == null) {
      throw new IllegalArgumentException(
          "Initialisation parameters cannot be null.");
    }

    setCurrentStep(Step.SPLASH_SCREEN);
  }

  public void exitGame() {
    System.out.println("GameEngine > Exiting game...");
    setCurrentStep(Step.EXIT_GAME);
    // saveHeroToDatabase();
  }

  //

  public boolean isValidHeroSelection(String selection) {
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
        || heroClass.equalsIgnoreCase("r")
        || heroClass.equals("1")
        || heroClass.equals("2")
        || heroClass.equals("3"));
  }

  public void selectHero(String selection) {
    System.out.println("GameEngine > Selecting hero: " + selection);

    int index = Integer.parseInt(selection) - 1;

    setHero(heroes.get(index));
    setInitialHeroState(heroes.get(index));
    selectedHeroIndex = index;
  }

  public void createHero(String name, String heroClass) {
    System.out.println("GameEngine > Creating hero: " + name);
    Hero newHero;
    CharacterFactory factory = CharacterFactory.getInstance();

    if (heroClass.equalsIgnoreCase("Warrior")
        || heroClass.equalsIgnoreCase("w") || heroClass.equals("1")) {
      newHero = factory.newHero("Warrior", name, 1, 0, 5, 5, 20, 1, 1, 1);

    } else if (heroClass.equalsIgnoreCase("Rogue")
        || heroClass.equalsIgnoreCase("r") || heroClass.equals("2")) {
      newHero = factory.newHero("Rogue", name, 1, 0, 5, 5, 20, 1, 1, 1);

    } else if (heroClass.equalsIgnoreCase("Mage")
        || heroClass.equalsIgnoreCase("m") || heroClass.equals("3")) {
      newHero = factory.newHero("Mage", name, 1, 0, 5, 5, 20, 1, 1, 1);
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
    moveTiles = new Tile[2];
    initialiseGameState();
  }

  private void initialiseGameState() {
    int side = (hero.getLevel() - 1) * 5 + 10 - (hero.getLevel() % 2);
    setMapSize(side);

    hero.setXCoord(side / 2);
    hero.setYCoord(side / 2);

    generateVillains();

    // // make copy of villains and sort villains by level
    // List<Villain> villainsCopy = new ArrayList<>(villains);
    // villainsCopy.sort(
    // (v1, v2) -> Integer.compare(v1.getLevel(), v2.getLevel()));
    // // list all villains
    // for (Villain villain : villainsCopy) {
    // System.out.println(villain.toString());
    // }
    // System.out.println(hero.toString());

    generateMap();
  }

  private void generateMap() {
    map = new Tile[mapSize + 2][mapSize + 2];

    // Create the map with border
    for (int x = 0; x < mapSize + 2; x++) {
      for (int y = 0; y < mapSize + 2; y++) {
        Tile.Type type = Tile.Type.GRASS;
        Boolean visible = false;
        if (x == 0 || x == mapSize + 1 || y == 0 || y == mapSize + 1) {
          type = Tile.Type.BORDER;
          visible = true;
        }
        map[y][x] = TileFactory.createTile(x, y, type, visible);
      }
    }

    // Assign hero to starting position
    Tile heroTile = map[hero.getYCoord() + 1][hero.getXCoord() + 1];
    heroTile.assignHero();

    // Assign villains to their respective tiles
    for (Villain villain : villains) {
      Tile enemyTile = map[villain.getYCoord() + 1][villain.getXCoord() + 1];
      enemyTile.assignEnemy();
      // enemyTile.setVisible(true);
    }
  }

  private void generateVillains() {

    CharacterFactory factory = CharacterFactory.getInstance();

    villains.clear();
    // Villain coordinates should be unique
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
      int level = hero.getLevel() + (int) (Math.random() * 3) - 1;
      if (level < 1) {
        level = 1;
      }

      Villain villain = factory.generateVillain(level, x, y);
      villains.add(villain);
    }

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
    if (targetTile.getType() == Tile.Type.ENEMY) { // fight
      targetTile.setVisible(true);
      setFightTiles(currentHeroTile, targetTile);
      setMoveDirection(direction);
      setCurrentStep(Step.ENEMY_ENCOUNTER);

    } else if (targetTile.getType() == Tile.Type.BORDER) { // victory
      setInitialHeroState(hero); // Replace with current hero state
      saveHeroToDatabase();
      setCurrentStep(Step.VICTORY_MISSION);

    } else { // move
      setMoveTiles(currentHeroTile, targetTile);
      setIsMoving(true);
      setMoveDirection(direction);
      currentHeroTile.assignGrass();
      hero.setXCoord(newX);
      hero.setYCoord(newY);
      targetTile.assignHero();
      setCurrentStep(Step.PLAYING);
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

  public void fightEnemy() {
    System.out.println("GameEngine > Fighting enemy...");

    Hero hero = getHero();
    Tile heroTile = getFightTiles()[0];
    Tile enemyTile = getFightTiles()[1];
    Villain villain = getVillainAtCoord(
        enemyTile.getX() - 1, enemyTile.getY() - 1);
    if (villain == null) {
      throw new IllegalStateException(
          "No villain found at enemy tile coordinates.");
    }

    if (isHeroVictorious(villain)) {
      // Add experience and level up if needed
      Boolean levelUp = false;
      int prevExperience = hero.getExperience();
      int experienceReward = villain.getExperienceReward();

      if (hero.getHeroClass().equals("Warrior")) {
        experienceReward *= 1.1; // Warriors get 10% more XP
      }

      // Adjust experience based on level difference
      if (villain.getLevel() == hero.getLevel() - 1) {
        experienceReward = (int) (experienceReward * 0.5);
      } else if (villain.getLevel() <= hero.getLevel() - 2) {
        experienceReward = (int) (experienceReward * 0.25);
      }
      hero.addExperience(experienceReward);
      levelUp = hero.checkAndApplyLevelUp(prevExperience);

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
        setCurrentStep(Step.ITEM_FOUND_AND_LEVEL_UP);
      else if (levelUp)
        setCurrentStep(Step.LEVEL_UP);
      else if (itemFound)
        setCurrentStep(Step.ITEM_FOUND);
      else
        setCurrentStep(Step.ENEMY_FIGHT_SUCCESS);

    } else {
      setCurrentStep(Step.GAME_OVER);
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
          .println("GameEngine > Hero attacks for: "
              + heroAttackRoll + " - " + villainDefense + " = " + heroDamage
              + " damage. Villain HP: " + villainHitPoints);

      if (villainHitPoints <= 0) {
        System.out.println("GameEngine > Villain defeated!");
        System.out.println(
            "GameEngine > Hero hit points after fight: " + heroHitPoints);
        // hero.setHitPoints(heroHitPoints);
        return true;
      }

      // Villain attacks back
      if (hero.getHeroClass().equals("Rogue"))
        heroCrit = Math.random() < 0.2; // Rogues +10% dodge attacks
      else
        heroCrit = Math.random() < 0.1;
      int heroDefenseThisTurn = heroDefense;
      if (heroCrit) {
        heroDefenseThisTurn *= 2;
      }
      int villainAttackRoll = villainAttack + (int) (Math.random() * 2);
      int villainDamage = Math.max(1, villainAttackRoll - heroDefenseThisTurn);
      heroHitPoints -= villainDamage;
      System.out.println(
          "GameEngine > Villain attacks for: "
              + villainAttack + " - " + heroDefenseThisTurn + " = " + villainDamage
              + " damage. Hero HP: " + heroHitPoints);

      if (heroHitPoints <= 0) {
        System.out.println("GameEngine > Hero defeated!");
        return false;
      }

      turn--;
    }
    // Enemy runs away, default player victory
    return true;
  }

  public void runFromEnemy() {
    System.out.println("GameEngine > Running from enemy...");
    // 50% chance to run away
    if (Math.random() < 0.5) {
      System.out.println("GameEngine > Hero run away - SUCCESS");
      setCurrentStep(Step.ENEMY_RUN_SUCCESS);
      return;
    }
    System.out.println("GameEngine > Hero run away - FAILURE");
    setCurrentStep(Step.ENEMY_RUN_FAILURE);
  }

  private Boolean checkForItemFound(Villain villain) {
    System.out.println("GameEngine > Checking for item found...");

    // 20% chance to find an item. Mages +10%
    double dropChance = hero.getHeroClass().equals("Mage") ? 0.22 : 0.2;

    if (Math.random() < dropChance) {
      // Randomly select an item from the villain's items
      Item[] villainItems = {
          villain.getWeapon(),
          villain.getArmor(),
          villain.getHelm()
      };
      Item item = villainItems[(int) (Math.random() * villainItems.length)];

      setItemFound(item);
      return true;
    } else {
      return false;
    }
  }

  public void keepItem() {
    // Replace the item in the hero's inventory
    Item item = getItemFound();

    if (item != null) {
      if (item.getType() == Item.Type.WEAPON) {
        hero.setWeapon((Weapon) item);
      } else if (item.getType() == Item.Type.ARMOR) {
        hero.setArmor((Armor) item);
      } else if (item.getType() == Item.Type.HELM) {
        hero.setHelm((Helm) item);
      }
      setItemFound(null);
    }
  }

}
