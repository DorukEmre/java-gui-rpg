package demre.rpg.model.characters;

public class Villain extends AbstractCharacter {

  public Villain(
      int attack, int defense, int hitPoints, int level, int x, int y) {

    super("Bandit", attack, defense, hitPoints, level, x, y);
  }

  public String toString() {
    return "Villain\t{" +
        "name='" + getName() + '\'' +
        ", level=" + getLevel() +
        ", attack=" + getAttack() +
        ", defense=" + getDefense() +
        ", hp=" + getHitPoints() +
        ", at (" + getXCoord() + ", " + getYCoord() + ")" +
        '}';
  }
}
