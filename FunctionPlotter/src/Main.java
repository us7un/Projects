import java.awt.*;

/**
 * Java program plotting various functions using concepts like OOP and Method Overriding, using StdDraw
 *
 * @author ustunyilmaz
 * @version 1.0.0
 */
public class Main {
    public static void main(String[] args) {
        int canvasWidth = 800; // width of the canvas
        int canvasHeight = 600; // height of the canvas
        double pointWidth = 0.01; // 'size' of the pen
        double xMin = (-2) * Math.PI; // starting location
        double xMax = 2 * Math.PI; // finishing location
        double yMin = -6; // lowest y-coordinate
        double yMax = 6; // highest y-coordinate

        StdDraw.enableDoubleBuffering(); // enable double buffering for smoother drawing
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(xMin, xMax);
        StdDraw.setYscale(yMin, yMax);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(pointWidth);
        StdDraw.line(0, yMin, 0, yMax); // draw y-axis
        StdDraw.line(xMin, 0, xMax, 0); // draw x-axis

        // Create function objects
        SineFunction sin = new SineFunction(Color.magenta);
        CosineFunction cos = new CosineFunction(Color.cyan);
        TangentFunction tan = new TangentFunction(Color.ORANGE);
        ExponentialFunction exp = new ExponentialFunction(Color.red);
        LogarithmicFunction ln = new LogarithmicFunction(Color.green);

        // Plot each function
        sin.plot(xMin, xMax, pointWidth, yMin, yMax);
        cos.plot(xMin, xMax, pointWidth, yMin, yMax);
        tan.plot(xMin, xMax, pointWidth, yMin, yMax);
        exp.plot(xMin, xMax, pointWidth, yMin, yMax);
        ln.plot(xMin, xMax, pointWidth, yMin, yMax);

    }
}