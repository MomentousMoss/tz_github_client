package com.momentousmoss.tz_github_client;

public class Commit {
    String commitAuthor, commitDate, commitShortMessage, commitHash;
    Commit(
            String commitAuthor,
            String commitDate,
            String commitShortMessage,
            String commitHash
    ){
        this.commitAuthor = commitAuthor;
        this.commitDate = commitDate;
        this.commitShortMessage = commitShortMessage;
        this.commitHash = commitHash;
    }
}
