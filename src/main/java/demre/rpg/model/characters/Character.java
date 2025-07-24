package demre.rpg.model.characters;

import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Weapon;

public interface Character {

  // Getters

  String getName();

  int getAttack();

  int getDefense();

  int getHitPoints();

  int getLevel();

  Weapon getWeapon();

  Armor getArmor();

  Helm getHelm();

  int getXCoord();

  int getYCoord();

  // Setters

  void setName(String name);

  void setAttack(int attack);

  void setDefense(int defense);

  void setHitPoints(int hitPoints);

  void setLevel(int level);

  void setWeapon(Weapon weapon);

  void setArmor(Armor armor);

  void setHelm(Helm helm);

  void setXCoord(int xCoord);

  void setYCoord(int yCoord);
}