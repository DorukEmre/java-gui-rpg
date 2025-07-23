package demre.rpg.model.factories;

import demre.rpg.model.characters.Hero;
import demre.rpg.model.characters.Mage;
import demre.rpg.model.characters.Rogue;
import demre.rpg.model.characters.Villain;
import demre.rpg.model.characters.Warrior;
import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Weapon;
import demre.rpg.model.factories.ItemFactory;

public class CharacterFactory {
  private static CharacterFactory instance;
  private static final String[] ADJECTIVES = {
      "Silver", "Golden", "Mystic", "Party", "Enchanted", "Wobbly",
      "Ancient", "Shiny", "Pointy", "Lightweight", "Fluffy", "Bouncy"
  };
  private static final String[] WEAPON_TYPES = {
      "Banana", "Noodle", "Toothbrush", "Sock", "Plunger", "Umbrella", "Spoon" };
  private static final String[] ARMOR_TYPES = {
      "Bathrobe", "Tutu", "Onesie", "Apron", "Cardboard Box", "Bubble Wrap"
  };
  private static final String[] HELM_TYPES = {
      "Colander", "Hat", "Sombrero", "Beanie", "Tiara", "Mask"
  };

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
          ItemFactory.createWeapon(weaponName, weaponModifier),
          ItemFactory.createArmor(armorName, armorModifier),
          ItemFactory.createHelm(helmName, helmModifier));
    } else if (heroClass.equalsIgnoreCase("Warrior")) {
      hero = new Warrior(name, level, experience, attack, defense, hitPoints,
          ItemFactory.createWeapon(weaponName, weaponModifier),
          ItemFactory.createArmor(armorName, armorModifier),
          ItemFactory.createHelm(helmName, helmModifier));
    } else if (heroClass.equalsIgnoreCase("Rogue")) {
      hero = new Rogue(name, level, experience, attack, defense, hitPoints,
          ItemFactory.createWeapon(weaponName, weaponModifier),
          ItemFactory.createArmor(armorName, armorModifier),
          ItemFactory.createHelm(helmName, helmModifier));
    } else {
      throw new IllegalArgumentException("Unknown hero class: " + heroClass);
    }
    return hero;

  }

  public Hero newHero(String heroClass, String name,
      int level, int experience, int attack, int defense, int hitPoints,
      int weaponModifier,
      int armorModifier,
      int helmModifier) {
    return newHero(heroClass, name, level, experience, attack, defense, hitPoints,
        generateWeaponName(), weaponModifier,
        generateArmorName(), armorModifier,
        generateHelmName(), helmModifier);
  }

  public Villain newVillain(int level, int attack, int defense, int hitPoints,
      String weaponName, int weaponModifier,
      String armorName, int armorModifier,
      String helmName, int helmModifier,
      int x, int y) {
    return new Villain(attack, defense, hitPoints, level,
        ItemFactory.createWeapon(weaponName, weaponModifier),
        ItemFactory.createArmor(armorName, armorModifier),
        ItemFactory.createHelm(helmName, helmModifier), x, y);
  }

  public Villain newVillain(int level, int attack, int defense, int hitPoints,
      int weaponModifier,
      int armorModifier,
      int helmModifier,
      int x, int y) {
    return (newVillain(level, attack, defense, hitPoints,
        generateWeaponName(), weaponModifier,
        generateArmorName(), armorModifier,
        generateHelmName(), helmModifier, x, y));
  }

  private String generateWeaponName() {
    return (ADJECTIVES[(int) (Math.random() * ADJECTIVES.length)] + " "
        + WEAPON_TYPES[(int) (Math.random() * WEAPON_TYPES.length)]);
  }

  private String generateArmorName() {
    return (ADJECTIVES[(int) (Math.random() * ADJECTIVES.length)] + " "
        + ARMOR_TYPES[(int) (Math.random() * ARMOR_TYPES.length)]);
  }

  private String generateHelmName() {
    return (ADJECTIVES[(int) (Math.random() * ADJECTIVES.length)] + " "
        + HELM_TYPES[(int) (Math.random() * HELM_TYPES.length)]);
  }

}
