package demre.rpg.view.gui;

import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public class CreateHeroPanel extends JPanel {
  private JButton defaultButton;
  private JTextField focusField;

  public CreateHeroPanel(GameController controller, GameEngine gameEngine) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(CENTER_ALIGNMENT);

    add(Box.createVerticalGlue());

    JLabel titleLabel = GUIUtils.createTitle(
        "Create Your Hero", 24);
    add(titleLabel);

    add(Box.createVerticalStrut(20));

    if (gameEngine.getStep() == GameEngine.Step.INVALID_HERO_CREATION) {
      {
        JLabel errorLabel = GUIUtils.createInfo(
            "Invalid hero creation. Please try again.", 16);
        add(errorLabel);
      }
      {
        JLabel errorLabel = GUIUtils.createInfo(
            "Hero name must be 3-20 characters long and can only contain alphanumeric characters and spaces.", 16);
        add(errorLabel);
        add(Box.createVerticalStrut(20));
      }

    }

    // Enter hero name
    JLabel nameLabel = GUIUtils.createSubTitle(
        "Enter hero name:", 18);
    add(nameLabel);
    add(Box.createVerticalStrut(10));

    JTextField heroNameField = new JTextField(20);
    heroNameField.setMaximumSize(heroNameField.getPreferredSize());
    heroNameField.setFont(new Font("Arial", Font.PLAIN, 18));
    heroNameField.setAlignmentX(CENTER_ALIGNMENT);
    heroNameField.setToolTipText(
        "Hero name must be 3-20 characters long and can only contain alphanumeric characters and spaces.");
    add(heroNameField);

    add(Box.createVerticalStrut(20));

    // Select Hero Class
    JLabel classLabel = GUIUtils.createSubTitle(
        "Select Hero Class:", 18);
    add(classLabel);

    JPanel classPanel = new JPanel();
    classPanel.setLayout(new BoxLayout(classPanel, BoxLayout.Y_AXIS));
    classPanel.setAlignmentX(CENTER_ALIGNMENT);

    JRadioButton warriorButton = new JRadioButton(
        "Warrior - A strong fighter, +10% experience gain");
    warriorButton.setFont(new Font("Serif", Font.PLAIN, 16));
    warriorButton.setAlignmentX(LEFT_ALIGNMENT);
    warriorButton.setSelected(true);

    JRadioButton rogueButton = new JRadioButton(
        "Rogue - A stealthy assassin, +10% chance to dodge attacks");
    rogueButton.setFont(new Font("Serif", Font.PLAIN, 16));
    rogueButton.setAlignmentX(LEFT_ALIGNMENT);

    JRadioButton mageButton = new JRadioButton(
        "Mage - A master of magic, +10% chance to find items");
    mageButton.setFont(new Font("Serif", Font.PLAIN, 16));
    mageButton.setAlignmentX(LEFT_ALIGNMENT);

    ButtonGroup group = new ButtonGroup();
    group.add(warriorButton);
    group.add(rogueButton);
    group.add(mageButton);

    classPanel.add(warriorButton);
    classPanel.add(rogueButton);
    classPanel.add(mageButton);

    add(classPanel);

    add(Box.createVerticalStrut(20));

    // Add buttons for creating hero or canceling
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

    JButton cancelButton = GUIUtils.createButton("Go back");
    GUIUtils.bindButtonToKey(
        buttonPanel, cancelButton, KeyStroke.getKeyStroke("ESCAPE"));
    cancelButton.addActionListener(e -> {
      controller.onCreateHeroContinue(
          "", "", true);
    });
    buttonPanel.add(cancelButton);

    buttonPanel.add(Box.createHorizontalStrut(10));

    JButton createButton = GUIUtils.createButton("Create Hero");
    createButton.addActionListener(e -> {
      String heroClass = warriorButton.isSelected()
          ? "Warrior"
          : mageButton.isSelected() ? "Mage" : "Rogue";
      controller.onCreateHeroContinue(
          heroNameField.getText(), heroClass, false);
    });
    buttonPanel.add(createButton);

    add(buttonPanel);

    add(Box.createVerticalGlue());

    defaultButton = createButton;
    focusField = heroNameField;
  }

  @Override
  public void addNotify() {
    super.addNotify();
    if (getRootPane() != null) {
      getRootPane().setDefaultButton(defaultButton);
    }
    focusField.requestFocusInWindow();
  }

}
