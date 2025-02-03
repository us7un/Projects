This demo is a simulation of a feed management system in a hypothetical social media application.
You can give 9 different commands through an input text file (.txt) which perform different operations.
1 - create_user <user_id>
Example: create_user user1 will create a new user with the id "user1". 
Output: "Created user with Id user1." if user is created successfully, "Some error occurred in create_user." if user1 already exists.
2 - follow_user and unfollow_user <user_id1> <user_id2>
Example: follow_user user1 user2 will make user1 follow user2 if the operation is applicable.
Similarly, unfollow_user user1 user2 will make user1 unfollow user2 if the operation is applicable.
Output: "user1 followed/unfollowed user2." if the operation is applicable, "Some error occurred in follow/unfollow_user." else.
3 - create_post <user_id> <post_id> <content>
Example: create_post user1 post1 HelloWorld! will create a post with the content "HelloWorld!" by user1, with the post id of post1 if the operation is applicable.
Output: "user1 created a post with Id post1." if the operation is applicable, "Some error occurred in create_post." else.
4 - see_post <user_id> <post_id>
Example: see_post user1 post1 will make user1 see post1. After this operation, post1 won't show up on user1's feed.
Output: "user1 saw post1." if the operation was successful, "Some error occurred in see_post." else.
5 - see_all_posts_from_user <user_id1> <user_id2>
Example: see_all_posts_from_user user1 user2 will make user1 see all posts made by user2.
Output: "user1 saw all posts of user2." if the operation was successful, "Some error occurred in see_all_posts_from_user." else.
6 - toggle_like <user_id> <post_id>
Example: toggle_like user1 post1 will like post1 if it is unliked, and vice versa if it is liked.
Output: "user1 liked/unliked post1." if the operation was successful, "Some error occurred in toggle_like." else.
7 - generate_feed <user_id> <number>
Example: generate_feed user1 3 will show the most liked 3 posts that user1 has not seen, or until all posts are exhausted if the operation is applicable.
Output: "Feed for user1:
Post ID: post1, Author: user2, Likes: 3
Post ID: post3, Author: user2, Likes: 1
No more posts available for user1." if there are 2 posts in user1's feed, "Some error occurred in generate_feed." else.
8 - scroll_through_feed <user_id> <number> <toggle_like1> <toggle_like2> ...
Example: scroll_through_feed user1 3 1 1 0 will show the most liked 3 posts that user1 has not seen, or until all posts are exhausted;
then will press the like button on the first and second top posts on the feed.
Output: "user1 is scrolling through feed.
user1 saw post1 while scrolling and clicked the like button.
user1 saw post2 while scrolling and clicked the like button.
No more posts in feed." if there are 2 posts in user1's feed, "Some error occurred in scroll_through_feed." else.
9 - sort_posts <user_id>
Example: sort_posts user1 will log all posts created by user1 with respect to their number of likes.
Output: "post1, Likes: 77
post2, Likes 46
post3, Likes 32
... " if user1 has posts, "No posts from user1." if they don't, "Some error occurred in sort_posts." else.
