package sim.gates;

import java.util.stream.Stream;
import java.util.function.Function;

public enum GateType {
  AND(ins -> !ins.anyMatch(in -> !in)),
  OR(ins -> ins.anyMatch(in -> in)),
  XOR(ins -> ins.mapToInt(in -> in ? 1 : 0).reduce(0, Integer::sum) % 2 == 1),
  NAND(ins -> ins.anyMatch(in -> !in)),
  NOR(ins -> !ins.anyMatch(in -> in)),
  XNOR(ins -> ins.mapToInt(in -> in ? 1 : 0).reduce(0, Integer::sum) % 2 == 0);

  Function<Stream<Boolean>, Boolean> eval;
  GateType(Function<Stream<Boolean>, Boolean> eval) {
    this.eval = eval;
  }
}
