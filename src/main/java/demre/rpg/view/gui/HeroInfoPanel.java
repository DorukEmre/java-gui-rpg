package demre.rpg.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import demre.rpg.controller.GameController;
import demre.rpg.model.GameEngine;
import demre.rpg.model.characters.Hero;

public class HeroInfoPanel extends JPanel {

  public HeroInfoPanel(GameController controller, GameEngine gameEngine) {
    Hero hero = gameEngine.getHero();

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setBackground(Color.WHITE);

    JLabel titleLabel = new JLabel("Hero Information");
    titleLabel.setFont(new Font("Serif", Font.BOLD, 18));
    titleLabel.setAlignmentX(CENTER_ALIGNMENT);
    add(titleLabel);
    add(Box.createRigidArea(new Dimension(0, 10)));

    add(new JLabel("Name:       " + hero.getName()));
    add(new JLabel("Class:      " + hero.getClass().getSimpleName()));
    add(new JLabel("Level:      " + hero.getLevel()));
    add(new JLabel("Experience: " + hero.getExperience()));
    add(new JLabel("Attack:     " + hero.getAttack()));
    add(new JLabel("Defense:    " + hero.getDefense()));
    add(new JLabel("Hit Points: " + hero.getHitPoints()));
    add(new JLabel("Weapon:     " + hero.getWeapon().getName() +
        " +" + hero.getWeapon().getModifier()));
    add(new JLabel("Armor:      " + hero.getArmor().getName() +
        " +" + hero.getArmor().getModifier()));
    add(new JLabel("Helm:       " + hero.getHelm().getName() +
        " +" + hero.getHelm().getModifier()));

    add(Box.createRigidArea(new Dimension(0, 20)));

    JButton nextButton = new JButton("Next");
    nextButton.setAlignmentX(CENTER_ALIGNMENT);
    nextButton.addActionListener(
        e -> controller.onShowHeroInfoContinue("continue"));
    add(nextButton);

  }
}
