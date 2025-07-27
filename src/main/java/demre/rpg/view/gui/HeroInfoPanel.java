package demre.rpg.view.gui;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;

public class HeroInfoPanel extends JPanel {
  private JButton defaultButton;

  public HeroInfoPanel(GameController controller, GameEngine gameEngine) {
    Hero hero = gameEngine.getHero();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setAlignmentX(CENTER_ALIGNMENT);

    add(Box.createVerticalGlue());

    // Title label
    JLabel titleLabel = GUIUtils.createTitle(
        "Hero Information", 24);
    add(titleLabel);

    add(Box.createVerticalStrut(20));

    // Hero stats panel
    JPanel statsPanel = new JPanel();
    statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
    statsPanel.setAlignmentX(CENTER_ALIGNMENT);

    statsPanel.add(GUIUtils.createList(
        "Name:       " + hero.getName(), 18));
    statsPanel.add(GUIUtils.createList(
        "Class:      " + hero.getClass().getSimpleName(), 18));
    statsPanel.add(GUIUtils.createList(
        "Level:      " + hero.getLevel(), 18));
    statsPanel.add(GUIUtils.createList(
        "Experience: " + hero.getExperience(), 18));
    statsPanel.add(GUIUtils.createList(
        "Attack:     " + hero.getAttack(), 18));
    statsPanel.add(GUIUtils.createList(
        "Defense:    " + hero.getDefense(), 18));
    statsPanel.add(GUIUtils.createList(
        "Hit Points: " + hero.getHitPoints(), 18));
    statsPanel.add(GUIUtils.createList(
        "Weapon:     " + hero.getWeapon().getFormattedName(), 18));
    statsPanel.add(GUIUtils.createList(
        "Armor:      " + hero.getArmor().getFormattedName(), 18));
    statsPanel.add(GUIUtils.createList(
        "Helm:       " + hero.getHelm().getFormattedName(), 18));

    add(statsPanel);

    add(Box.createVerticalStrut(40));

    // Next button
    JButton nextButton = new JButton("Next");
    nextButton.addActionListener(
        e -> controller.onShowHeroInfoContinue("continue"));
    add(nextButton);

    add(Box.createVerticalGlue());

    defaultButton = nextButton;
  }

  @Override
  public void addNotify() {
    super.addNotify();
    if (getRootPane() != null) {
      getRootPane().setDefaultButton(defaultButton);
    }
  }

}
