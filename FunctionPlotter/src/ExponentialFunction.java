import java.awt.*;

public class ExponentialFunction extends Function {
    public ExponentialFunction(Color color) {
        super(color);
    }

    /**
     * Evaluates the e^x function
     *
     * @param x - x-coordinate
     * @return e^x
     */
    @Override
    public double evaluate(double x) {
        return Math.exp(x);
    }
}
