package demre.rpg.view.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JPanel;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.map.Tile;
import demre.rpg.view.GUIView;

/**
 * GUI component that displays the game map.
 * It shows the tiles of the map and allows interaction with them.
 */
public class MapDrawPanel extends JPanel {
  private final GameEngine gameEngine;
  private final int side;
  private final ButtonsMap tileButtonsMap;

  public MapDrawPanel(
      GUIView guiView,
      GameController controller,
      GameEngine gameEngine, boolean isInteractive) {
    this.tileButtonsMap = guiView.tileButtonsMap;
    this.gameEngine = gameEngine;
    this.side = gameEngine.getMapSize() + 2;

    createTileButtonsMap(isInteractive);

  }

  private void createTileButtonsMap(boolean isInteractive) {

    int tileSize = calculateTileSize();

    tileButtonsMap.setButtonsMap(side);
    tileButtonsMap.setTileSize(tileSize);

    this.removeAll();
    setLayout(new GridBagLayout());

    JPanel gridPanel = new JPanel(null);
    gridPanel.setPreferredSize(new Dimension(side * tileSize, side * tileSize));

    Tile[][] tilesMap = gameEngine.getMap();

    // Create map tiles
    for (int y = 0; y < side; y++) {
      for (int x = 0; x < side; x++) {
        Tile tile = tilesMap[y][x];
        JButton tileButton = new JButton();

        if (tile.isVisible()) {
          switch (tile.getType()) {
            case HERO:
              tileButtonsMap.setHeroButton(tileButton);
              break;
            case ENEMY:
              tileButtonsMap.setEnemyButton(tileButton);
              break;
            case GRASS:
              tileButtonsMap.setGrassButton(tileButton);
              break;
            case BORDER:
              tileButtonsMap.setBorderButton(tileButton);
              break;
            default:
              tileButtonsMap.setGrassButton(tileButton);
          }
        } else {
          tileButtonsMap.setFogButton(tileButton);
        }

        tileButton.setEnabled(tile.isVisible());
        tileButton.setFont(new Font("Monospaced", Font.PLAIN, 16));
        tileButton.setFocusable(false);
        tileButton.setMargin(new Insets(0, 0, 0, 0));
        tileButton.setBounds(x * tileSize, y * tileSize, tileSize, tileSize);

        tileButtonsMap.setMapButton(x, y, tileButton);
        gridPanel.add(tileButton);
      }
    }

    // Enable tiles around Hero if map is interactive
    if (isInteractive) {
      int heroX = gameEngine.getHero().getXCoord() + 1;
      int heroY = gameEngine.getHero().getYCoord() + 1;
      tileButtonsMap.enableSurroundingTiles(heroX, heroY);
    }

    // Center the gridPanel
    add(gridPanel, new GridBagConstraints());

    this.revalidate();
    this.repaint();

  }

  // Helper

  private int calculateTileSize() {
    // Calculate tile size based on screen resolution
    int tileSize;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    if ((screenSize.height - 200) / side >= 32) {
      tileSize = 32;
    } else if ((screenSize.height - 200) / side >= 24) {
      tileSize = 24;
    } else {
      tileSize = 16;
    }

    return tileSize;
  }

}
