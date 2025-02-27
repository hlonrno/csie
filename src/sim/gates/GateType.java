package sim.gates;

import java.util.stream.Stream;
import java.util.function.Function;

import gui.Colors;

public enum GateType {
    AND (Colors.lightBlue,   ins -> !ins.anyMatch(in -> !in)),
    NAND(Colors.darkBlue,    ins -> ins.anyMatch(in -> !in)),
    OR  (Colors.lightGreen,  ins -> ins.anyMatch(in -> in)),
    NOR (Colors.darkGreen,   ins -> !ins.anyMatch(in -> in)),
    XOR (Colors.lightPurple, ins -> ins.filter(in -> in).count() % 2 == 1),
    XNOR(Colors.darkPurple,  ins -> ins.filter(in -> in).count() % 2 == 0);

    protected Function<Stream<Boolean>, Boolean> eval;
    private Colors color;
    private byte id; // range [0;inf]

    GateType(Colors color, Function<Stream<Boolean>, Boolean> eval) {
        this.color = color;
        this.eval = eval;
    }

    public Colors color() {
        return color;
    }

    public byte id() {
        return id;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds.
     */
    public static GateType fromId(int id) {
        return values()[id];
    }

    static {
        var values = values();
        for (int i = 0; i < values.length; i++)
            values[i].id = (byte)i;
    }
}
