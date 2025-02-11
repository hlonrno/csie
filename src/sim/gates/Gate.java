package sim.gates;

import java.util.ArrayList;

import sim.Vec2;

public class Gate {
  public Vec2 position;
  public GateType type;
  public boolean state;
  public ArrayList<Gate> inputs;
  public ArrayList<Gate> outputs;

  public Gate(GateType type, Vec2 position) {
    this.type = type;
    this.position = position;
    inputs = new ArrayList<Gate>();
    outputs = new ArrayList<Gate>();
    eval();
  }

  public void eval() {
    state = type.eval.apply(inputs.stream().map(gate -> gate.state));
  }
}
