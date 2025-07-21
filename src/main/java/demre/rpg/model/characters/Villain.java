package demre.rpg.model.characters;

import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Weapon;

public class Villain extends AbstractCharacter {

  public Villain(
      int attack, int defense, int hitPoints, int level,
      Weapon weapon, Armor armor, Helm helm, int x, int y) {

    super("Bandit", attack, defense, hitPoints, level, weapon, armor, helm, x, y);
  }

  public String toString() {
    return "Villain\t{" +
        "name='" + getName() + '\'' +
        ", level=" + getLevel() +
        ", attack=" + getAttack() +
        ", defense=" + getDefense() +
        ", hp=" + getHitPoints() +
        ", weapon=" + getWeapon().getName() +
        ", +" + getWeapon().getModifier() +
        ", armor=" + getArmor().getName() +
        ", +" + getArmor().getModifier() +
        ", helm=" + getHelm().getName() +
        ", +" + getHelm().getModifier() +
        ", at (" + getXCoord() + ", " + getYCoord() + ")" +
        '}';
  }
}
