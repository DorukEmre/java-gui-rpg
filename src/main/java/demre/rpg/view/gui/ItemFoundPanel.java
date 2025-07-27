
package demre.rpg.view.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;
import demre.rpg.model.items.Item;

public class ItemFoundPanel extends JPanel {
  private final GameController controller;
  private final GameEngine gameEngine;
  private JButton defaultButton;

  public ItemFoundPanel(GameController controller, GameEngine gameEngine) {
    this.controller = controller;
    this.gameEngine = gameEngine;

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    // Add components for controlling the map view
    add(createInstructionsPanel());
    add(createActionButtonsPanel());
  }

  private JPanel createInstructionsPanel() {
    JPanel instructionPanel = new JPanel();
    instructionPanel.setLayout(
        new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
    instructionPanel.setAlignmentX(CENTER_ALIGNMENT);

    GameEngine.Step step = gameEngine.getStep();
    Hero hero = gameEngine.getHero();
    Item.Type foundItemType = gameEngine.getItemFound().getType();

    if (step == GameEngine.Step.ITEM_INVALID_ACTION) {
      JLabel label = GUIUtils.createInfo(
          "Invalid action. Please try again.", 16);
      instructionPanel.add(label);

    } else if (step == GameEngine.Step.ITEM_FOUND_AND_LEVEL_UP) {
      JLabel label1 = GUIUtils.createLabel(
          "You defeated the enemy and leveled up!", 16);
      instructionPanel.add(label1);
      JLabel label2 = GUIUtils.createLabel(
          "You are now level " + hero.getLevel() + " with "
              + hero.getExperience() + " experience points.",
          16);
      instructionPanel.add(label2);

    } else if (step == GameEngine.Step.ITEM_FOUND) {
      JLabel label1 = GUIUtils.createLabel(
          "You defeated the enemy!", 16);
      instructionPanel.add(label1);
    }

    instructionPanel.add(Box.createVerticalStrut(10));

    {
      JLabel label1 = GUIUtils.createLabel(
          "You found an item. " + gameEngine.getItemFound(), 16);
      instructionPanel.add(label1);

      String text = "";
      if (foundItemType == Item.Type.WEAPON) {
        text = "Your current weapon is: " + hero.getWeapon().getFormattedName();
      } else if (foundItemType == Item.Type.ARMOR) {
        text = "Your current armor is: " + hero.getArmor().getFormattedName();
      } else if (foundItemType == Item.Type.HELM) {
        text = "Your current helm is: " + hero.getHelm().getFormattedName();
      }
      JLabel label2 = GUIUtils.createLabel(text, 16);
      instructionPanel.add(label2);

    }

    return instructionPanel;
  }

  private JPanel createActionButtonsPanel() {
    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
    actionPanel.setAlignmentX(CENTER_ALIGNMENT);

    {
      JButton button = GUIUtils.createButton("Keep");
      GUIUtils.bindButtonToKey(
          actionPanel, button, KeyStroke.getKeyStroke("K"));
      button.addActionListener(
          e -> controller.onItemFoundContinue("Keep"));
      actionPanel.add(button);
    }

    {
      actionPanel.add(Box.createHorizontalStrut(10));

      JButton button = GUIUtils.createButton("Leave");
      GUIUtils.bindButtonToKey(
          actionPanel, button, KeyStroke.getKeyStroke("L"));
      button.addActionListener(
          e -> controller.onItemFoundContinue("Leave"));
      actionPanel.add(button);
      defaultButton = button; // Default button for Enter key
    }

    return actionPanel;
  }

  @Override
  public void addNotify() {
    super.addNotify();
    if (getRootPane() != null) {
      getRootPane().setDefaultButton(defaultButton);
    }
  }

}
