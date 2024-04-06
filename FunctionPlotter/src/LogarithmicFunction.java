import java.awt.*;

public class LogarithmicFunction extends Function {
    public LogarithmicFunction(Color color) {
        super(color);
    }

    /**
     * Evaluates the ln(x) function
     *
     * @param x - x-coordinate
     * @return ln(x)
     */
    @Override
    public double evaluate(double x) {
        return Math.log(x);
    }

    /**
     * Specialized plot for ln(x) function
     *
     * @param xMin  - starting x-coordinate
     * @param xMax  - finishing x-coordinate
     * @param width - thickness of the plot
     * @param yMin  - bottom of the canvas
     * @param yMax  - top of the canvas
     */
    @Override
    public void plot(double xMin, double xMax, double width, double yMin, double yMax) {
        StdDraw.setPenRadius(width);
        int pauseDuration = 3;
        double increment = 0.01;
        for (double i = Double.MIN_VALUE; i < xMax; i += increment) {
            plotNumbers(xMin, xMax, yMin, yMax);
            StdDraw.setPenColor(color);
            StdDraw.point(i, evaluate(i));
            StdDraw.show();
            StdDraw.pause(pauseDuration);
        }
    }
}
