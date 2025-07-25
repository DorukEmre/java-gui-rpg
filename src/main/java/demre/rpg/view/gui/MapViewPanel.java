package demre.rpg.view.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import demre.rpg.Main;
import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.view.GUIView;

public class MapViewPanel extends JPanel {
  private final GameController controller;
  private final GameEngine gameEngine;

  public MapViewPanel(GameController controller, GameEngine gameEngine) {
    this.controller = controller;
    this.gameEngine = gameEngine;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    MapDrawPanel mapDrawPanel = new MapDrawPanel(controller, gameEngine);
    JScrollPane scrollPane = new JScrollPane(mapDrawPanel);

    // Dimension preferredSize = mapDrawPanel.getPreferredSize();
    // scrollPane.setPreferredSize(preferredSize);
    // scrollPane.setMinimumSize(preferredSize);
    // scrollPane.setMaximumSize(new Dimension(preferredSize.width + 30,
    // preferredSize.height + 30));

    add(scrollPane);

    JPanel directionPanel = createDirectionPanel();
    add(Box.createVerticalStrut(20));
    add(directionPanel);

    SwingUtilities.invokeLater(() -> centerMapOnHero(scrollPane, mapDrawPanel));

    // Add label with mapDrawPanel size, scrollPane size, and directionPanel size
    JLabel sizeLabel = new JLabel();
    sizeLabel.setFont(new Font("Serif", Font.PLAIN, 16));
    sizeLabel.setAlignmentX(CENTER_ALIGNMENT);
    add(sizeLabel);

    // Helper to update the label
    Runnable updateSizeLabel = () -> sizeLabel.setText(
        "Map size: " + mapDrawPanel.getWidth() + "x" + mapDrawPanel.getHeight() +
            ", ScrollPane size: " + scrollPane.getWidth() + "x" + scrollPane.getHeight() +
            ", DirectionPanel size: " + directionPanel.getWidth() + "x" + directionPanel.getHeight());
    // Add listeners to update the label on resize
    scrollPane.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent e) {
        updateSizeLabel.run();
      }
    });
    mapDrawPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent e) {
        updateSizeLabel.run();
      }
    });
    directionPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent e) {
        updateSizeLabel.run();
      }
    });
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
      public void componentResized(java.awt.event.ComponentEvent e) {
        updateSizeLabel.run();
      }
    });

    // Set initial text
    updateSizeLabel.run();

    // add(Box.createVerticalStrut(20));

    // GameEngine.Step step = gameEngine.getStep();
    // if (step == GameEngine.Step.INVALID_ACTION) {
    // JLabel invalidActionLabel = new JLabel("Invalid action. Please try again.");
    // invalidActionLabel.setFont(new Font("Serif", Font.PLAIN, 16));
    // invalidActionLabel.setAlignmentX(CENTER_ALIGNMENT);
    // add(invalidActionLabel);
    // add(Box.createVerticalStrut(20));
    // } else if (step == GameEngine.Step.ENEMY_FIGHT_SUCCESS) {
    // JLabel fightSuccessLabel = new JLabel("You defeated the enemy!");
    // fightSuccessLabel.setFont(new Font("Serif", Font.PLAIN, 16));
    // fightSuccessLabel.setAlignmentX(CENTER_ALIGNMENT);
    // add(fightSuccessLabel);
    // add(Box.createVerticalStrut(20));
    // } else if (step == GameEngine.Step.LEVEL_UP) {
    // JLabel levelUpLabel = new JLabel("You defeated the enemy and leveled up!");
    // levelUpLabel.setFont(new Font("Serif", Font.PLAIN, 16));
    // levelUpLabel.setAlignmentX(CENTER_ALIGNMENT);
    // add(levelUpLabel);
    // add(Box.createVerticalStrut(20));
    // JLabel levelInfoLabel = new JLabel("You are now level "
    // + gameEngine.getHero().getLevel() + " with "
    // + gameEngine.getHero().getExperience() + " experience points.");
    // levelInfoLabel.setFont(new Font("Serif", Font.PLAIN, 16));
    // levelInfoLabel.setAlignmentX(CENTER_ALIGNMENT);
    // add(levelInfoLabel);
    // add(Box.createVerticalStrut(20));
    // } else if (step == GameEngine.Step.ENEMY_RUN_SUCCESS) {
    // JLabel runSuccessLabel = new JLabel("You successfully ran away from the
    // enemy!");
    // runSuccessLabel.setFont(new Font("Serif", Font.PLAIN, 16));
    // runSuccessLabel.setAlignmentX(CENTER_ALIGNMENT);
    // add(runSuccessLabel);
    // add(Box.createVerticalStrut(20));
    // }
    // JLabel actionPromptLabel = new JLabel(
    // "(N)orth, (S)outh, (E)ast, (W)est, (i)nfo or 'exit'.");
    // actionPromptLabel.setFont(new Font("Serif", Font.PLAIN, 16));
    // actionPromptLabel.setAlignmentX(CENTER_ALIGNMENT);
    // add(actionPromptLabel);
    // add(Box.createVerticalGlue());
  }

  private JPanel createDirectionPanel() {
    JPanel directionPanel = new JPanel();
    directionPanel.setLayout(new GridLayout(3, 3));
    directionPanel.setMaximumSize(new Dimension(96, 96)); // 3*32
    directionPanel.setPreferredSize(new Dimension(96, 96));

    // Dummy buttons
    JButton dummy1 = newDummyButton();
    JButton dummy2 = newDummyButton();
    JButton dummy3 = newDummyButton();
    JButton dummy4 = newDummyButton();
    JButton dummy5 = newDummyButton();

    // Direction buttons
    JButton northButton = newDirectionButton("N");
    JButton southButton = newDirectionButton("S");
    JButton eastButton = newDirectionButton("E");
    JButton westButton = newDirectionButton("W");

    // Add buttons in compass arrangement
    directionPanel.add(dummy1); // (0,0)
    directionPanel.add(northButton); // (0,1)
    directionPanel.add(dummy2); // (0,2)
    directionPanel.add(westButton); // (1,0)
    directionPanel.add(dummy3); // (1,1) center
    directionPanel.add(eastButton); // (1,2)
    directionPanel.add(dummy4); // (2,0)
    directionPanel.add(southButton); // (2,1)
    directionPanel.add(dummy5); // (2,2)

    return directionPanel;
  }

  private JButton newDummyButton() {
    JButton button = new JButton();
    button.setPreferredSize(new Dimension(32, 32));
    button.setMargin(new Insets(0, 0, 0, 0));
    button.setEnabled(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);

    return button;
  }

  private JButton newDirectionButton(String direction) {
    JButton button = new JButton(direction);
    button.setPreferredSize(new Dimension(32, 32));
    button.setMargin(new Insets(0, 0, 0, 0));

    button.addActionListener(e -> {
      try {
        controller.onMapInputContinue(direction);
      } catch (Exception ex) {
        GUIView.windowDispose(button);
        Main.errorAndExit(ex, ex.getMessage());
      }
    });

    return button;
  }

  private void centerMapOnHero(JScrollPane scrollPane, MapDrawPanel mapDrawPanel) {
    int tileSize = mapDrawPanel.getTileSize();
    int heroX = gameEngine.getHero().getXCoord() + 1;
    int heroY = gameEngine.getHero().getYCoord() + 1;

    int viewWidth = scrollPane.getViewport().getWidth();
    int viewHeight = scrollPane.getViewport().getHeight();

    int heroPx = heroX * tileSize;
    int heroPy = heroY * tileSize;

    int scrollX = heroPx - viewWidth / 2 + tileSize / 2;
    int scrollY = heroPy - viewHeight / 2 + tileSize / 2;

    scrollX = Math.max(0, scrollX);
    scrollY = Math.max(0, scrollY);

    scrollPane.getViewport().setViewPosition(new java.awt.Point(scrollX, scrollY));

    System.out.println("MapViewPanel > Centering map on hero at: " + heroX + ", " + heroY);
    System.out.println("MapViewPanel > Hero position in pixels: " + heroPx + ", " + heroPy);
    System.out.println("MapViewPanel > Scroll position set to: " + scrollX + ", " + scrollY);
    System.out.println("MapViewPanel > Viewport size: " + viewWidth + "x" + viewHeight);
    System.out.println("MapViewPanel > Map size: " + mapDrawPanel.getWidth() + "x" + mapDrawPanel.getHeight());
  }
}
