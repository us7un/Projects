/**
 * @author ustunyilmaz
 * @version 1.0.0
 * Java code that simulates a game using StdDraw library (stdlib.jar)
 */

import java.awt.*;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {
        double fallingConstant = 25;
        int jumpSpeed = 30;
        double x = 10, y = 200;
        double vx = 20;
        double dty = 0.1, dtx = 0.05;

        int canvasWidth = 800;
        int canvasHeight = 600;
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(0, canvasWidth);
        StdDraw.setYscale(0, canvasHeight);
        StdDraw.enableDoubleBuffering();

        double[][] rectangles = {
                {300, 160, 50, 160},
                {300, 490, 50, 110},
                {650, 415, 50, 185},
                {650, 85, 50, 85}
        };

        boolean gameAlive = true;
        double lastPressed = 0;

        while (gameAlive) {
            double currentTime = (System.currentTimeMillis());
            StdDraw.setPenColor();
            StdDraw.clear();
            for (double[] rectanglePositions : rectangles) {
                StdDraw.filledRectangle(rectanglePositions[0], rectanglePositions[1], rectanglePositions[2], rectanglePositions[3]);
            }

            StdDraw.setPenColor(Color.magenta);
            StdDraw.filledCircle(x, y, 10);
            x += vx * 2 * dtx;
            y -= fallingConstant * dty;
            if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE) && (currentTime - lastPressed > 100)) {
                lastPressed = System.currentTimeMillis();
                y += jumpSpeed;
            }

            for (double[] rectanglePositions : rectangles) {
                if ((x >= rectanglePositions[0] - rectanglePositions[2] && x <= rectanglePositions[0] + rectanglePositions[2] && y <= rectanglePositions[1] + rectanglePositions[3] && y >= rectanglePositions[1] - rectanglePositions[3]) || x == 0 || y == 0 || x > canvasWidth || y > canvasWidth) {
                    StdDraw.setFont(new Font("Helvetica", Font.BOLD, 100));
                    StdDraw.text(400, 300, "You lost!");
                    gameAlive = false;
                }
            }
            if (x == canvasWidth) {
                StdDraw.setFont(new Font("Helvetica", Font.BOLD, 100));
                StdDraw.text(400, 300, "You won!");
                gameAlive = false;

            }


            StdDraw.show();
            StdDraw.pause(15);
        }
    }
}
