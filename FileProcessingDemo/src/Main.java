/**
 * Java program that graphs the grades and their averages of the students through StdDraw library (stdlib.jar),
 * provided some text file in the format of grades.txt
 *
 * @author ustunyilmaz
 * @version 1.0.0
 */

import java.util.*;
import java.awt.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String fileName = "grades.txt"; // initialize a file name
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.printf("%s does not exist in this directory", fileName);
            System.exit(1);
        }
        Scanner fileInput = new Scanner(file);

        int n = fileInput.nextInt();
        String[] names = new String[n];
        double[] grades = new double[n];
        fileInput.nextLine();

        String[] duple;
        for (int i = 0; i < n; i++) {
            duple = fileInput.nextLine().split(",");
            names[i] = duple[0];
            grades[i] = Double.parseDouble(duple[1]);
        }
        fileInput.close();

        double total = 0;
        for (double grade : grades) {
            total += grade;
        }
        double average = total / (double) n;

        StdDraw.setCanvasSize(800, 500); // set the canvas size
        StdDraw.setXscale(0, (n * 200) + 100); // set x-coordinate resolution
        StdDraw.setYscale(0, 11000); // set y-coordinate resolution

        double startingPoint = 100; // set x-coordinate origin for drawing columns
        StdDraw.setPenColor(Color.cyan); // set the color of columns
        for (int i = 0; i < n; i++) {
            StdDraw.filledRectangle(startingPoint + 50, grades[i] * 50, 50, grades[i] * 50);
            startingPoint += 200;
        }
        StdDraw.setPenColor(Color.red); // set the color of average line
        StdDraw.filledRectangle(((n * 200) + 100) / 2.0, average * 100, ((n * 200) + 100) / 2.0, 50);
        StdDraw.setPenColor(); // set the color of student names on columns
        double textPositionX = 150; // set x-coordinate origin for writing names (recommended to set as same as startingPoint variable)
        double textPositionY = 200; // set y-coordinate for writing names
        for (int i = 0; i < n; i++) {
            StdDraw.text(textPositionX, textPositionY, names[i]);
            StdDraw.text(textPositionX, textPositionY + (grades[i] * 100), "" + grades[i]);
            textPositionX += 200;
        }
        double averageTextX = ((n * 200) + 100) / 5.0; // set x-coordinate of average text
        StdDraw.setPenColor(Color.red); // set the color of average text
        StdDraw.text(averageTextX, (average * 100) + 300, "Average:" + average);

        StdDraw.show();
    }
}
