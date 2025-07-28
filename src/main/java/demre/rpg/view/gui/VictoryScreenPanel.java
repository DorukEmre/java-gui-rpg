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

public class VictoryScreenPanel extends JPanel {
  private JButton defaultButton;

  private JLabel heroLabel;
  private JLabel enemyLabel;

  private final ImageIcon heroImage = new ImageIcon(
      getClass().getResource("/icons/heroHappy.png"));
  private final ImageIcon enemyImage = new ImageIcon(
      getClass().getResource("/icons/monsterScared.png"));

  public VictoryScreenPanel(GameController controller, GameEngine gameEngine) {

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    add(Box.createVerticalGlue());

    // Add messages
    JLabel titleLabel = GUIUtils.createTitle(
        "Congratulations!", 24);
    add(titleLabel);

    add(Box.createVerticalStrut(20));

    JLabel label1 = GUIUtils.createLabel(
        "You have successfully completed the mission!", 18);
    add(label1);

    if (gameEngine.getStep() == GameEngine.Step.VICTORY_INVALID_ACTION) {
      JLabel label2 = GUIUtils.createInfo(
          "Invalid action. Please try again!", 18);
      add(label2);
    }

    add(Box.createVerticalStrut(20));

    // Add images
    JPanel imagePanel = new JPanel();
    imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
    imagePanel.setAlignmentX(CENTER_ALIGNMENT);

    heroLabel = new JLabel(heroImage);
    imagePanel.add(heroLabel);

    imagePanel.add(Box.createHorizontalStrut(20));

    enemyLabel = new JLabel(enemyImage);
    imagePanel.add(enemyLabel);

    add(imagePanel);

    add(Box.createVerticalStrut(20));

    // Add buttons
    JPanel choice = new JPanel();
    choice.setLayout(new BoxLayout(choice, BoxLayout.X_AXIS));
    choice.setAlignmentX(CENTER_ALIGNMENT);

    {
      JButton button = GUIUtils.createButton("Next mission");
      button.setAlignmentX(CENTER_ALIGNMENT);
      GUIUtils.bindButtonToKey(
          choice, button, KeyStroke.getKeyStroke("N"));
      button.addActionListener(
          e -> controller.onVictoryScreenContinue("next"));
      choice.add(button);
    }

    choice.add(Box.createHorizontalStrut(20));

    {
      JButton button = GUIUtils.createButton("Exit");
      button.setAlignmentX(CENTER_ALIGNMENT);
      GUIUtils.bindButtonToKey(
          choice, button, KeyStroke.getKeyStroke("X"));
      button.addActionListener(
          e -> controller.onVictoryScreenContinue("exit"));
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
