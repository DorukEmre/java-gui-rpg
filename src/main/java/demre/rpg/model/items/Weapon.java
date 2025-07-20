package demre.rpg.model.items;

public class Weapon extends Item {
  // increases the attack

  public Weapon(String name) {
    super("Weapon", name, 2);
  }

  public Weapon(String name, int modifier) {
    super("Weapon", name, modifier);
  }

}
