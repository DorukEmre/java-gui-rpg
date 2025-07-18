package demre.rpg.model.characters;

import jakarta.validation.constraints.NotNull;

import demre.rpg.model.artifacts.Armor;
import demre.rpg.model.artifacts.Weapon;

public abstract class Hero extends Character {
  @NotNull
  private Weapon weapon;
  @NotNull
  private Armor armor;

  int level;
  int experience;

}
