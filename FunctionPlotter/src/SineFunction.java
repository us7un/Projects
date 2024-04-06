import java.awt.*;

public class SineFunction extends Function {
    public SineFunction(Color color) {
        super(color);
    }

    /**
     * Evaluates the sin(x) function
     *
     * @param x - x-coordinate
     * @return sin(x)
     */

    @Override
    public double evaluate(double x) {
        return Math.sin(x);
    }
}
