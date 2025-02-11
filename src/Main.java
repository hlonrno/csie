import java.util.Scanner;

import sim.world.World;
import sim.gates.*;
import sim.Vec2;

public class Main {
  public static void main(String[] args) {
    var world = new World();

    try (var sc = new Scanner(System.in)) {
      String line;
      while (!(line = sc.nextLine()).isBlank()) {
        String[] axies = line.split(";");
        Vec2 position = new Vec2(Integer.parseInt(axies[0]), Integer.parseInt(axies[1]));
        Gate gate = new Gate(GateType.OR, position);
        world.add(gate);
      }
    }
  }
}
