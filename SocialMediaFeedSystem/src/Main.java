import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * Driver code for the feed management system of a proposed social media application. Having operations like creating users and posts,
 * managing and sorting them efficiently. Usage of the new input/output (java.nio) library is essential for efficient reading and writing.
 *
 * @author ustunyilmaz
 * @version 1.0.0
 * @since Nov. 11, 2024
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Path input = Path.of(args[0]); // Get path of input file
        Path outputPath = Path.of(args[1]); // Path of output file to be created and written to
        Files.writeString(outputPath, "", StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING); // Clear file if already exists, if not create it
        String newLine = Files.readString(input); // Get all new lines from input file
        StringBuilder stringBuilder = new StringBuilder(); // Efficiently concatenate strings to each other
        // Take and execute commands line by line
        FeedManagementApp feedManagementApp = new FeedManagementApp(); // Generate the social media management system
        for (String line : newLine.split("\n")) {
            String[] command = line.split(" "); // Split the command and its parameters
            if (command[0].equals("create_user")) {
                String toBeWritten = feedManagementApp.createUser(command[1]);
                stringBuilder.append(toBeWritten).append("\n"); // Concatenate to the output and continue
            }
            if (command[0].equals("follow_user")) {
                String toBeWritten = feedManagementApp.followUser(command[1], command[2]);
                stringBuilder.append(toBeWritten).append("\n");
            }
            if (command[0].equals("unfollow_user")) {
                String toBeWritten = feedManagementApp.unfollowUser(command[1], command[2]);
                stringBuilder.append(toBeWritten).append("\n");
            }
            if (command[0].equals("create_post")) {
                String toBeWritten = feedManagementApp.createPost(command[1], command[2], command[3]);
                stringBuilder.append(toBeWritten).append("\n");
            }
            if (command[0].equals("see_post")) {
                String toBeWritten = feedManagementApp.seePost(command[1], command[2]);
                stringBuilder.append(toBeWritten).append("\n");
            }
            if (command[0].equals("see_all_posts_from_user")) {
                String toBeWritten = feedManagementApp.seeAllPosts(command[1], command[2]);
                stringBuilder.append(toBeWritten).append("\n");
            }
            if (command[0].equals("toggle_like")) {
                String toBeWritten = feedManagementApp.toggleLike(command[1], command[2]);
                stringBuilder.append(toBeWritten).append("\n");
            }
            if (command[0].equals("generate_feed")) {
                String toBeWritten = feedManagementApp.generateFeed(command[1], Integer.parseInt(command[2].trim()));
                stringBuilder.append(toBeWritten).append("\n");
            }
            if (command[0].equals("scroll_through_feed")) {
                // Since the command size is arbitrary, handle it through a for-loop
                int[] likeNumber = new int[Integer.parseInt(command[2].trim())];
                for (int i = 0; i < Integer.parseInt(command[2].trim()); i++) {
                    likeNumber[i] = Integer.parseInt(command[3 + i].trim());
                }
                String toBeWritten = feedManagementApp.scrollThruFeed(command[1], Integer.parseInt(command[2].trim()), likeNumber);
                stringBuilder.append(toBeWritten).append("\n");
            }
            if (command[0].equals("sort_posts")) {
                String toBeWritten = feedManagementApp.sortPosts(command[1]);
                stringBuilder.append(toBeWritten).append("\n");
            }
        }
        Files.writeString(outputPath, stringBuilder.toString(), StandardOpenOption.WRITE); // Write all lines at the same time for minimized time efficiency
    }
}