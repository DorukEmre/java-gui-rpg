package demre.rpg.model.characters;

import jakarta.validation.constraints.NotNull;

public interface Character {

  // Getters

  String getName();

  int getAttack();

  int getDefense();

  int getHitPoints();

  int getLevel();

  int getXCoord();

  int getYCoord();

  // Setters

  void setName(@NotNull String name);

  void setAttack(@NotNull int attack);

  void setDefense(@NotNull int defense);

  void setHitPoints(@NotNull int hitPoints);

  void setLevel(@NotNull int level);

  void setXCoord(@NotNull int xCoord);

  void setYCoord(@NotNull int yCoord);
}