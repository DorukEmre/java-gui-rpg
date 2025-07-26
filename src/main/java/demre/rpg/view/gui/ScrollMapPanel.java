package demre.rpg.view.gui;

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
    super();

    this.gameEngine = gameEngine;
    this.mapDrawPanel = new MapDrawPanel(controller, gameEngine);

    setViewportView(this.mapDrawPanel);

    System.out.println(
        "ScrollMapPanel > Initialising scrollable map view panel...");
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

    getViewport().setViewPosition(new java.awt.Point(scrollX, scrollY));

    System.out.println("MapViewPanel > Centering map on hero at: " + heroX + ", " + heroY);
    System.out.println("MapViewPanel > Hero position in pixels: " + heroPx + ", " + heroPy);
    System.out.println("MapViewPanel > Scroll position set to: " + scrollX + ", " + scrollY);
    System.out.println("MapViewPanel > Viewport size: " + viewWidth + "x" + viewHeight);
    System.out.println("MapViewPanel > Map size: " + mapDrawPanel.getWidth() + "x" + mapDrawPanel.getHeight());
  }

}
