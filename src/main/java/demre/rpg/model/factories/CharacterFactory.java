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
