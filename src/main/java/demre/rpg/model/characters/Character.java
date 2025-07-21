package demre.rpg.model.characters;

import demre.rpg.model.items.Armor;
import demre.rpg.model.items.Helm;
import demre.rpg.model.items.Weapon;
import jakarta.validation.constraints.NotNull;

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

  void setName(@NotNull String name);

  void setAttack(@NotNull int attack);

  void setDefense(@NotNull int defense);

  void setHitPoints(@NotNull int hitPoints);

  void setLevel(@NotNull int level);

  void setWeapon(@NotNull Weapon weapon);

  void setArmor(@NotNull Armor armor);

  void setHelm(@NotNull Helm helm);

  void setXCoord(@NotNull int xCoord);

  void setYCoord(@NotNull int yCoord);
}