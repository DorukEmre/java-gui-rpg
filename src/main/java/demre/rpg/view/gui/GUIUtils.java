package demre.rpg.view.gui;

import java.awt.Font;

import javax.swing.JLabel;

public class GUIUtils {

  protected static JLabel createTitle(String text, int fontSize) {
    JLabel label = new JLabel(text);

    label.setFont(new Font("Serif", Font.BOLD, fontSize));
    label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    return label;
  }

  protected static JLabel createLabel(String text, int fontSize) {
    JLabel label = new JLabel(text);

    label.setFont(new Font("Serif", Font.PLAIN, fontSize));
    label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    return label;
  }

  protected static JLabel createLabel(String text, int fontSize, int style) {
    JLabel label = new JLabel(text);

    label.setFont(new Font("Serif", style, fontSize));
    label.setAlignmentX(JLabel.CENTER_ALIGNMENT);

    return label;
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
}
