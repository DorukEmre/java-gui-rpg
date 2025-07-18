package demre.rpg.model.characters;

public abstract class AbstractCharacter implements Character {
  private String name;

  private int attack;
  private int defense;
  private int hitPoints;

  private int level;

  private int xCoord;
  private int yCoord;

  // Constructors

  protected AbstractCharacter(
      String name, int attack, int defense, int hitPoints, int level) {
    this.name = name;
    this.attack = attack;
    this.defense = defense;
    this.hitPoints = hitPoints;
    this.level = level;
  }

  protected AbstractCharacter(
      String name, int attack, int defense, int hitPoints, int level, int x, int y) {
    this.name = name;
    this.attack = attack;
    this.defense = defense;
    this.hitPoints = hitPoints;
    this.level = level;
    this.xCoord = x;
    this.yCoord = y;
  }

  // Getters

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getAttack() {
    return attack;
  }

  @Override
  public int getDefense() {
    return defense;
  }

  @Override
  public int getHitPoints() {
    return hitPoints;
  }

  @Override
  public int getLevel() {
    return level;
  }

  @Override
  public int getXCoord() {
    return xCoord;
  }

  @Override
  public int getYCoord() {
    return yCoord;
  }

  // Setters

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setAttack(int attack) {
    this.attack = attack;
  }

  @Override
  public void setDefense(int defense) {
    this.defense = defense;
  }

  @Override
  public void setHitPoints(int hitPoints) {
    this.hitPoints = hitPoints;
  }

  @Override
  public void setLevel(int level) {
    this.level = level;
  }

  @Override
  public void setXCoord(int x) {
    this.xCoord = x;
  }

  @Override
  public void setYCoord(int y) {
    this.yCoord = y;
  }
}