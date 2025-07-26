package demre.rpg.model.items;

public class Weapon extends Item {
  // increases the attack

  public Weapon(String name, int modifier) {
    super(Type.WEAPON, name, modifier);
  }

}
