package demre.rpg.view.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class GUIUtils {

  protected static JLabel createTitle(String text, int fontSize) {
    JLabel label = new JLabel(text);

    label.setFont(new Font("Serif", Font.BOLD, fontSize));
    label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    return label;
  }

  protected static JLabel createLabel(String text, int fontSize, int style) {
    JLabel label = new JLabel(text);

    label.setFont(new Font("Serif", style, fontSize));
    label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    return label;
  }

  protected static JLabel createLabel(String text, int fontSize) {
    return createLabel(text, fontSize, Font.PLAIN);
  }

  protected static JLabel createList(String text, int fontSize) {
    JLabel label = new JLabel(text);

    label.setFont(new Font("Serif", Font.PLAIN, fontSize));
    label.setAlignmentX(JLabel.LEFT_ALIGNMENT);

    return label;
  }

  protected static JLabel createSubTitle(String text, int fontSize) {
    JLabel label = new JLabel(text);

    label.setFont(new Font("Arial", Font.BOLD, fontSize));
    label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    return label;
  }

  protected static JLabel createInfo(String text, int fontSize) {
    JLabel label = new JLabel(text);

    label.setFont(new Font("Arial", Font.ITALIC, fontSize));
    label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    return label;
  }

  protected static JButton createButton(String text, int fontSize) {
    JButton button = new JButton(text);
    button.setFont(button.getFont().deriveFont((float) fontSize));
    return button;
  }

  public static JButton createButton(String text) {
    return createButton(text, 16);
  }

  protected static void bindButtonToKey(
      JPanel panel, JButton button, KeyStroke keyStroke) {

    InputMap im = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = panel.getActionMap();

    String actionName = "move_" + keyStroke.toString();

    im.put(keyStroke, actionName);
    am.put(actionName, new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        button.doClick();
      }
    });

  }
}
