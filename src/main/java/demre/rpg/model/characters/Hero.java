package demre.rpg.model.characters;

import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Weapon;
import jakarta.validation.constraints.NotNull;

public abstract class Hero extends AbstractCharacter {
  @NotNull
  private String heroClass;

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
