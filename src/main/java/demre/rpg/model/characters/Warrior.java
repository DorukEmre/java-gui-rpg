package demre.rpg.model.characters;

import demre.rpg.model.items.Weapon;
import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;

public class Warrior extends Hero {

  public Warrior(
      String name, int level, int experience,
      int attack, int defense, int hitPoints,
      Weapon weapon, Armor armor, Helm helm) {

    super(
        name, level, experience, attack, defense, hitPoints, weapon, armor, helm);
    setHeroClass("Warrior");
  }

}
