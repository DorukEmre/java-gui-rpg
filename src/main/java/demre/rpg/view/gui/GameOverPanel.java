
package demre.rpg.view.gui;

import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public class GameOverPanel extends JPanel {
  private JButton defaultButton;

  private JLabel heroLabel;
  private JLabel enemyLabel;

  private final ImageIcon heroImage = new ImageIcon(
      getClass().getResource("/icons/heroDeadRotated.png"));
  private final ImageIcon enemyImage = new ImageIcon(
      getClass().getResource("/icons/monster.png"));

  public GameOverPanel(GameController controller, GameEngine gameEngine) {

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    add(Box.createVerticalGlue());

    // Add messages
    JLabel titleLabel = GUIUtils.createTitle(
        "You died!", 48);
    add(titleLabel);

    add(Box.createVerticalStrut(20));

    if (gameEngine.getStep() == GameEngine.Step.GAME_OVER_INVALID_ACTION) {
      JLabel label2 = GUIUtils.createInfo(
          "Invalid action. Please try again!", 18);
      add(label2);
    }

    add(Box.createVerticalStrut(20));

    // Add images
    JPanel imagePanel = new JPanel();
    imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
    imagePanel.setAlignmentX(CENTER_ALIGNMENT);

    enemyLabel = new JLabel(enemyImage);
    imagePanel.add(enemyLabel);

    imagePanel.add(Box.createHorizontalStrut(20));

    heroLabel = new JLabel(heroImage);
    imagePanel.add(heroLabel);

    add(imagePanel);

    add(Box.createVerticalStrut(20));

    // Add buttons
    JPanel choice = new JPanel();
    choice.setLayout(new BoxLayout(choice, BoxLayout.X_AXIS));
    choice.setAlignmentX(CENTER_ALIGNMENT);

    {
      JButton button = GUIUtils.createButton("Try again");
      button.setAlignmentX(CENTER_ALIGNMENT);
      GUIUtils.bindButtonToKey(
          choice, button, KeyStroke.getKeyStroke("T"));
      button.addActionListener(
          e -> controller.onGameOverContinue("try"));
      choice.add(button);
    }

    choice.add(Box.createHorizontalStrut(20));

    {
      JButton button = GUIUtils.createButton("Exit");
      button.setAlignmentX(CENTER_ALIGNMENT);
      GUIUtils.bindButtonToKey(
          choice, button, KeyStroke.getKeyStroke("X"));
      button.addActionListener(
          e -> controller.onGameOverContinue("exit"));
      choice.add(button);
    }

    add(choice);

    add(Box.createVerticalGlue());

    defaultButton = (JButton) choice.getComponent(0);
  }

  @Override
  public void addNotify() {
    super.addNotify();
    if (getRootPane() != null) {
      getRootPane().setDefaultButton(defaultButton);

      // Get current window height
      int windowHeight = getRootPane().getHeight();
      int imgSize = windowHeight / 2;

      // Scale and set icons
      heroLabel.setIcon(new ImageIcon(
          heroImage.getImage().getScaledInstance(
              imgSize, -1, Image.SCALE_SMOOTH)));
      enemyLabel.setIcon(new ImageIcon(
          enemyImage.getImage().getScaledInstance(
              imgSize, -1, Image.SCALE_SMOOTH)));
    }
  }

}
