package demre.rpg.model.items;

public class Armor extends Item {
  // increases defense

  public Armor(String name) {
    super("Armor", name, 2);
  }

  public Armor(String name, int modifier) {
    super("Armor", name, modifier);
  }

}
