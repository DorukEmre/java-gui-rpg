package demre.rpg.model.factories;

import java.util.Set;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import demre.rpg.model.map.Tile;

public class TileFactory {

  private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
  private static final Validator validator = factory.getValidator();

  public static Tile createTile(
      int x, int y, Tile.Type type, Boolean isVisible) {
    Tile tile = new Tile(x, y, type, getSymbol(type), isVisible);
    validateTile(tile);
    return tile;
  }

  private static String getSymbol(Tile.Type type) {
    switch (type) {
      case HERO:
        return "@";
      case ENEMY:
        return "X";
      case BORDER:
        return "#";
      case GRASS:
        return ".";
      default:
        throw new IllegalArgumentException("Unknown tile type: " + type);
    }
  }

  private static void validateTile(Tile tile) {
    Set<ConstraintViolation<Tile>> violations = validator.validate(tile);

    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();

      for (ConstraintViolation<Tile> violation : violations) {
        sb.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
      }
      throw new IllegalArgumentException(
          "Validation error. Tile: " + sb.toString());
    }
  }
}