package demre.rpg.model.characters;

import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Weapon;

public abstract class AbstractCharacter implements Character {
  private String name;

  private int attack;
  private int defense;
  private int hitPoints;

  private int level;

  private Weapon weapon;
  private Armor armor;
  private Helm helm;

  private int xCoord;
  private int yCoord;

  // Constructors

  protected AbstractCharacter(
      String name, int attack, int defense, int hitPoints, int level,
      Weapon weapon, Armor armor, Helm helm) {
    this.name = name;
    this.attack = attack;
    this.defense = defense;
    this.hitPoints = hitPoints;
    this.level = level;
    this.weapon = weapon;
    this.armor = armor;
    this.helm = helm;
  }

  protected AbstractCharacter(
      String name, int attack, int defense, int hitPoints, int level,
      Weapon weapon, Armor armor, Helm helm, int x, int y) {
    this(name, attack, defense, hitPoints, level, weapon, armor, helm);
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
  public Weapon getWeapon() {
    return weapon;
  }

  @Override
  public Armor getArmor() {
    return armor;
  }

  @Override
  public Helm getHelm() {
    return helm;
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
  public void setWeapon(Weapon weapon) {
    this.weapon = weapon;
  }

  @Override
  public void setArmor(Armor armor) {
    this.armor = armor;
  }

  @Override
  public void setHelm(Helm helm) {
    this.helm = helm;
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