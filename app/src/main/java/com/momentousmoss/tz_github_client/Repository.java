package com.momentousmoss.tz_github_client;

import android.graphics.Bitmap;

public class Repository {
    String repositoryName, repositoryDescription, repositoryAuthor;
    Integer repositoryForksCount, repositoryWatchesCount;
    Bitmap repositoryAuthorAvatar;
    Repository(
            String repositoryName,
            String repositoryDescription,
            Integer repositoryForksCount,
            Integer repositoryWatchesCount,
            String repositoryAuthor,
            Bitmap repositoryAuthorAvatar
    ){
        this .repositoryName = repositoryName;
        this .repositoryDescription = repositoryDescription;
        this .repositoryForksCount = repositoryForksCount;
        this .repositoryWatchesCount = repositoryWatchesCount;
        this .repositoryAuthor = repositoryAuthor;
        this .repositoryAuthorAvatar = repositoryAuthorAvatar;
    }
}
