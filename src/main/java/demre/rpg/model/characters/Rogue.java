package demre.rpg.model.characters;

import demre.rpg.model.items.Weapon;
import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;

public class Rogue extends Hero {

  public Rogue(
      String name, int level, int experience,
      int attack, int defense, int hitPoints,
      Weapon weapon, Armor armor, Helm helm) {

    super(
        name, level, experience, attack, defense, hitPoints, weapon, armor, helm);
    setHeroClass("Rogue");
  }

}
