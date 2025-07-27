package demre.rpg.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JPanel;

import demre.rpg.Main;
import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.map.Tile;
import demre.rpg.view.GUIView;

/**
 * GUI component that displays the game map.
 * It shows the tiles of the map and allows interaction with them.
 */
public class MapDrawPanel extends JPanel {
  private final int side;
  private final JButton[][] tileButtons;
  private final GameController controller;
  protected int tileSize;

  public MapDrawPanel(
      GameController controller, GameEngine gameEngine, boolean isInteractive) {
    System.out.println("MapDrawPanel > Initialising map panel...");

    this.controller = controller;
    this.side = gameEngine.getMapSize() + 2;
    this.tileSize = calculateTileSize();
    this.tileButtons = new JButton[side][side];

    System.out.println(
        "MapDrawPanel > Map size: " + side + "x" + side
            + ", Dimension(side * tileSize): " + (side * tileSize));

    setLayout(new GridBagLayout());

    JPanel gridPanel = new JPanel(null);
    gridPanel.setPreferredSize(new Dimension(side * tileSize, side * tileSize));

    Tile[][] map = gameEngine.getMap();

    // Create map tiles
    for (int y = 0; y < side; y++) {
      for (int x = 0; x < side; x++) {
        Tile tile = map[y][x];
        JButton tileButton = new JButton();

        if (tile.isVisible()) {
          tileButton.setText(tile.getSymbol());
        } else {
          tileButton.setText(" ");
        }

        tileButton.setEnabled(tile.isVisible());
        tileButton.setFont(new Font("Monospaced", Font.PLAIN, 16));
        tileButton.setFocusable(false);
        tileButton.setMargin(new Insets(0, 0, 0, 0));
        tileButton.setBounds(x * tileSize, y * tileSize, tileSize, tileSize);

        tileButtons[y][x] = tileButton;
        gridPanel.add(tileButton);
      }
    }

    // Enable Hero tile
    int heroX = gameEngine.getHero().getXCoord() + 1;
    int heroY = gameEngine.getHero().getYCoord() + 1;
    tileButtons[heroY][heroX].setEnabled(true);
    tileButtons[heroY][heroX].setBackground(Color.pink);

    // Enable tiles around Hero if map is interactive
    if (isInteractive) {
      if (heroX > 0)
        enableTile(tileButtons[heroY][heroX - 1], "West");
      if (heroX < side - 1)
        enableTile(tileButtons[heroY][heroX + 1], "East");
      if (heroY > 0)
        enableTile(tileButtons[heroY - 1][heroX], "North");
      if (heroY < side - 1)
        enableTile(tileButtons[heroY + 1][heroX], "South");
    }

    // Center the gridPanel
    add(gridPanel, new GridBagConstraints());

  }

  // Getters

  protected int getTileSize() {
    return tileSize;
  }

  // Methods

  private void enableTile(JButton tileButton, String direction) {
    // Enable the tile button and set its action
    tileButton.setEnabled(true);
    tileButton.setBackground(new Color(255, 230, 240)); // Light pink background
    tileButton.addActionListener(e -> {
      try {
        controller.onMapInputContinue(direction);
      } catch (Exception ex) {
        GUIView.windowDispose(tileButton);
        Main.errorAndExit(ex, ex.getMessage());
      }
    });
  }

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
