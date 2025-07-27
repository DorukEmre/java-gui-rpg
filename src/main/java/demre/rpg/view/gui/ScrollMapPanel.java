package demre.rpg.view.gui;

import java.awt.Point;

import javax.swing.JScrollPane;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

/**
 * Creates a scrollable map view panel.
 */
public class ScrollMapPanel extends JScrollPane {
  private final GameEngine gameEngine;
  private final MapDrawPanel mapDrawPanel;

  public ScrollMapPanel(GameController controller, GameEngine gameEngine) {
    this.gameEngine = gameEngine;

    GameEngine.Step step = gameEngine.getStep();
    if (step == GameEngine.Step.PLAYING
        || step == GameEngine.Step.INVALID_ACTION
        || step == GameEngine.Step.ENEMY_FIGHT_SUCCESS
        || step == GameEngine.Step.LEVEL_UP
        || step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
      this.mapDrawPanel = new MapDrawPanel(
          controller, gameEngine, true);
    } else {
      this.mapDrawPanel = new MapDrawPanel(
          controller, gameEngine, false);
    }

    setViewportView(this.mapDrawPanel);

    System.out.println(
        "ScrollMapPanel > Initialised scrollable map view panel.");
  }

  protected void centerMapOnHero() {
    int tileSize = mapDrawPanel.getTileSize();
    int heroX = gameEngine.getHero().getXCoord() + 1;
    int heroY = gameEngine.getHero().getYCoord() + 1;

    int viewWidth = getViewport().getWidth();
    int viewHeight = getViewport().getHeight();

    int heroPx = heroX * tileSize;
    int heroPy = heroY * tileSize;

    int scrollX = heroPx - viewWidth / 2 + tileSize / 2;
    int scrollY = heroPy - viewHeight / 2 + tileSize / 2;

    scrollX = Math.max(0, scrollX);
    scrollY = Math.max(0, scrollY);

    getViewport().setViewPosition(new Point(scrollX, scrollY));

  }

}
