package demre.rpg.model.items;

public class Helm extends Item {
  // increases hit points

  public Helm(String name, int modifier) {
    super(Type.HELM, name, modifier);
  }
}
