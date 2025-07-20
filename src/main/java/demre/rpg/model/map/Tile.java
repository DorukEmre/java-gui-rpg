package demre.rpg.model.map;

public class Tile {
  private final int x;
  private final int y;
  private String type; // e.g., "Grass", "Wall", "Hero", "Enemy"
  private String symbol; // e.g., ".", "#", "@", "X"
  private Boolean isVisible;

  // Constructors

  public Tile(int x, int y, String type, String symbol) {
    this.x = x;
    this.y = y;
    this.type = type;
    this.symbol = symbol;
    this.isVisible = false;
  }

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

  // Methods

  public void assignHero() {
    this.type = "Hero";
    this.symbol = "@";
    this.isVisible = true;
  }

  public void assignGrass() {
    this.type = "Grass";
    this.symbol = ".";
  }

}
