package demre.rpg.model.map;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Tile {
  public enum Type {
    GRASS, BORDER, HERO, ENEMY
  }

  @Min(0)
  private final int x;

  @Min(0)
  private final int y;

  @NotNull
  private Type type;

  @NotBlank
  private String symbol; // e.g., ".", "#", "@", "X"

  @NotNull
  private Boolean isVisible;

  // Constructors

  public Tile(int x, int y, Type type, String symbol, Boolean isVisible) {
    this.x = x;
    this.y = y;
    this.type = type;
    this.symbol = symbol;
    this.isVisible = isVisible;
  }

  // Getters

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public Type getType() {
    return type;
  }

  public String getSymbol() {
    return symbol;
  }

  public Boolean isVisible() {
    return isVisible;
  }

  // Setters

  public void setType(Type type) {
    this.type = type;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public void setVisible(Boolean visible) {
    this.isVisible = visible;
  }

  public void assignHero() {
    this.type = Type.HERO;
    this.symbol = "@";
    this.isVisible = true;
  }

  public void assignGrass() {
    this.type = Type.GRASS;
    this.symbol = ".";
  }

  public void assignEnemy() {
    this.type = Type.ENEMY;
    this.symbol = "X";
  }
}
