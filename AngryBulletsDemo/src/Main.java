import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * A shooter game where you shoot a projectile through a square 'cannon'.
 *
 * @author Ustun Yilmaz
 * @version 1.0.0
 */


public class Main {
    public static void main(String[] args) {
        // Game Parameters
        int width = 1600; // screen width
        int height = 800; // screen height
        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(0, 1600); // set the x-scale resolution
        StdDraw.setYscale(0, 800); // set the y-scale resolution
        StdDraw.enableDoubleBuffering(); // enable double buffering for better rendering
        int pauseDuration = 34; // animation speed a.k.a. the refresh rate
        boolean gameRunning = true; // check variable that determines if the game is running
        boolean gameStart = false; // check variable that determines if the ball is launched
        Font font = new Font("Helvetica", Font.BOLD, 20); // arbitrary font for game won/over prompts
        double muzzleThickness = 0.008; // thickness of the cannon 'muzzle'

        double gravity = 9.80665; // gravity
        double x0 = 120; // x and y coordinates of the bullet’s starting position on the platform
        double y0 = 120;
        double bulletVelocity = 180; // initial velocity
        double bulletAngle = 45.0; // initial angle
        double bulletRadius = 4; // arbitrary bullet "size"
        double bulletPositionX = x0; // bullet's trajectory (x-position)
        double bulletPositionY = y0; // bullet's trajectory (y-position)
        double initialVelocityX; // x-component of the velocity
        double initialVelocityY; // y-component of the velocity
        double lineStartingPointX; // x-component of starting point of the line for the ball's trajectory
        double lineStartingPointY; // y-component of starting point of the line for the ball's trajectory
        double alternativeTime = 0; // time variable that determines the trajectory of projectile motion


        // Box coordinates for obstacles and targets
        // Each row stores a box containing the following information:
        // x and y coordinates of the lower left rectangle corner, width, and height
        double[][] obstacleArray = {
                {500, 300, 60, 220},
                {560, 300, 110, 60},
                {670, 300, 60, 220},
                {900, 200, 60, 220},
                {825, 420, 200, 60},
                {825, 480, 60, 110},
                {965, 480, 60, 110},
                {400, 200, 30, 400},
                {400, 200, 700, 30},
                {1100, 200, 30, 400},
                {600, 600, 400, 30}
        };
        double[][] targetArray = {
                {550, 520, 50, 50},
                {635, 520, 50, 50},
                {750, 160, 40, 40},
                {1000, 120, 40, 40},
                {1130, 500, 90, 30},
                {1200, 300, 60, 60}
        };

        while (true) {
            while (gameRunning) {
                // Drawing the "cannon"
                StdDraw.setFont(font); // set the font to helvetica bold
                StdDraw.setPenColor(); // reset the drawing color
                StdDraw.filledSquare(x0 / 2.0, y0 / 2.0, x0 / 2.0); // the "cannon"
                StdDraw.setPenColor(StdDraw.WHITE); // set the color of the text
                StdDraw.text(x0 * (1.0 / 2.0), y0 * (2.0 / 4.0), String.format("a: %.1f", bulletAngle)); // the angle to be displayed on the "cannon"
                StdDraw.text(x0 * (1.0 / 2.0), y0 * (1.0 / 4.0), String.format("v: %.1f", bulletVelocity)); // the velocity to be displayed on the "cannon"
                StdDraw.setPenColor(); // reset the drawing color
                StdDraw.setPenRadius(muzzleThickness); // set thickness of the "muzzle"
                StdDraw.line(x0, y0, x0 + (Math.pow(bulletVelocity,4) * (Math.cos(Math.toRadians(bulletAngle)))/(180*85000.0)), y0 + (Math.pow(bulletVelocity,4) * (Math.sin(Math.toRadians(bulletAngle))))/(180*85000.0)); // the shooter
                StdDraw.setPenRadius(); // reset the pen radius

                // Drawing the "obstacles"
                for (double[] rectangles : obstacleArray) {
                    StdDraw.setPenColor(StdDraw.DARK_GRAY); // set the color of the obstacles
                    StdDraw.filledRectangle((rectangles[0] + (rectangles[2] / 2.0)), (rectangles[1] + (rectangles[3] / 2.0)), rectangles[2] / 2.0, rectangles[3] / 2.0); // place the obstacles in a for-loop
                }

                // Drawing the "targets"
                for (double[] rectangles : targetArray) {
                    StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE); // set the color of the targets
                    StdDraw.filledRectangle((rectangles[0] + (rectangles[2] / 2.0)), (rectangles[1] + (rectangles[3] / 2.0)), rectangles[2] / 2.0, rectangles[3] / 2.0); // place the targets in a for-loop
                }

                // Control Configuration
                if (StdDraw.isKeyPressed(KeyEvent.VK_UP) && !gameStart) {
                    bulletAngle += 1.0; // increment bullet angle by 1
                    StdDraw.clear(); // clear the screen so the lines won't overlap and 'paint' the screen
                    // Drawing the "cannon"
                    StdDraw.setFont(font); // set the font to helvetica bold
                    StdDraw.setPenColor(); // reset the drawing color
                    StdDraw.filledSquare(x0 / 2.0, y0 / 2.0, x0 / 2.0); // the "cannon"
                    StdDraw.setPenColor(StdDraw.WHITE); // set the color of the text
                    StdDraw.text(x0 * (1.0 / 2.0), y0 * (2.0 / 4.0), String.format("a: %.1f", bulletAngle)); // the angle to be displayed on the "cannon"
                    StdDraw.text(x0 * (1.0 / 2.0), y0 * (1.0 / 4.0), String.format("v: %.1f", bulletVelocity)); // the velocity to be displayed on the "cannon"
                    StdDraw.setPenColor(); // reset the drawing color
                    StdDraw.setPenRadius(muzzleThickness); // thickness of the "muzzle"
                    StdDraw.line(x0, y0, x0 + (Math.pow(bulletVelocity,4) * (Math.cos(Math.toRadians(bulletAngle)))/(180*85000.0)), y0 + (Math.pow(bulletVelocity,4) * (Math.sin(Math.toRadians(bulletAngle))))/(180*85000.0)); // the shooter
                    StdDraw.setPenRadius(); // reset the pen radius

                    // Drawing the "obstacles"
                    for (double[] rectangles : obstacleArray) {
                        StdDraw.setPenColor(StdDraw.DARK_GRAY); // set the color of the obstacles
                        StdDraw.filledRectangle((rectangles[0] + (rectangles[2] / 2.0)), (rectangles[1] + (rectangles[3] / 2.0)), rectangles[2] / 2.0, rectangles[3] / 2.0); // place the obstacles in a for-loop
                    }

                    // Drawing the "targets"
                    for (double[] rectangles : targetArray) {
                        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE); // set the color of the targets
                        StdDraw.filledRectangle((rectangles[0] + (rectangles[2] / 2.0)), (rectangles[1] + (rectangles[3] / 2.0)), rectangles[2] / 2.0, rectangles[3] / 2.0); // place the targets in a for-loop
                    }
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_DOWN) && !gameStart) {
                    bulletAngle -= 1.0; // increment bullet angle by -1
                    StdDraw.clear(); // clear the screen so the lines won't overlap and 'paint' the screen
                    // Drawing the "cannon"
                    StdDraw.setFont(font); // set the font to helvetica bold
                    StdDraw.setPenColor(); // reset the drawing color
                    StdDraw.filledSquare(x0 / 2.0, y0 / 2.0, x0 / 2.0); // the "cannon"
                    StdDraw.setPenColor(StdDraw.WHITE); // set the color of the text
                    StdDraw.text(x0 * (1.0 / 2.0), y0 * (2.0 / 4.0), String.format("a: %.1f", bulletAngle)); // the angle to be displayed on the "cannon"
                    StdDraw.text(x0 * (1.0 / 2.0), y0 * (1.0 / 4.0), String.format("v: %.1f", bulletVelocity)); // the velocity to be displayed on the "cannon"
                    StdDraw.setPenColor(); // reset the drawing color
                    StdDraw.setPenRadius(muzzleThickness); // thickness of the "muzzle"
                    StdDraw.line(x0, y0, x0 + (Math.pow(bulletVelocity,4) * (Math.cos(Math.toRadians(bulletAngle)))/(180*85000.0)), y0 + (Math.pow(bulletVelocity,4) * (Math.sin(Math.toRadians(bulletAngle))))/(180*85000.0)); // the shooter
                    StdDraw.setPenRadius(); // reset the pen radius

                    // Drawing the "obstacles"
                    for (double[] rectangles : obstacleArray) {
                        StdDraw.setPenColor(StdDraw.DARK_GRAY); // set the color of the obstacles
                        StdDraw.filledRectangle((rectangles[0] + (rectangles[2] / 2.0)), (rectangles[1] + (rectangles[3] / 2.0)), rectangles[2] / 2.0, rectangles[3] / 2.0); // place the obstacles in a for-loop
                    }

                    // Drawing the "targets"
                    for (double[] rectangles : targetArray) {
                        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE); // set the color of the targets
                        StdDraw.filledRectangle((rectangles[0] + (rectangles[2] / 2.0)), (rectangles[1] + (rectangles[3] / 2.0)), rectangles[2] / 2.0, rectangles[3] / 2.0); // place the targets in a for-loop
                    }
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_RIGHT) && !gameStart) {
                    bulletVelocity += 1.0; // increment bullet velocity by 1
                    StdDraw.clear(); // clear the screen so the lines won't overlap and 'paint' the screen
                    // Drawing the "cannon"
                    StdDraw.setFont(font); // set the font to helvetica bold
                    StdDraw.setPenColor(); // reset the drawing color
                    StdDraw.filledSquare(x0 / 2.0, y0 / 2.0, x0 / 2.0); // the "cannon"
                    StdDraw.setPenColor(StdDraw.WHITE); // set the color of the text
                    StdDraw.text(x0 * (1.0 / 2.0), y0 * (2.0 / 4.0), String.format("a: %.1f", bulletAngle)); // the angle to be displayed on the "cannon"
                    StdDraw.text(x0 * (1.0 / 2.0), y0 * (1.0 / 4.0), String.format("v: %.1f", bulletVelocity)); // the velocity to be displayed on the "cannon"
                    StdDraw.setPenColor(); // reset the drawing color
                    StdDraw.setPenRadius(muzzleThickness); // thickness of the "muzzle"
                    StdDraw.line(x0, y0, x0 + (Math.pow(bulletVelocity,4) * (Math.cos(Math.toRadians(bulletAngle)))/(180*85000.0)), y0 + (Math.pow(bulletVelocity,4) * (Math.sin(Math.toRadians(bulletAngle))))/(180*85000.0)); // the shooter
                    StdDraw.setPenRadius(); // reset the pen radius

                    // Drawing the "obstacles"
                    for (double[] rectangles : obstacleArray) {
                        StdDraw.setPenColor(StdDraw.DARK_GRAY); // set the color of the obstacles
                        StdDraw.filledRectangle((rectangles[0] + (rectangles[2] / 2.0)), (rectangles[1] + (rectangles[3] / 2.0)), rectangles[2] / 2.0, rectangles[3] / 2.0); // place the obstacles in a for-loop
                    }

                    // Drawing the "targets"
                    for (double[] rectangles : targetArray) {
                        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE); // set the color of the targets
                        StdDraw.filledRectangle((rectangles[0] + (rectangles[2] / 2.0)), (rectangles[1] + (rectangles[3] / 2.0)), rectangles[2] / 2.0, rectangles[3] / 2.0); // place the targets in a for-loop
                    }
                }
                if (StdDraw.isKeyPressed(KeyEvent.VK_LEFT) && !gameStart) {
                    bulletVelocity -= 1.0; // increment bullet velocity by -1
                    StdDraw.clear(); // clear the screen so the lines won't overlap and 'paint' the screen
                    // Drawing the "cannon"
                    StdDraw.setFont(font); // set the font to helvetica bold
                    StdDraw.setPenColor(); // reset the drawing color
                    StdDraw.filledSquare(x0 / 2.0, y0 / 2.0, x0 / 2.0); // the "cannon"
                    StdDraw.setPenColor(StdDraw.WHITE); // set the color of the text
                    StdDraw.text(x0 * (1.0 / 2.0), y0 * (2.0 / 4.0), String.format("a: %.1f", bulletAngle)); // the angle to be displayed on the "cannon"
                    StdDraw.text(x0 * (1.0 / 2.0), y0 * (1.0 / 4.0), String.format("v: %.1f", bulletVelocity)); // the velocity to be displayed on the "cannon"
                    StdDraw.setPenColor(); // reset the drawing color
                    StdDraw.setPenRadius(muzzleThickness); // thickness of the "muzzle"
                    StdDraw.line(x0, y0, x0 + (Math.pow(bulletVelocity,4) * (Math.cos(Math.toRadians(bulletAngle)))/(180*85000.0)), y0 + (Math.pow(bulletVelocity,4) * (Math.sin(Math.toRadians(bulletAngle))))/(180*85000.0)); // the shooter
                    StdDraw.setPenRadius(); // reset the pen radius

                    // Drawing the "obstacles"
                    for (double[] rectangles : obstacleArray) {
                        StdDraw.setPenColor(StdDraw.DARK_GRAY); // set the color of the obstacles
                        StdDraw.filledRectangle((rectangles[0] + (rectangles[2] / 2.0)), (rectangles[1] + (rectangles[3] / 2.0)), rectangles[2] / 2.0, rectangles[3] / 2.0); // place the obstacles in a for-loop
                    }

                    // Drawing the "targets"
                    for (double[] rectangles : targetArray) {
                        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE); // set the color of the targets
                        StdDraw.filledRectangle((rectangles[0] + (rectangles[2] / 2.0)), (rectangles[1] + (rectangles[3] / 2.0)), rectangles[2] / 2.0, rectangles[3] / 2.0); // place the targets in a for-loop
                    }
                }


                // Drawing the "bullet" in motion
                StdDraw.setPenColor(); // reset the drawing color
                initialVelocityX = bulletVelocity * Math.cos(Math.toRadians(bulletAngle)) / 1.725; // initialize the velocity x-component according to trigonometric coefficients
                initialVelocityY = bulletVelocity * Math.sin(Math.toRadians(bulletAngle)) / 1.725; // initialize the velocity y-component according to trigonometric coefficients
                if (StdDraw.isKeyPressed(KeyEvent.VK_SPACE))
                    gameStart = true; // launch the ball when space is pressed
                if (gameStart) {
                    alternativeTime += 0.20; // increment time for the ball to progress along the trajectory
                    StdDraw.filledCircle(bulletPositionX, bulletPositionY, bulletRadius); // draw the ball
                    lineStartingPointX = bulletPositionX; // move the trajectory starting point x-component
                    lineStartingPointY = bulletPositionY; // move the trajectory starting point y-component
                    bulletPositionX = x0 + initialVelocityX * alternativeTime; // update the ball's position x-component according to projectile motion formula
                    bulletPositionY = y0 + initialVelocityY * alternativeTime - ((1 / 2.0) * gravity * Math.pow(alternativeTime, 2)); // update the ball's position y-component according to projectile motion formula
                    StdDraw.line(lineStartingPointX, lineStartingPointY, bulletPositionX, bulletPositionY); // draw the trajectory
                }

                // Game Won and Game End
                for (double[] rectangles : obstacleArray) {
                    if (bulletPositionX >= rectangles[0] && bulletPositionX <= rectangles[0] + rectangles[2] && bulletPositionY >= rectangles[1] && bulletPositionY <= rectangles[1] + rectangles[3]) {
                        StdDraw.filledCircle(bulletPositionX, bulletPositionY, bulletRadius); // draw the circle one last time
                        gameRunning = false; // end the game
                        gameStart = false; // 'de-launch' the ball
                        StdDraw.setFont(font); // set font to helvetica bold
                        StdDraw.text(200, 765, "Hit an obstacle. Press 'r' to shoot again."); // display losing message
                        StdDraw.show(); // show the canvas
                        break;
                    }
                }
                for (double[] rectangles : targetArray) {
                    if (bulletPositionX >= rectangles[0] && bulletPositionX <= rectangles[0] + rectangles[2] && bulletPositionY >= rectangles[1] && bulletPositionY <= rectangles[1] + rectangles[3]) {
                        StdDraw.filledCircle(bulletPositionX, bulletPositionY, bulletRadius); // draw the circle one last time
                        gameRunning = false; // end the game
                        gameStart = false; // 'de-launch' the ball
                        StdDraw.setFont(font); // set font to helvetica bold
                        StdDraw.text(180, 765, "Congratulations: You hit the target!"); // display winning message
                        StdDraw.show(); // show the canvas
                        break;
                    }
                }
                if (bulletPositionX >= 1600){
                    StdDraw.filledCircle(bulletPositionX, bulletPositionY, bulletRadius); // draw the circle one last time
                    gameRunning = false; // end the game
                    gameStart = false; // 'de-launch' the ball
                    StdDraw.setFont(font); // set font to helvetica bold
                    StdDraw.text(200, 765, "Max X reached. Press 'r' to shoot again."); // display losing message
                    StdDraw.show(); // show the canvas
                    break;
                }
                if (bulletPositionY <= 0){
                    StdDraw.setFont(font); // set font to helvetica bold
                    StdDraw.text(200, 765, "Hit the ground. Press 'r' to shoot again."); // display losing message
                    StdDraw.filledCircle(bulletPositionX, bulletPositionY, bulletRadius); // draw the circle one last time
                    gameRunning = false; // end the game
                    gameStart = false; // 'de-launch' the ball
                    StdDraw.show(); // show the canvas
                    break;
                }

                // "Drawing" the game
                StdDraw.show(); // show the canvas
                StdDraw.pause(pauseDuration); // pause according to refresh rate
            }
            if (StdDraw.isKeyPressed(KeyEvent.VK_R)){
                StdDraw.clear(); // clear the canvas
                bulletVelocity = 180; // modify the velocity to initial
                bulletAngle = 45.0; // modify the angle to initial
                bulletPositionX = x0; // 'de-launch' the ball x-component
                alternativeTime = 0; // reset the time
                bulletPositionY = y0; // 'de-launch' the ball y-component
                gameRunning = true; // restart the game
            }
        }
    }
}