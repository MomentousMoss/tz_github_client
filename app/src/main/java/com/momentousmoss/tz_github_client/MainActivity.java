package com.momentousmoss.tz_github_client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String access_token = "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";

    String personal_token = "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";
    String userLogin = "TestTZ123Name";
    String userId = "180656150";
    String userMail = "atlassianfortest1@gmail.com";
    String userPassword = "TestTZ123";
    String UserNodeId = "U_kgDOCsSYFg";

    String appId = "990205";
    String clientId = "Iv23ctj7JYTegy7Cc2Rm";
    String clientSecret = "1b0957a46c1fb216472e277fdc7610a6cc144ba0";
    String note = "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";
    String note_url = "https://github.com/settings/tokens/1745338480";
    String[] scopes = {""};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Thread task = new Thread(() -> {
            try {
                GitHub gitHub = authorize();
                GHMyself userData = getUserData(gitHub);

                Map<String, GHRepository> repos = getRepositories(userData);
                setReposData(repos);

                Bitmap avatar = loadAvatar(userData);
                setAvatar(avatar);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        task.start();
    }

    private GitHub authorize() throws IOException {
        return new GitHubBuilder().withAppInstallationToken(access_token).build();
    }

    private GHMyself getUserData(GitHub gitHub) throws IOException {
        return gitHub.getMyself();
    }

    private Map<String, GHRepository> getRepositories(GHMyself userData) throws IOException {
        return userData.getAllRepositories();
    }

    private void setReposData(Map<String, GHRepository> repos) throws IOException {
        GHRepository[] repositoriesArray = repos.values().toArray(new GHRepository[0]);
        for (GHRepository repository : repositoriesArray) {
            String repositoryName = repository.getName();
            String repositoryDescription = repository.getDescription();
            String repositoryAuthor = repository.getOwnerName();
            int repositoryForksCount = repository.getForksCount();
            int repositoryWatchesCount = repository.getWatchersCount();

            List<GHCommit> repositoryCommits = repository.listCommits().toList();
            setCommitsData(repositoryCommits);
        }
    }

    private void setCommitsData(List<GHCommit> repositoryCommits) throws IOException {
        for (GHCommit commit : repositoryCommits) {
            String hash = commit.getSHA1();
            String shortMessage = commit.getCommitShortInfo().getMessage();
            String author = commit.getAuthor().getLogin();
            Date date = commit.getCommitDate();
        }
    }

    private Bitmap loadAvatar(GHMyself userData) throws IOException {
        URL url = new URL(userData.getAvatarUrl());
        return BitmapFactory.decodeStream(url.openConnection().getInputStream());
    }

    private void setAvatar(Bitmap avatar) {

    }

}
