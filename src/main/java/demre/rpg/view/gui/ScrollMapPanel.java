package demre.rpg.view.gui;

import java.awt.Point;

import javax.swing.JScrollPane;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.view.GUIView;

/**
 * Creates a scrollable map view panel.
 */
public class ScrollMapPanel extends JScrollPane {
  private final GameEngine gameEngine;
  private final GUIView guiView;

  public ScrollMapPanel(
      GUIView guiView, GameController controller, GameEngine gameEngine) {

    this.gameEngine = gameEngine;
    this.guiView = guiView;

    GameEngine.Step step = gameEngine.getStep();
    MapDrawPanel mapDrawPanel;

    if (step == GameEngine.Step.PLAYING
        || step == GameEngine.Step.INVALID_ACTION
        || step == GameEngine.Step.ENEMY_FIGHT_SUCCESS
        || step == GameEngine.Step.LEVEL_UP
        || step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
      mapDrawPanel = new MapDrawPanel(
          guiView, controller, gameEngine, true);
    } else {
      mapDrawPanel = new MapDrawPanel(
          guiView, controller, gameEngine, false);
    }

    setViewportView(mapDrawPanel);

  }

  /**
   * Centers the map on the hero's position within the scrollable panel.
   */
  protected void centerMapOnHero() {
    int tileSize = guiView.tileButtonsMap.getTileSize();
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
