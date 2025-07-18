package demre.rpg.model.characters;

import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Weapon;
import jakarta.validation.constraints.NotNull;

public abstract class Hero extends AbstractCharacter {
  @NotNull
  private String heroClass;

  @NotNull
  private Weapon weapon;

  @NotNull
  private Armor armor;

  @NotNull
  private Helm helm;

  private int experience;

  // Constructors

  protected Hero(
      String name, int level, int experience,
      int attack, int defense, int hitPoints,
      Weapon weapon, Armor armor, Helm helm) {

    super(name, attack, defense, hitPoints, level);

    this.experience = experience;
    this.weapon = weapon;
    this.armor = armor;
    this.helm = helm;
  }

  protected Hero(
      String name, int level, int experience,
      int attack, int defense, int hitPoints,
      Weapon weapon, Armor armor, Helm helm, int x, int y) {

    super(name, attack, defense, hitPoints, level, x, y);

    this.experience = experience;
    this.weapon = weapon;
    this.armor = armor;
    this.helm = helm;
  }

  // Getters

  public int getExperience() {
    return experience;
  }

  public String getHeroClass() {
    return heroClass;
  }

  public Weapon getWeapon() {
    return weapon;
  }

  public Armor getArmor() {
    return armor;
  }

  public Helm getHelm() {
    return helm;
  }

  // Setters

  public void setExperience(int experience) {
    this.experience = experience;
  }

  public void setHeroClass(String heroClass) {
    this.heroClass = heroClass;
  }

  public void setWeapon(Weapon weapon) {
    this.weapon = weapon;
  }

  public void setArmor(Armor armor) {
    this.armor = armor;
  }

  public void setHelm(Helm helm) {
    this.helm = helm;
  }

}
