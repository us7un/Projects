import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Class to manage the whole user operation handling and feed generation/sorting operations in the proposed social media application.
 *
 * @author ustunyilmaz
 */
public class FeedManagementApp {
    // Initialize the needed structures and random number generator
    UserHashTable users = new UserHashTable();
    PostHashTable posts = new PostHashTable();
    Random random = new Random();

    /**
     * Method to create a user in the feedManagementApp
     *
     * @param ID The unique key of a user
     * @return A creation successful message if user is created successfully, else an error message.
     */
    public String createUser(String ID) {
        try {
            users.insert(new UserNode(ID.trim()));
            return "Created user with Id " + ID.trim() + ".";
        } catch (IllegalArgumentException alreadyExistsException) {
            return "Some error occurred in create_user.";
        }
    }

    /**
     * Method to make a user follow another
     *
     * @param ID1 Unique key of the user we want to operate on
     * @param ID2 Unique key of the user we want the operated user to follow
     * @return A follow successful message if the user is followed successfully, else an error message.
     */
    public String followUser(String ID1, String ID2) {
        try {
            UserNode user1 = users.get(ID1.trim());
            UserNode user2 = users.get(ID2.trim());
            // You cannot follow yourself
            if (user1.equals(user2)) {
                return "Some error occurred in follow_user.";
            }
            user1.getFollowing().insert(user2);
            user2.getFollowers().insert(user1);
            return ID1.trim() + " followed " + ID2.trim() + ".";
        } catch (IllegalArgumentException | NoSuchElementException exception) {
            return "Some error occurred in follow_user.";
        }
    }

    /**
     * Method to make a user unfollow another if they are already following so
     *
     * @param ID1 Unique key of the user we want to operate on
     * @param ID2 Unique key of the user we want the operated user to unfollow
     * @return An unfollow successful message if the user is unfollowed successfully, else an error message.
     */
    public String unfollowUser(String ID1, String ID2) {
        try {
            UserNode user1 = users.get(ID1.trim());
            UserNode user2 = users.get(ID2.trim());
            user1.getFollowing().delete(user2);
            user2.getFollowers().delete(user1);
            return ID1.trim() + " unfollowed " + ID2.trim() + ".";
        } catch (IllegalArgumentException | NoSuchElementException exception) {
            return "Some error occurred in unfollow_user.";
        }
    }

    /**
     * Method to make a user create a post of their desire
     *
     * @param userID  Unique key of the user we want to operate on
     * @param postID  Unique key of the post that user creates
     * @param content Content of the post
     * @return A creation successful message if the post is created successfully, else an error message.
     */
    public String createPost(String userID, String postID, String content) {
        try {
            UserNode user = users.get(userID.trim());
            PostNode post = new PostNode(postID.trim(), content.trim(), userID.trim());
            // Insert the post both to the hashTable and user's arrayList of posts
            posts.insert(post);
            user.getPosts().add(post);
            return userID.trim() + " created a post with Id " + postID.trim() + ".";
        } catch (IllegalArgumentException | NoSuchElementException exception) {
            return "Some error occurred in create_post.";
        }
    }

    /**
     * Method to make a user see a certain post of their desire
     *
     * @param userID Unique key of the user we want to operate on
     * @param postID Unique key of the post that user sees
     * @return A successfully seen message if the post is seen successfully, else an error message.
     */
    public String seePost(String userID, String postID) {
        try {
            UserNode user = users.get(userID.trim());
            PostNode post = posts.get(postID.trim());
            // If the user hasn't already seen the post, also insert them to the post's seen table
            if (!post.getSeen().has(userID.trim())) post.getSeen().insert(user);
            return userID.trim() + " saw " + postID.trim() + ".";

        } catch (IllegalArgumentException | NoSuchElementException exception) {
            return "Some error occurred in see_post.";
        }
    }

    /**
     * Method to make a user see all posts of another certain user
     *
     * @param viewerID Unique key of the user we want to operate on
     * @param viewedID Unique key of the user that the operated user sees all posts of
     * @return A successfully seen message if the post is seen successfully, else an error message.
     */
    public String seeAllPosts(String viewerID, String viewedID) {
        try {
            UserNode viewed = users.get(viewedID.trim());
            UserNode viewer = users.get(viewerID.trim());
            for (PostNode post : viewed.getPosts()) {
                seePost(viewer.getID().trim(), post.getID().trim());
            }
            return viewerID.trim() + " saw all posts of " + viewedID.trim() + ".";
        } catch (IllegalArgumentException | NoSuchElementException exception) {
            return "Some error occurred in see_all_posts_from_user.";
        }
    }

    /**
     * Method to make a user click the like button on a certain post
     *
     * @param userID Unique key of the user we want to operate on
     * @param postID Unique key of the post we want to operate on
     * @return A successfully liked/unliked message if the operation is successful, else an error message.
     */
    public String toggleLike(String userID, String postID) {
        try {
            PostNode post = posts.get(postID.trim());
            UserNode user = users.get(userID.trim());
            // Like the post
            if (!post.getLiked().has(userID.trim())) {
                if (!post.getSeen().has(userID.trim())) {
                    seePost(userID.trim(), postID.trim());
                }
                post.getLiked().insert(user);
                return userID.trim() + " liked " + postID.trim() + ".";
            }
            // Unlike the post
            else {
                if (!post.getSeen().has(userID.trim())) {
                    seePost(userID.trim(), postID.trim());
                }
                post.getLiked().delete(user);
                return userID.trim() + " unliked " + postID.trim() + ".";
            }
        } catch (IllegalArgumentException | NoSuchElementException exception) {
            return "Some error occurred in toggle_like.";
        }
    }

    /**
     * Method to generate feed consisting of certain number of posts for a certain user (just for generation, no other operations involved)
     *
     * @param userID Unique key of the user we want to operate on
     * @param number The number of posts we want to "pull" from the whole feed
     * @return The posts in the feed with their properties if the operation is successful, else an error message.
     */
    public String generateFeed(String userID, int number) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            UserNode user = users.get(userID.trim());
            UserNode[] followed = user.getFollowing().getAllContents(); // A user's feed only consists of posts from people they follow
            ArrayList<PostNode> unseenPosts = new ArrayList<>();
            // Add all the unseen posts to the feed arrayList
            for (UserNode followedUser : followed) {
                for (PostNode post : followedUser.getPosts()) {
                    if (!post.getSeen().has(userID.trim())) {
                        unseenPosts.add(post);
                    }
                }
            }
            // Build the max-heap for generating the feed in a sorted-by-likes manner
            PostNode[] unseen = unseenPosts.toArray(new PostNode[0]);
            PostMaxHeap feed = new PostMaxHeap(unseen);
            // Generate posts until "number" is reached
            stringBuilder.append("Feed for ").append(userID.trim()).append(":").append("\n");
            int iterations = 0;
            while (!feed.isEmpty() && iterations != number) {
                PostNode post = feed.deleteMax();
                stringBuilder.append("Post ID: ").append(post.getID().trim()).append(", Author: ").append(post.getCreatorID().trim()).append(", Likes: ").append(post.getLikes()).append("\n");
                iterations++;
            }
            // If there aren't any more posts, add the below message
            if (iterations < number) {
                stringBuilder.append("No more posts available for ").append(userID.trim()).append(".");
            }
            // Remove the extra newline
            if (stringBuilder.charAt(stringBuilder.length() - 1) == '\n') {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            return stringBuilder.toString();
        } catch (IllegalArgumentException | NoSuchElementException exception) {
            return "Some error occurred in generate_feed.";
        }
    }

    /**
     * Method to make a user perform operations through their feed
     *
     * @param userID     Unique key of the user we want to operate on
     * @param number     The number of operations the user will perform
     * @param likeNumber The array consisting of 1s and 0s to determine if the user is to click the like button or not
     * @return All the operations of the user if successful, else an error message.
     */
    public String scrollThruFeed(String userID, int number, int[] likeNumber) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            // The same operations as feed generation
            UserNode user = users.get(userID.trim());
            UserNode[] followed = user.getFollowing().getAllContents();
            ArrayList<PostNode> unseenPosts = new ArrayList<>();
            for (UserNode followedUser : followed) {
                for (PostNode post : followedUser.getPosts()) {
                    if (!post.getSeen().has(userID.trim())) {
                        unseenPosts.add(post);
                    }
                }
            }
            PostNode[] unseen = unseenPosts.toArray(new PostNode[0]);
            PostMaxHeap feed = new PostMaxHeap(unseen);
            // Click like button according to the user's preference while scrolling through the feed
            stringBuilder.append(userID.trim()).append(" is scrolling through feed:").append("\n");
            int iterations = 0;
            while (!feed.isEmpty() && iterations != number) {
                PostNode post = feed.deleteMax();
                seePost(user.getID(), post.getID());
                if (likeNumber[iterations] == 1) {
                    toggleLike(user.getID(), post.getID());
                    stringBuilder.append(user.getID()).append(" saw ").append(post.getID()).append(" while scrolling and clicked the like button.").append("\n");
                } else {
                    stringBuilder.append(user.getID()).append(" saw ").append(post.getID()).append(" while scrolling.").append("\n");
                }
                iterations++;
            }
            // If there aren't any more posts, add the below message
            if (iterations < number) {
                stringBuilder.append("No more posts in feed.");
            }
            // Remove the extra newline
            if (stringBuilder.charAt(stringBuilder.length() - 1) == '\n') {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            return stringBuilder.toString();
        } catch (IllegalArgumentException | NoSuchElementException exception) {
            return "Some error occurred in scroll_through_feed.";
        }
    }

    /**
     * Method to efficiently sort a user's posts according to their likes using quicksort with Hoare's partition
     *
     * @param userID Unique key of the user we want to operate on
     * @return The sorted posts with their respective properties if successful, else an error message.
     */
    public String sortPosts(String userID) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            UserNode user = users.get(userID.trim());
            PostNode[] userPosts = user.getPosts().toArray(new PostNode[0]);
            // No posts to sort
            if (userPosts.length == 0) {
                return "No posts from " + userID.trim() + ".";
            }
            stringBuilder.append("Sorting ").append(userID.trim()).append("'s posts:").append("\n");
            quickSort(userPosts, 0, userPosts.length - 1);
            // The most liked post comes first
            for (int i = userPosts.length - 1; i >= 0; i--) {
                stringBuilder.append(userPosts[i].getID()).append(", Likes: ").append(userPosts[i].getLikes()).append("\n");
            }
            // Remove the extra newline
            if (stringBuilder.charAt(stringBuilder.length() - 1) == '\n') {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            }
            return stringBuilder.toString();
        } catch (IllegalArgumentException | NoSuchElementException exception) {
            return "Some error occurred in sort_posts.";
        }
    }

    /**
     * Quicksort driver method for the Hoare partition with random pivoting implementation
     *
     * @param posts Array of PostNode objects
     * @param start Index we want to start sorting from
     * @param end   Index we want to end sorting at
     */
    private void quickSort(PostNode[] posts, int start, int end) {
        // Partition the array with respect to the pivot and sort the parts recursively
        if (start < end) {
            int partitionedIndex = partitionRandomly(posts, start, end); // Index of the randomly selected pivot
            quickSort(posts, start, partitionedIndex - 1); // Sort the left of the pivot
            quickSort(posts, partitionedIndex + 1, end); // Sort the right of the pivot
        }
    }

    /**
     * Method to randomly choose a pivot to partition the array in the quicksort algorithm
     *
     * @param posts Array of PostNode objects
     * @param start Index we want to start partitioning from
     * @param end   Index we want to end partitioning at
     * @return Final position of the pivot after the partition is rearranged
     */
    private int partitionRandomly(PostNode[] posts, int start, int end) {
        int pivot = random.nextInt(end - start + 1) + start; // Randomly choose the pivot

        // Get the pivot out of the way right to the end to partition easily
        PostNode temp = posts[pivot];
        posts[pivot] = posts[end];
        posts[end] = temp;

        // Rearrange elements around the pivot to get its final position
        return partition(posts, start, end);
    }

    /**
     * Method to perform the key part of the quicksort algorithm, which is to keep swapping elements until small and large pointers cross
     *
     * @param posts Array of PostNode objects
     * @param start Index we want to start partitioning from
     * @param end   Index we want to end partitioning at
     * @return Final position of the pivot after the partition
     */
    private int partition(PostNode[] posts, int start, int end) {
        // The pivot is always assumed to be at the end index since this method is always called inside PartitionRandomly
        PostNode pivot = posts[end];
        int small = start - 1; // The small pointer starts from outside the array for easier usage in the for loop

        // Keep swapping until large and small pointers cross
        for (int large = start; large < end; large++) {
            if (compare(posts[large], pivot) > 0) {
                small++;
                PostNode temp = posts[small];
                posts[small] = posts[large];
                posts[large] = temp;
            }
        }

        // Put the pivot back in its place
        PostNode temp = posts[small + 1];
        posts[small + 1] = posts[end];
        posts[end] = temp;

        // Return the final position of the pivot
        return small + 1;
    }

    /**
     * Method to compare two posts according to their likes and if they have the same likes, the lexicographically greater one prevails
     *
     * @param post1 Unique key of the first post
     * @param post2 Unique key of the second post
     * @return A positive value if post2 is "greater than" post1 in any sense
     */
    private int compare(PostNode post1, PostNode post2) {
        int likeDifference = post2.getLikes() - post1.getLikes();
        if (likeDifference != 0) {
            return likeDifference;
        }
        return post2.getID().compareTo(post1.getID());
    }
}
