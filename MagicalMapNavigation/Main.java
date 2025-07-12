import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * Driver code for the flexible shortest-path finding algorithm for the project 'Magical Map' in CMPE250 course of Bogazici University.
 *
 * @author ustunyilmaz
 * @since Dec. 3, 2024
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // Get the input files, one by one
        Path gridPath = Path.of(args[0]);
        Path timesPath = Path.of(args[1]);
        Path objectivePath = Path.of(args[2]);

        // If it does not exist, create the output file, else erase its contents to prepare it for the writing process
        Path outputPath = Path.of(args[3]);
        Files.writeString(outputPath, "", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        // Parse and process the grid file, containing information about the grid and its node types
        String gridContent = Files.readString(gridPath);
        String[] gridLines = gridContent.split("\n");
        String[] gridSize = gridLines[0].split(" ");
        int width = Integer.parseInt(gridSize[0]);
        int height = Integer.parseInt(gridSize[1]);
        NodeRepository repository = new NodeRepository(width, height); // Generate the node repository for easier grid handling (see the NodeRepository class)
        // Initialize and add all nodes to the repository
        for (int i = 1; i < gridLines.length; i++) {
            String[] nodeDetails = gridLines[i].split(" ");
            // Get the coordinates of the node and add it to the repository
            int xCoordinate = Integer.parseInt(nodeDetails[0]);
            int yCoordinate = Integer.parseInt(nodeDetails[1]);
            int nodeType = Integer.parseInt(nodeDetails[2]);
            repository.addNode(xCoordinate, yCoordinate, nodeType);
        }

        // Parse and process the edges file, containing information about the edges and their weights
        String edgeContent = Files.readString(timesPath);
        String[] edgeLines = edgeContent.split("\n");
        for (String line : edgeLines) {
            String[] details = line.split(" ");
            String[] nodes = details[0].split(",");
            String[] node1Coordinates = nodes[0].split("-");
            String[] node2Coordinates = nodes[1].split("-");
            String edgeWeight = details[1];
            // Get the coordinates of the corresponding nodes and parse the edgeWeight
            int node1X = Integer.parseInt(node1Coordinates[0]);
            int node1Y = Integer.parseInt(node1Coordinates[1]);
            int node2X = Integer.parseInt(node2Coordinates[0]);
            int node2Y = Integer.parseInt(node2Coordinates[1]);
            double travelTime = Double.parseDouble(edgeWeight);
            Node node1 = repository.getNode(node1X, node1Y);
            Node node2 = repository.getNode(node2X, node2Y);
            // Add the edge between its corresponding nodes
            if (node1 != null && node2 != null) {
                node1.addEdge(node2, travelTime);
                node2.addEdge(node1, travelTime);
            }
        }

        // Parse and process the objectives file, containing information about the objectives and so-called 'choices'
        String objectiveContent = Files.readString(objectivePath);
        String[] objectiveLines = objectiveContent.split("\n");
        ArrayList<Objective> objectives = new ArrayList<>(); // Objectives have their respective arrayList for easier handling (see Objective class)
        int radiusOfSight = Integer.parseInt(objectiveLines[0]); // Radius of nodes we can 'see'
        String[] sourceDetails = objectiveLines[1].split(" ");
        // Get the coordinates of the starting source and destination after isolating them, then initialize the objective
        int sourceX = Integer.parseInt(sourceDetails[0]);
        int sourceY = Integer.parseInt(sourceDetails[1]);
        String[] firstObjectiveDetails = objectiveLines[2].split(" ");
        int firstObjectiveX = Integer.parseInt(firstObjectiveDetails[0]);
        int firstObjectiveY = Integer.parseInt(firstObjectiveDetails[1]);
        Node firstObjective = repository.getNode(firstObjectiveX, firstObjectiveY);
        // If the objective is of type-1, it does not have any corresponding choices, simply add it
        if (firstObjectiveDetails.length == 2) {
            objectives.add(new Objective(repository.getNode(sourceX, sourceY), firstObjective));
        }
        // The case where the objective has corresponding choices, we parse them separately in a for loop, then add it
        else {
            ArrayList<Integer> wizardsChoices = new ArrayList<>();
            for (int i = 2; i < firstObjectiveDetails.length; i++) {
                wizardsChoices.add(Integer.parseInt(firstObjectiveDetails[i]));
            }
            objectives.add(new Objective(repository.getNode(sourceX, sourceY), firstObjective, wizardsChoices));
        }
        Node nextSource = firstObjective; // Since the input type is giving the choices for the next objective, we need to isolate the first objective

        // After isolating the first objective, parse all the others in a for loop with the same assumptions as before
        for (int i = 3; i < objectiveLines.length; i++) {
            String[] nodeDetails = objectiveLines[i].split(" ");
            int nodeX = Integer.parseInt(nodeDetails[0]);
            int nodeY = Integer.parseInt(nodeDetails[1]);
            Node targetNode = repository.getNode(nodeX, nodeY);
            if (nodeDetails.length == 2) {
                objectives.add(new Objective(nextSource, targetNode));
            } else {
                ArrayList<Integer> wizardsChoices = new ArrayList<>();
                for (int j = 2; j < nodeDetails.length; j++) {
                    wizardsChoices.add(Integer.parseInt(nodeDetails[j]));
                }
                objectives.add(new Objective(nextSource, targetNode, wizardsChoices));
            }
            nextSource = targetNode;
        }

        // Initialize the pathfinding algorithm (see Dijkstra class) and the corresponding stringBuilder
        Dijkstra dijkstra = new Dijkstra(repository, radiusOfSight);
        StringBuilder toBeWritten = new StringBuilder();

        // For the first objective, choiceArray will always be null, and the iteration will only run the pathfinding algorithm
        ArrayList<Integer> choiceArray = null;
        for (int i = 0; i < objectives.size(); i++) {
            if (i != 0 && choiceArray != null) {
                dijkstra.bestPossibleChoice(toBeWritten, objectives.get(i).getSource(), objectives.get(i).getDestination(), choiceArray); // Select the best possible choice if the previous objective had choices corresponding to it
            }
            dijkstra.algorithm(i + 1, toBeWritten, objectives.get(i).getSource(), objectives.get(i).getDestination()); // Run the pathfinding algorithm to find the shortest path

            // If current objective has choices for the next one, add them to the array
            if (objectives.get(i).isChoicer()) {
                choiceArray = objectives.get(i).getChoices();
            } else {
                choiceArray = null; // Otherwise reset the array
            }
        }

        Files.writeString(outputPath, toBeWritten.toString()); // Write the contents of the output file
    }
}