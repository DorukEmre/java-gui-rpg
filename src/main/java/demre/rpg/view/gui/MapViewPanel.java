package demre.rpg.view.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.map.Tile;
import demre.rpg.view.GUIView;

public class MapViewPanel extends JPanel {

  private static final Logger logger = LoggerFactory.getLogger(MapViewPanel.class);

  protected final GameController controller;
  protected final GameEngine gameEngine;
  protected final GUIView guiView;

  public MapViewPanel(
      GUIView guiView, GameController controller, GameEngine gameEngine) {
    this.controller = controller;
    this.gameEngine = gameEngine;
    this.guiView = guiView;

    GameEngine.Step step = gameEngine.getStep();
    logger.info("MapViewPanel > Game step: " + step);

    this.removeAll();
    setLayout(new BorderLayout(0, 20));

    JScrollPane scrollableMap = new ScrollMapPanel(
        guiView, controller, gameEngine);

    JPanel instructionsPanel;
    if (step == GameEngine.Step.PLAYING
        || step == GameEngine.Step.INVALID_ACTION
        || step == GameEngine.Step.ENEMY_FIGHT_SUCCESS
        || step == GameEngine.Step.LEVEL_UP
        || step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
      instructionsPanel = new PlayerControlPanel(controller, gameEngine);

    } else if (step == GameEngine.Step.ENEMY_ENCOUNTER
        || step == GameEngine.Step.ENEMY_INVALID_ACTION
        || step == GameEngine.Step.ENEMY_RUN_FAILURE) {
      instructionsPanel = new EnemyEncounterPanel(controller, gameEngine);

    } else if (step == GameEngine.Step.ITEM_FOUND
        || step == GameEngine.Step.ITEM_FOUND_AND_LEVEL_UP
        || step == GameEngine.Step.ITEM_INVALID_ACTION) {
      instructionsPanel = new ItemFoundPanel(controller, gameEngine);

    } else {
      instructionsPanel = new JPanel(); // Fallback empty panel
    }

    // Set fixed height for instructionsPanel
    instructionsPanel.setPreferredSize(new Dimension(0, 150));

    add(scrollableMap, BorderLayout.CENTER);
    add(instructionsPanel, BorderLayout.SOUTH);

    controller.onMapDisplayed();

    // Center the map on the hero
    SwingUtilities.invokeLater(
        () -> ((ScrollMapPanel) scrollableMap).centerMapOnHero());

  }

  /**
   * Updates the hero's position on the map when the hero moves to a new tile
   * without redrawing the entire map.
   */
  public void updateHeroPosition() {
    logger.info("MapViewPanel > updateHeroPosition() called");

    ButtonsMap tileButtonsMap = guiView.tileButtonsMap;

    Tile[] moveTiles = gameEngine.getMoveTiles();

    // Remove hero icon from old position
    JButton oldButton = tileButtonsMap.getMapButton(moveTiles[0]);
    tileButtonsMap.disableSurroundingTiles(oldButton);
    tileButtonsMap.setGrassButton(oldButton);

    // Set hero icon at new position
    JButton newButton = tileButtonsMap.getMapButton(moveTiles[1]);
    tileButtonsMap.setHeroButton(newButton);
    tileButtonsMap.enableSurroundingTiles(newButton);

    oldButton.repaint();
    newButton.repaint();

    controller.onMapDisplayed();

    // To repaint the whole panel
    // this.revalidate();
    // this.repaint();
  }

}
