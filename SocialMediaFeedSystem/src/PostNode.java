/**
 * PostNode class to hold and access the needed properties of a post
 * @author ustunyilmaz
 */
public class PostNode {
    private final String ID;
    private final String content;
    private final String creatorID;
    private final UserHashTable seen;
    private final UserHashTable liked;

    /**
     * Constructor for the PostNode
     * @param ID Unique key of the post
     * @param content Content of the post
     * @param creatorID Unique key of the user that created the post
     */
    public PostNode(String ID, String content, String creatorID) {
        this.ID = ID;
        this.content = content;
        this.creatorID = creatorID;
        this.seen = new UserHashTable();
        this.liked = new UserHashTable();
    }

    /**
     * Method to access the key of the creator of the post
     * @return ID of the post's creator
     */
    public String getCreatorID() {
        return creatorID;
    }

    /**
     * Method to get how many users have liked the post
     * @return Size of the liked users' hashTable
     */
    public int getLikes() {
        return liked.getSize();
    }

    /**
     * Method to get ID of the post
     * @return ID of the post
     */
    public String getID() {
        return ID;
    }

    /**
     * Method to get the hashTable of users that have seen the post
     * @return Users that have seen the post
     */
    public UserHashTable getSeen() {
        return seen;
    }

    /**
     * Method to get the hashTable of users that have liked the post
     * @return Users that have liked the post
     */
    public UserHashTable getLiked() {
        return liked;
    }
}
