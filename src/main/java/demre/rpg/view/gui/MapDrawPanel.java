package demre.rpg.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

  public MapDrawPanel(GameController controller, GameEngine gameEngine) {
    this.controller = controller;
    System.out.println("MapPanel > Initialising map panel...");

    this.side = gameEngine.getMapSize() + 2;
    Tile[][] map = gameEngine.getMap();
    int heroX = gameEngine.getHero().getXCoord() + 1;
    int heroY = gameEngine.getHero().getYCoord() + 1;

    System.out.println("GUI MapPanel > Map size: " + side + "x" + side);

    setLayout(new GridLayout(side, side));
    tileButtons = new JButton[side][side];

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
        tileButton.setAlignmentX(CENTER_ALIGNMENT);
        tileButton.setFocusable(false);
        tileButton.setPreferredSize(new Dimension(32, 32));
        tileButton.setMargin(new Insets(0, 0, 0, 0));

        tileButtons[y][x] = tileButton;
        add(tileButton);
      }
    }

    // Enable Hero tile
    tileButtons[heroY][heroX].setEnabled(true);
    tileButtons[heroY][heroX].setBackground(Color.pink);

    // Enable tiles around Hero
    if (heroX > 0)
      enableTile(tileButtons[heroY][heroX - 1], "West");
    if (heroX < side - 1)
      enableTile(tileButtons[heroY][heroX + 1], "East");
    if (heroY > 0)
      enableTile(tileButtons[heroY - 1][heroX], "North");
    if (heroY < side - 1)
      enableTile(tileButtons[heroY + 1][heroX], "South");

  }

  private void enableTile(JButton tileButton, String direction) {
    tileButton.setEnabled(true);
    tileButton.setBackground(Color.YELLOW);
    tileButton.addActionListener(e -> {
      try {
        controller.onMapInputContinue(direction);
      } catch (Exception ex) {
        GUIView.windowDispose(tileButton);
        Main.errorAndExit(ex, ex.getMessage());
      }
    });
  }

  @Override
  public Dimension getPreferredSize() {
    int max = 400;
    Dimension parent = getParent() != null
        ? getParent().getSize()
        : new Dimension(max, max);
    int size = Math.min(Math.min(parent.width, parent.height), max);
    return new Dimension(size, size);
  }

  @Override
  public void doLayout() {
    // Ensure each button is square and fills the available square area
    int size = Math.min(getWidth(), getHeight());
    int tileSize = size / side;
    for (int i = 0; i < side; i++) {
      for (int j = 0; j < side; j++) {
        JButton btn = tileButtons[i][j];
        btn.setBounds(j * tileSize, i * tileSize, tileSize, tileSize);
      }
    }
  }

}
