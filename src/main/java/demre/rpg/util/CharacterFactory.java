package demre.rpg.util;

import demre.rpg.model.characters.Hero;
import demre.rpg.model.characters.Mage;
import demre.rpg.model.characters.Rogue;
import demre.rpg.model.characters.Villain;
import demre.rpg.model.characters.Warrior;
import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Weapon;

public class CharacterFactory {
  private static CharacterFactory instance;

  private CharacterFactory() {
  }

  public static CharacterFactory getInstance() {
    if (instance == null) {
      instance = new CharacterFactory();
    }
    return instance;
  }

  public Hero newHero(String heroClass, String name,
      int level, int experience, int attack, int defense, int hitPoints,
      String weaponName, int weaponModifier,
      String armorName, int armorModifier,
      String helmName, int helmModifier) {

    Hero hero;

    if (heroClass.equalsIgnoreCase("Mage")) {
      hero = new Mage(name, level, experience, attack, defense, hitPoints,
          new Weapon(weaponName, weaponModifier),
          new Armor(armorName, armorModifier),
          new Helm(helmName, helmModifier));
    } else if (heroClass.equalsIgnoreCase("Warrior")) {
      hero = new Warrior(name, level, experience, attack, defense, hitPoints,
          new Weapon(weaponName, weaponModifier),
          new Armor(armorName, armorModifier),
          new Helm(helmName, helmModifier));
    } else if (heroClass.equalsIgnoreCase("Rogue")) {
      hero = new Rogue(name, level, experience, attack, defense, hitPoints,
          new Weapon(weaponName, weaponModifier),
          new Armor(armorName, armorModifier),
          new Helm(helmName, helmModifier));
    } else {
      throw new IllegalArgumentException("Unknown hero class: " + heroClass);
    }
    return hero;

  }

  public Villain newVillain(int level, int attack, int defense, int hitPoints,
      String weaponName, int weaponModifier,
      String armorName, int armorModifier,
      String helmName, int helmModifier,
      int x, int y) {
    return new Villain(attack, defense, hitPoints, level,
        new Weapon(weaponName, weaponModifier),
        new Armor(armorName, armorModifier),
        new Helm(helmName, helmModifier), x, y);
  }

}
