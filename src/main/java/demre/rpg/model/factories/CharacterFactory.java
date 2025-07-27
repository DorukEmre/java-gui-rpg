package demre.rpg.model.factories;

import java.util.Set;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import demre.rpg.model.characters.AbstractCharacter;
import demre.rpg.model.characters.Hero;
import demre.rpg.model.characters.Mage;
import demre.rpg.model.characters.Rogue;
import demre.rpg.model.characters.Villain;
import demre.rpg.model.characters.Warrior;

public class CharacterFactory {
  private static CharacterFactory instance;

  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = factory.getValidator();

  private static final String[] ADJECTIVES = {
      "Golden", "Mystic", "Party", "Enchanted", "Wobbly", "Crusty",
      "Ancient", "Shiny", "Pointy", "Fluffy", "Bouncy", "Soggy", "Lumpy"
  };
  private static final String[] WEAPON_TYPES = {
      "Banana", "Noodle", "Toothbrush", "Sock", "Plunger",
      "Spoon", "Cactus", "Lollipop", "Fish", "Umbrella",
  };
  private static final String[] ARMOR_TYPES = {
      "Bathrobe", "Tutu", "Onesie", "Apron", "Cardboard Box",
      "Towel", "Yoga Mat", "Laundry Basket", "Bubble Wrap",
  };
  private static final String[] HELM_TYPES = {
      "Colander", "Mask", "Bucket", "Flower Pot", "Wig", "Lamp Shade",
      "Fish Bowl", "Ice Cream Cone", "Traffic Cone",
  };
  private static final String[] HERO_NAMES = {
      "Hilda", "Bob", "Clara", "Fred", "Gary", "Vera", "Dave", "Bella",
      "Ian", "Nancy", "Hank", "Alice", "Charlie", "Grace", "Victor",
      "Olivia", "Jack", "Mia", "Leo", "Sophie", "Max", "Lily", "Oscar",
  };

  private static final String[] HERO_QUALIFIERS = {
      "Heroic", "Brave", "Courageous", "Fearless", "Gallant", "Valiant",
      "Daring", "Bold", "Intrepid", "Noble", "Adventurous", "Chivalrous",
      "Vigorous", "Unyielding", "Unflinching", "Unwavering"
  };

  private CharacterFactory() {
  }

  public static CharacterFactory getInstance() {
    if (instance == null) {
      instance = new CharacterFactory();
    }
    return instance;
  }

  // Hero methods

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

    validateCharacter(hero);

    return hero;
  }

  public Hero newHero(String heroClass, String name,
      int level, int experience, int attack, int defense, int hitPoints,
      int weaponModifier,
      int armorModifier,
      int helmModifier) {

    return (newHero(heroClass, name, level, experience, attack, defense, hitPoints,
        generateWeaponName(), weaponModifier,
        generateArmorName(), armorModifier,
        generateHelmName(), helmModifier));
  }

  public Hero generateHero(int level) {

    String[] classes = { "Mage", "Warrior", "Rogue" };
    String heroClass = classes[(int) (Math.random() * classes.length)];
    String name = generateHeroName();
    int experience = (level == 1)
        ? 0
        : (level - 1) * 1000 + (level - 2) * (level - 2) * 450;
    int attack = 5 + generateBonus(level, 0);
    int defense = 5 + generateBonus(level, 0);
    int hitPoints = 20 + generateBonus(level, 4);

    return newHero(heroClass, name, level, experience,
        attack, defense, hitPoints,
        generateWeaponName(), generateItemModifier(level),
        generateArmorName(), generateItemModifier(level),
        generateHelmName(), generateItemModifier(level));
  }

  // Villain methods

  public Villain newVillain(int level, int attack, int defense, int hitPoints,
      String weaponName, int weaponModifier,
      String armorName, int armorModifier,
      String helmName, int helmModifier,
      int x, int y) {

    Villain newVillain = new Villain(attack, defense, hitPoints, level,
        ItemFactory.createWeapon(weaponName, weaponModifier),
        ItemFactory.createArmor(armorName, armorModifier),
        ItemFactory.createHelm(helmName, helmModifier), x, y);

    validateCharacter(newVillain);

    return newVillain;
  }

  public Villain generateVillain(int level, int x, int y) {

    int attack = 5 + generateBonus(level, 0);
    int defense = 5 + generateBonus(level, 0);
    int hitPoints = 10 + generateBonus(level, 4);

    return newVillain(level, attack, defense, hitPoints,
        generateWeaponName(), generateItemModifier(level),
        generateArmorName(), generateItemModifier(level),
        generateHelmName(), generateItemModifier(level),
        x, y);
  }

  // Helper methods

  private String generateHeroName() {
    String name = "This is over twenty characters";
    while (name.length() > 20) {
      name = HERO_NAMES[(int) (Math.random() * HERO_NAMES.length)]
          + " the "
          + HERO_QUALIFIERS[(int) (Math.random() * HERO_QUALIFIERS.length)];
    }
    return name;
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

  private int generateItemModifier(int level) {
    return (level + ((int) (Math.random() * 3)) - 1); // level +/- 1
  }

  private int generateBonus(int level, int offset) {
    int bonus = 0;

    for (int i = 0; i < level; i++) {
      // add (0-2 + offset) per level
      bonus += (int) (Math.random() * 3) + offset;
    }

    return bonus;
  }

  private void validateCharacter(AbstractCharacter character) {
    Set<ConstraintViolation<AbstractCharacter>> violations = validator.validate(character);

    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();

      for (ConstraintViolation<AbstractCharacter> violation : violations) {
        sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
      }
      throw new IllegalArgumentException(
          "Validation error. Character: " + sb.toString());
    }
  }
}
