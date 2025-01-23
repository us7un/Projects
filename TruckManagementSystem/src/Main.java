import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * A truck stop simulation project which has simple but efficient and powerful methods to create/delete parking lots,
 * add trucks, load and organize their workloads.
 * @author ustunyilmaz
 * @version 1.0.0
 * @since Oct. 28, 2024
 */
public class Main {
    /**
     * Driver method to simulate the truckStop
     * @param args Input and output text files, respectively
     * @throws IOException File does not exist
     */
    public static void main(String[] args) throws IOException {
        Path input = Path.of(args[0]);
        Path outputPath = Path.of(args[1]);
        Files.writeString(outputPath, "", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING); // Clear file if already exists, if not create it
        String newLine = Files.readString(input); // Get all new lines from input file
        StringBuilder stringBuilder = new StringBuilder(); // Efficiently concatenate strings to each other

        TruckStop truckStop = new TruckStop(); // Generate the truck stop, i.e. our so-called "parking-lot system"
        for (String line : newLine.split("\n")) {
            String[] command = line.split(" "); // Split the command and its parameters
            if (command[0].equals("create_parking_lot")) {
                truckStop.createParkingLot(Integer.parseInt(command[1]), Integer.parseInt(command[2])); // Create parking lot with said constraints
            }
            if (command[0].equals("delete_parking_lot")) {
                truckStop.deleteParkingLot(Integer.parseInt(command[1])); // Delete parking lot with said constraint
            }
            if (command[0].equals("add_truck")) {
                String toBeWritten = truckStop.addTruck(Integer.parseInt(command[1]), Integer.parseInt(command[2]),0); // Add truck and get return value
                stringBuilder.append(toBeWritten).append("\n"); // Concatenate return value to our string builder
            }
            if (command[0].equals("ready")) {
                String[] toBeWritten = truckStop.ready(Integer.parseInt(command[1])); // Ready truck and get return value
                if (toBeWritten[0].equals("-1")) {
                    stringBuilder.append("-1").append("\n");
                } else {
                    stringBuilder.append(toBeWritten[0]).append(" ").append(toBeWritten[1]).append("\n");
                }
            }
            if (command[0].equals("load")){
                String toBeWritten = truckStop.load(Integer.parseInt(command[1]),Integer.parseInt(command[2])); // Load truck and get return value
                stringBuilder.append(toBeWritten).append("\n");
            }
            if (command[0].equals("count")){
                String toBeWritten = truckStop.count(Integer.parseInt(command[1])); // Count trucks and get return value
                stringBuilder.append(toBeWritten).append("\n");
            }
        }
        Files.writeString(outputPath, stringBuilder.toString(), StandardOpenOption.WRITE); // Write all lines at the same time for minimized time efficiency
    }
}
