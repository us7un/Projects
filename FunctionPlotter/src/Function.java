import java.awt.*;

/**
 * Function super-class for common attributes of plotting several mathematical functions
 *
 * @author ustunyilmaz
 * @version 1.0.0
 */

public abstract class Function {
    protected Color color;

    public Function() {
    }

    public Function(Color color) {
        this.color = color;
    }

    /**
     * Method to plot numbers on a cartesian coordinate graph generated on StdDraw canvas
     *
     * @param xMin - the minimum x-coordinate
     * @param xMax - the maximum x-coordinate
     * @param yMin - the minimum y-coordinate
     * @param yMax - the maximum y-coordinate
     */
    public void plotNumbers(double xMin, double xMax, double yMin, double yMax) {
        StdDraw.setPenColor(StdDraw.BLACK); // set color of numbers

        for (int i = (int) xMin; i < ((int) xMax) + 1; i++) {
            if (i != 0) {
                StdDraw.text(i, -0.25, String.valueOf(i)); // write the numbers on x-axis
            } else StdDraw.text(0.1, -0.25, "0");
        }
        for (int i = (int) yMin; i < ((int) yMax) + 1; i++) {
            if (i != 0) {
                StdDraw.text(0.25, i, String.valueOf(i)); // write the numbers on y-axis
            }
        }

    }

    /**
     * A generalized method to plot the graphs of several functions
     *
     * @param xMin  - starting x-coordinate
     * @param xMax  - finishing x-coordinate
     * @param width - thickness of the plot
     * @param yMin  - bottom of the canvas
     * @param yMax  - top of the canvas
     */
    public void plot(double xMin, double xMax, double width, double yMin, double yMax) {
        StdDraw.setPenRadius(width);
        int pauseDuration = 3; // refresh rate of the plot
        double increment = 0.01; // period of the plot
        for (double i = xMin; i < xMax; i += increment) {
            plotNumbers(xMin, xMax, yMin, yMax); // plot numbers on axes
            StdDraw.setPenColor(color); // set plot color
            StdDraw.point(i, evaluate(i)); // draw the plot one by one on x-coordinates and y-coordinates
            StdDraw.show(); // show canvas
            StdDraw.pause(pauseDuration); // refresh
        }
    }

    public abstract double evaluate(double x);

}
