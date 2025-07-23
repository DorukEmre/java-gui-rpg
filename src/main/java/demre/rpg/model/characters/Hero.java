package demre.rpg.model.characters;

import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Weapon;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public abstract class Hero extends AbstractCharacter {
  @NotBlank
  private String heroClass;

  @Min(0)
  private int experience;

  // Constructors

  protected Hero(
      String name, int level, int experience,
      int attack, int defense, int hitPoints,
      Weapon weapon, Armor armor, Helm helm) {

    super(name, attack, defense, hitPoints, level, weapon, armor, helm);

    this.experience = experience;
  }

  protected Hero(
      String name, int level, int experience,
      int attack, int defense, int hitPoints,
      Weapon weapon, Armor armor, Helm helm, int x, int y) {

    super(name, attack, defense, hitPoints, level, weapon, armor, helm, x, y);

    this.experience = experience;
  }

  // Getters

  public int getExperience() {
    return experience;
  }

  public String getHeroClass() {
    return heroClass;
  }

  // Setters

  public void setExperience(int experience) {
    this.experience = experience;
  }

  public void setHeroClass(String heroClass) {
    this.heroClass = heroClass;
  }

  // Methods

  public void addExperience(int experience) {
    this.experience += experience;
  }

  public Boolean checkLevelUp(int prevExperience) {
    // Level 1 - 1000 XP
    // Level 2 - 2450 XP
    // Level 3 - 4800 XP
    // Level 4 - 8050 XP
    // Level 5 - 12200 XP

    int experienceNeeded = getLevel() * 1000
        + (getLevel() - 1) * (getLevel() - 1) * 450;
    if (prevExperience < experienceNeeded
        && getExperience() >= experienceNeeded) {
      setLevel(getLevel() + 1);
      setAttack(getAttack() + (int) (Math.random() * 3)); // add 0-2
      setDefense(getDefense() + (int) (Math.random() * 3)); // add 0-2
      setHitPoints(getHitPoints() + (int) (Math.random() * 3) + 4); // add 4-6
      return true;
    }
    return false;
  }

  public abstract Hero copy();

  public String description() {
    return String.format(
        "%s, %s\n\tLevel: %d exp: %d att: %d def: %d HP: %d\t%s, %s, %s",
        getName(),
        heroClass,
        getLevel(),
        experience,
        getAttack(),
        getDefense(),
        getHitPoints(),
        getWeapon(),
        getArmor(),
        getHelm());
  }

  public String toString() {
    return "Hero\t{" +
        "name='" + getName() + '\'' +
        ", class='" + heroClass + '\'' +
        ", level=" + getLevel() +
        ", experience=" + experience +
        ", attack=" + getAttack() +
        ", defense=" + getDefense() +
        ", hp=" + getHitPoints() +
        ", weapon=" + getWeapon().getName() +
        ", +" + getWeapon().getModifier() +
        ", armor=" + getArmor().getName() +
        ", +" + getArmor().getModifier() +
        ", helm=" + getHelm().getName() +
        ", +" + getHelm().getModifier() +
        ", at (" + getXCoord() + ", " + getYCoord() + ")" +
        '}';
  }

}
