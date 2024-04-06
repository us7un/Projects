import java.awt.*;

public class TangentFunction extends Function {
    public TangentFunction(Color color) {
        super(color);
    }

    /**
     * Evaluates the tan(x) function
     *
     * @param x - x-coordinate
     * @return tan(x)
     */
    @Override
    public double evaluate(double x) {
        return Math.tan(x);
    }
}
