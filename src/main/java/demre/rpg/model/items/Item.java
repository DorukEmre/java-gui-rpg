package demre.rpg.model.items;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public abstract class Item {

  @NotBlank
  private String type; // e.g., ""Weapon", "Helm", "Armor"

  @NotBlank
  private String name;

  @Min(0)
  @NotNull
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

  // Methods

  public String toString() {
    // Format: "type: name +modifier" e.g., "Weapon: Sword +5"
    return (type + ": " + name + " +" + modifier);
  }

}
