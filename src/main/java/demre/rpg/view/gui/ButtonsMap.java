package demre.rpg.view.gui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import demre.rpg.Main;
import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.GameEngine.Direction;
import demre.rpg.model.map.Tile;
import demre.rpg.view.GUIView;

public class ButtonsMap {
  private final GameEngine gameEngine;
  private final GameController controller;
  private JButton[][] buttonsMap;
  private int tileSize;

  private ImageIcon heroFrontIcon;
  private ImageIcon heroBackIcon;
  private ImageIcon heroLeftIcon;
  private ImageIcon heroRightIcon;
  private ImageIcon enemyIcon;
  private ImageIcon grassIcon;
  private ImageIcon borderIcon;
  private ImageIcon fogIcon;

  // Tile icons
  private final Image heroFront = new ImageIcon(
      getClass().getResource("/icons/heroFront82.png")).getImage();
  private final Image heroBack = new ImageIcon(
      getClass().getResource("/icons/heroBack82.png")).getImage();
  private final Image heroLeft = new ImageIcon(
      getClass().getResource("/icons/heroLeft82.png")).getImage();
  private final Image heroRight = new ImageIcon(
      getClass().getResource("/icons/heroRight82.png")).getImage();
  private final Image enemyImage = new ImageIcon(
      getClass().getResource("/icons/monster85.png")).getImage();
  private final Image grassImage = new ImageIcon(
      getClass().getResource("/icons/grass96.png")).getImage();
  private final Image borderImage = new ImageIcon(
      getClass().getResource("/icons/border96.png")).getImage();
  private final Image fogImage = new ImageIcon(
      getClass().getResource("/icons/fog96.png")).getImage();

  // Constructors

  public ButtonsMap(GameEngine gameEngine, GameController controller) {
    this.gameEngine = gameEngine;
    this.controller = controller;
    this.buttonsMap = new JButton[][] {};
  }

  // Getters

  protected int getTileSize() {
    return tileSize;
  }

  protected JButton[][] getButtonsMap() {
    return buttonsMap;
  }

  protected JButton getMapButton(int x, int y) {
    return buttonsMap[y][x];
  }

  protected JButton getMapButton(Tile tile) {
    return getMapButton(tile.getX(), tile.getY());
  }

  // Setters

  protected void setTileSize(int tileSize) {
    this.tileSize = tileSize;
    initialiseScaledIcons();
  }

  protected void setButtonsMap(int side) {
    this.buttonsMap = new JButton[side][side];
  }

  protected void setMapButton(int x, int y, JButton button) {
    buttonsMap[y][x] = button;
  }

  // Methods and enable/disable tiles

  private void enableHeroAdjacentTile(JButton button, String direction) {
    // Enable the tile button and set its action
    button.setEnabled(true);
    button.setBackground(new Color(255, 230, 240)); // Light pink background
    button.addActionListener(e -> {
      try {
        controller.onMapInputContinue(direction);
      } catch (Exception ex) {
        GUIView.windowDispose(button);
        Main.errorAndExit(ex, ex.getMessage());
      }
    });
  }

  private void enableHeroAdjacentTile(int x, int y, String direction) {
    enableHeroAdjacentTile(getMapButton(x, y), direction);
  }

  protected void enableSurroundingTiles(int heroX, int heroY) {
    if (heroX > 0)
      enableHeroAdjacentTile(heroX - 1, heroY, "West");
    if (heroX < buttonsMap.length - 1)
      enableHeroAdjacentTile(heroX + 1, heroY, "East");
    if (heroY > 0)
      enableHeroAdjacentTile(heroX, heroY - 1, "North");
    if (heroY < buttonsMap[0].length - 1)
      enableHeroAdjacentTile(heroX, heroY + 1, "South");
  }

  protected void enableSurroundingTiles(JButton button) {
    enableSurroundingTiles(button.getX() / tileSize, button.getY() / tileSize);
  }

  private void disableTile(int x, int y) {
    // Disable the tile button and remove its action
    JButton button = getMapButton(x, y);

    Tile[][] tileMap = gameEngine.getMap();

    if (!tileMap[y][x].isVisible()) {
      button.setEnabled(false);
    }

    button.setBackground(null);

    for (var listeners : button.getActionListeners()) {
      button.removeActionListener(listeners);
    }

  }

  protected void disableSurroundingTiles(JButton button) {
    int buttonX = button.getX() / tileSize;
    int buttonY = button.getY() / tileSize;

    disableTile(buttonX, buttonY);

    if (buttonX > 0)
      disableTile(buttonX - 1, buttonY);
    if (buttonX < buttonsMap.length - 1)
      disableTile(buttonX + 1, buttonY);
    if (buttonY > 0)
      disableTile(buttonX, buttonY - 1);
    if (buttonY < buttonsMap[0].length - 1)
      disableTile(buttonX, buttonY + 1);
  }

  // Icons

  protected void setHeroButton(JButton button) {
    Direction direction = gameEngine.getMoveDirection();
    if (direction == Direction.NORTH) {
      button.setIcon(heroBackIcon);
    } else if (direction == Direction.SOUTH) {
      button.setIcon(heroFrontIcon);
    } else if (direction == Direction.WEST) {
      button.setIcon(heroLeftIcon);
    } else if (direction == Direction.EAST) {
      button.setIcon(heroRightIcon);
    } else {
      button.setIcon(heroFrontIcon); // Default icon
    }
    button.setText("");
    button.setBackground(new Color(250, 218, 128)); // Light yellow background
    button.setEnabled(true);
  }

  protected void setGrassButton(JButton button) {
    button.setIcon(grassIcon);
    button.setText("");
    button.setBackground(null);
  }

  protected void setBorderButton(JButton button) {
    button.setIcon(borderIcon);
    button.setText("");
    button.setBackground(null);
  }

  protected void setEnemyButton(JButton button) {
    button.setIcon(enemyIcon);
    button.setText("");
    button.setBackground(null);
  }

  protected void setFogButton(JButton button) {
    button.setIcon(fogIcon);
    button.setText("");
  }

  private void initialiseScaledIcons() {

    this.heroFrontIcon = createScaledIcon(heroFront, tileSize);
    this.heroBackIcon = createScaledIcon(heroBack, tileSize);
    this.heroLeftIcon = createScaledIcon(heroLeft, tileSize);
    this.heroRightIcon = createScaledIcon(heroRight, tileSize);
    this.enemyIcon = createScaledIcon(enemyImage, tileSize);
    this.grassIcon = createScaledIcon(grassImage, tileSize);
    this.borderIcon = createScaledIcon(borderImage, tileSize);
    this.fogIcon = createScaledIcon(fogImage, tileSize);
  }

  private ImageIcon createScaledIcon(Image image, int size) {
    return new ImageIcon(
        image.getScaledInstance(size, size, Image.SCALE_SMOOTH));
  }

}
