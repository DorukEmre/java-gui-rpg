package demre.rpg.model.items;

public abstract class Item {

  private String type; // e.g., ""Weapon", "Helm", "Armor"
  private String name;
  private int modifier;

  protected Item(String type, String name, int modifier) {
    this.name = name;
    this.type = type;
    this.modifier = modifier;
  }

  // Getters

  public String getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public int getModifier() {
    return modifier;
  }

  // Setters

  public void setType(String type) {
    this.type = type;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setModifier(int modifier) {
    this.modifier = modifier;
  }

}
