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

  public int getExperienceReward() {
    int currentLevelExp = getLevel() * 1000
        + (getLevel() - 1) * (getLevel() - 1) * 450;
    int prevLevelExp = (getLevel() - 1 == 0)
        ? 0
        : (getLevel() - 1) * 1000
            + ((getLevel() - 1) - 1) * ((getLevel() - 1) - 1) * 450;

    int expReward = (int) ((currentLevelExp - prevLevelExp)
        * (Math.random() * 5 + 18) / 100); // 18% to 23% of the level's exp

    return expReward;
  }

  public String toString() {
    return "Villain\t{" +
        "name='" + getName() + '\'' +
        ", level=" + getLevel() +
        ", attack=" + getAttack() +
        ", defense=" + getDefense() +
        ", hp=" + getHitPoints() +
        ", weapon=" + getWeapon().getFormattedName() +
        ", armor=" + getArmor().getFormattedName() +
        ", helm=" + getHelm().getFormattedName() +
        ", at (" + getXCoord() + ", " + getYCoord() + ")" +
        '}';
  }
}
