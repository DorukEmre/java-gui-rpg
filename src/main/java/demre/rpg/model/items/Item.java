package demre.rpg.model.items;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public abstract class Item {
  public enum Type {
    WEAPON, HELM, ARMOR
  }

  @NotNull
  private Type type;

  @NotBlank
  private String name;

  @Min(0)
  private int modifier;

  protected Item(Type type, String name, int modifier) {
    this.name = name;
    this.type = type;
    this.modifier = modifier;
  }

  // Getters

  public Type getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public int getModifier() {
    return modifier;
  }

  // Methods

  public String getFormattedName() {
    // Format: "name +modifier" e.g., "Sword +5"
    return (name + " +" + modifier);
  }

  public String toString() {
    // Format: "type: name +modifier" e.g., "Weapon: Sword +5"
    return (type + ": " + name + " +" + modifier);
  }

}
