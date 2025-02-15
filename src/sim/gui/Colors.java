package sim.gui;

import java.awt.Color;

public enum Colors {
  black(new Color(10, 10, 10)),
  white(new Color(240, 240, 240)),
  lightBlue(new Color(75, 120, 230)),
  lightGreen(new Color(120, 230, 75)),
  lightPurple(new Color(180, 75, 210)),
  darkGrey(new Color(55, 55, 55)),
  darkBlue(new Color(20, 50, 120)),
  darkGreen(new Color(30, 170, 20)),
  darkPurple(new Color(140, 40, 190));

  Color color;

  Colors(Color color) {
    this.color = color;
  }

  public Color get() {
    return color;
  }
}
