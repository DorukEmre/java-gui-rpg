package demre.rpg.model.map;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Tile {

  @Min(0)
  private final int x;

  @Min(0)
  private final int y;

  @NotBlank
  private String type; // e.g., "Grass", "Border", "Hero", "Enemy"

  @NotBlank
  private String symbol; // e.g., ".", "#", "@", "X"

  @NotNull
  private Boolean isVisible;

  // Constructors

  public Tile(int x, int y, String type, String symbol, Boolean isVisible) {
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

  public String getType() {
    return type;
  }

  public String getSymbol() {
    return symbol;
  }

  public Boolean isVisible() {
    return isVisible;
  }

  // Setters

  public void setType(String type) {
    this.type = type;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public void setVisible(Boolean visible) {
    this.isVisible = visible;
  }

  public void assignHero() {
    this.type = "Hero";
    this.symbol = "@";
    this.isVisible = true;
  }

  public void assignGrass() {
    this.type = "Grass";
    this.symbol = ".";
  }

  public void assignEnemy() {
    this.type = "Enemy";
    this.symbol = "X";
  }
}
