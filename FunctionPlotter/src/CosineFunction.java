import java.awt.*;

public class CosineFunction extends Function {
    public CosineFunction(Color color) {
        super(color);
    }

    /**
     * Evaluates the cos(x) function
     *
     * @param x - x-coordinate
     * @return cos(x)
     */
    @Override
    public double evaluate(double x) {
        return Math.cos(x);
    }
}
