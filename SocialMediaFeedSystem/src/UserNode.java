import java.util.ArrayList;

/**
 * UserNode class hold and access the needed properties of a user
 *
 * @author ustunyilmaz
 */
public class UserNode {
    private final String ID;
    private final ArrayList<PostNode> posts;
    private final UserHashTable followers;
    private final UserHashTable following;

    /**
     * Constructor method for UserNode
     *
     * @param ID Unique key of the user
     */
    public UserNode(String ID) {
        this.ID = ID;
        this.posts = new ArrayList<>();
        this.followers = new UserHashTable();
        this.following = new UserHashTable();
    }

    /**
     * Method to get the unique key corresponding to the user
     *
     * @return ID of the user
     */
    public String getID() {
        return ID;
    }

    /**
     * Method to get the arrayList consisting of posts created by the user
     *
     * @return ArrayList of posts created by the user
     */
    public ArrayList<PostNode> getPosts() {
        return posts;
    }

    /**
     * Method to get the followers of the user
     *
     * @return HashTable consisting of followers of the user
     */
    public UserHashTable getFollowers() {
        return followers;
    }

    /**
     * Method to get the users that the user follows
     *
     * @return HashTable consisting of followed users
     */
    public UserHashTable getFollowing() {
        return following;
    }
}
