package demre.rpg.model.items;

public class Helm extends Item {
  // increases hit points

  public Helm(String name) {
    super("Helm", name, 2);
  }

  public Helm(String name, int modifier) {
    super("Helm", name, modifier);
  }
}
