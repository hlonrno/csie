package sim.gates;

import java.util.stream.Stream;
import java.util.function.Function;

import sim.gui.Colors;

public enum GateType {
  AND(Colors.lightBlue, ins -> !ins.anyMatch(in -> !in)),
  OR(Colors.lightGreen, ins -> ins.anyMatch(in -> in)),
  XOR(Colors.lightPurple, ins -> ins.filter(in -> in).count() % 2 == 1),
  NAND(Colors.darkBlue, ins -> ins.anyMatch(in -> !in)),
  NOR(Colors.darkGreen, ins -> !ins.anyMatch(in -> in)),
  XNOR(Colors.darkPurple, ins -> ins.filter(in -> in).count() % 2 == 0);

  Function<Stream<Boolean>, Boolean> eval;
  Colors color;

  GateType(Colors color, Function<Stream<Boolean>, Boolean> eval) {
    this.color = color;
    this.eval = eval;
  }
}
