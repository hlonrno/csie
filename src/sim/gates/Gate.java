package sim.gates;

import java.awt.Color;
import java.util.ArrayList;

import sim.Vec2;

public class Gate {
  public Vec2 position;
  public GateType type;
  public boolean state;
  public ArrayList<Gate> inputs;

  public Gate(GateType type, Vec2 position) {
    this.type = type;
    this.position = position;
    inputs = new ArrayList<Gate>();
    eval();
  }

  public void eval() {
    state = type.eval.apply(inputs.stream().map(gate -> gate.state));
  }

  public void addInput(Gate gate) {
    inputs.add(gate);
  }

  public Color getColor() {
    return type.color.get();
  }

  @Override
  public String toString() {
    return new StringBuilder("<Gate:")
      .append(state ? 1 : 0)
      .append('|')
      .append(position)
      .append('|')
      .append(type)
      .append('|')
      .append(inputs.size())
      .append('>')
      .toString();
  }
}
