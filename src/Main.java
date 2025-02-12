import sim.world.World;
import sim.gates.*;
import sim.Vec2;

public class Main {
  public static void main(String[] args) {
    // var and1 = new Gate(GateType.AND, new Vec2(0, 0));
    // var nor1 = new Gate(GateType.NOR, new Vec2(1, 1));
    var nor2 = new Gate(GateType.NOR, new Vec2(24, -24));

    var world = new World();
    // world.add(and1);
    // world.add(nor1);
    world.gate(nor2);
    /*
     * and1.addInput(nor1);
     * and1.addInput(nor2);
     * and1.eval();
     * System.out.println("1 & 1 = " + and1.state);
     */
  }
}
