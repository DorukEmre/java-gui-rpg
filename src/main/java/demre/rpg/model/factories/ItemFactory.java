package demre.rpg.model.factories;

import java.util.Set;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import demre.rpg.model.items.Item;
import demre.rpg.model.items.Weapon;
import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;

public class ItemFactory {
  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = factory.getValidator();

  public static Weapon createWeapon(String name, int modifier) {
    Weapon weapon = new Weapon(name, modifier);
    validateItem(weapon);
    return weapon;
  }

  public static Armor createArmor(String name, int modifier) {
    Armor armor = new Armor(name, modifier);
    validateItem(armor);
    return armor;
  }

  public static Helm createHelm(String name, int modifier) {
    Helm helm = new Helm(name, modifier);
    validateItem(helm);
    return helm;
  }

  private static void validateItem(Item item) {
    Set<ConstraintViolation<Item>> violations = validator.validate(item);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

}
