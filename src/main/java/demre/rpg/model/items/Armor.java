package demre.rpg.model.items;

public class Armor extends Item {
  // increases defense

  public Armor(String name, int modifier) {
    super(Type.ARMOR, name, modifier);
  }

}
