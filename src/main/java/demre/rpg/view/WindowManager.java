package demre.rpg.view;

import demre.rpg.model.characters.Hero;

public interface WindowManager {
  void showSelectHero();

  void showNewHero();

  void showSelectMission(Hero hero);
}
