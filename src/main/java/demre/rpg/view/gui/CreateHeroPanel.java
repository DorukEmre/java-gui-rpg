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

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;

public class CreateHeroPanel extends JPanel {
  private JButton defaultButton;
  private JTextField focusField;

  public CreateHeroPanel(GameController controller, GameEngine gameEngine) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(CENTER_ALIGNMENT);

    add(Box.createVerticalGlue());

    JLabel titleLabel = new JLabel("Create Your Hero");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setAlignmentX(CENTER_ALIGNMENT);
    add(titleLabel);

    add(Box.createVerticalStrut(20));

    if (gameEngine.getStep() == GameEngine.Step.INVALID_HERO_CREATION) {
      {
        JLabel errorLabel = new JLabel(
            "Invalid hero creation. Please try again.");
        errorLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        errorLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(errorLabel);
      }
      {
        JLabel errorLabel = new JLabel(
            "Hero name must be 3-20 characters long and can only contain alphanumeric characters and spaces.\n");
        errorLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        errorLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(errorLabel);
        add(Box.createVerticalStrut(20));
      }

    }

    // Enter hero name
    JLabel nameLabel = new JLabel("Enter hero name (3-20 characters):");
    nameLabel.setAlignmentX(CENTER_ALIGNMENT);
    add(nameLabel);

    JTextField heroNameField = new JTextField(20);
    heroNameField.setMaximumSize(heroNameField.getPreferredSize());
    heroNameField.setFont(new Font("Arial", Font.PLAIN, 14));
    heroNameField.setAlignmentX(CENTER_ALIGNMENT);
    heroNameField.setToolTipText("Enter hero name (3-20 characters)");
    add(heroNameField);

    add(Box.createVerticalStrut(20));

    // Select Hero Class
    JLabel classLabel = new JLabel("Select Hero Class:");
    classLabel.setAlignmentX(CENTER_ALIGNMENT);
    add(classLabel);

    JRadioButton warriorButton = new JRadioButton(
        "Warrior - A strong fighter, +10% experience gain");
    warriorButton.setFont(new Font("Serif", Font.PLAIN, 16));
    warriorButton.setAlignmentX(CENTER_ALIGNMENT);
    warriorButton.setSelected(true);

    JRadioButton rogueButton = new JRadioButton(
        "Rogue - A stealthy assassin, +10% chance to dodge attacks");
    rogueButton.setFont(new Font("Serif", Font.PLAIN, 16));
    rogueButton.setAlignmentX(CENTER_ALIGNMENT);

    JRadioButton mageButton = new JRadioButton(
        "Mage - A master of magic, +10% chance to find items");
    mageButton.setFont(new Font("Serif", Font.PLAIN, 16));
    mageButton.setAlignmentX(CENTER_ALIGNMENT);

    ButtonGroup group = new ButtonGroup();
    group.add(warriorButton);
    group.add(rogueButton);
    group.add(mageButton);

    add(warriorButton);
    add(mageButton);
    add(rogueButton);

    add(Box.createVerticalStrut(20));

    // Add buttons for creating hero or canceling
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

    JButton cancelButton = new JButton("Go back");
    cancelButton.setAlignmentX(CENTER_ALIGNMENT);
    cancelButton.addActionListener(e -> {
      controller.onCreateHeroContinue(
          "", "", true);
    });
    buttonPanel.add(cancelButton);

    buttonPanel.add(Box.createHorizontalStrut(10));

    JButton createButton = new JButton("Create Hero");
    createButton.setAlignmentX(CENTER_ALIGNMENT);
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
