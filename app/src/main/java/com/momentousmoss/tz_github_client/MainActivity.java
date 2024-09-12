package com.momentousmoss.tz_github_client;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.HttpException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends Activity {

    String part = "github_pat_";
    String accessToken = part + "11BLCJQFQ0cwiV5TyEEFET_Y3vko8hwurruoxTAdEhWPmfos0UIKquJZCGEEcHe4q1J2EYC4CXmyagCgpu";
    EditText loginTokenEditText;
    AsyncTask<Object, Object, Integer> asyncLoadTask = new AsyncLoadTask();
    public List<Repository> repositories = new ArrayList<>();
    public RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(repositories);
    private Settings settings;

    final Integer LOAD_SUCCESSFUL = 1;
    final Integer LOAD_CONNECTION_PROBLEM = -1;
    final Integer LOAD_SERVER_PROBLEM = -2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        loginTokenEditText = findViewById(R.id.login_token_edit_text);
        setRepositoriesAdapter();
        loadSharedPreferencesSettings();
        setLoginButtonAction();
    }

    private void loadSharedPreferencesSettings() {
        loginTokenEditText.setText(accessToken); //just for tests

        settings = new Settings(getBaseContext());
        accessToken = settings.getAccessToken();
        if (!accessToken.isEmpty()) {
            loginTokenEditText.setText(accessToken);
        }
    }

    private void setLoginButtonAction() {
        findViewById(R.id.login_button).setOnClickListener((v -> loginAction()));
    }

    private void loginAction() {
        hideLoginView();
        asyncLoadTask.cancel(false);
        repositories.clear();
        repositoriesAdapter.notifyItemRangeChanged(0, repositories.size() - 1);
        accessToken = loginTokenEditText.getText().toString();
        asyncLoadTask = new AsyncLoadTask().execute();
    }

    class AsyncLoadTask extends AsyncTask<Object, Object, Integer> {
        @Override
        protected Integer doInBackground(Object... arg) {
            try {
                GitHub gitHub = authorize();
                if (gitHub.isCredentialValid()) {
                    settings.saveAccessToken(accessToken);
                    GHMyself userData = getUserData(gitHub);
                    Map<String, GHRepository> repos = getRepositories(userData);
                    setReposData(repos);
                } else {
                    return checkConnection(gitHub);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return LOAD_SUCCESSFUL;
        }

        @Override
        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);
            if (Objects.equals(response, LOAD_SUCCESSFUL)) {
                repositoriesAdapter.notifyItemRangeChanged(0, repositories.size() - 1);
                hideLoaderView();
            } else {
                showLoginView();
                showConnectionProblemToast(response);
            }
        }
    }

    private void showConnectionProblemToast(Integer numb) {
        String toastMessage;
        if (Objects.equals(numb, LOAD_CONNECTION_PROBLEM)) {
            toastMessage = getString(R.string.toast_connection_problem);
        } else if (Objects.equals(numb, LOAD_SERVER_PROBLEM)) {
            toastMessage = getString(R.string.toast_server_problem);
        } else {
            toastMessage = getString(R.string.toast_token_problem);
        }
        Toast.makeText(getBaseContext(), toastMessage, Toast.LENGTH_LONG).show();
    }

    private Integer checkConnection(GitHub gitHub) {
        Integer availableConnection = checkAvailableConnection();
        if (availableConnection == null) {
            return checkServerConnection(gitHub);
        } else {
            return availableConnection;
        }
    }

    private Integer checkAvailableConnection() {
        boolean isConnected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if (activeNetworkInfo != null) isConnected = activeNetworkInfo.isConnectedOrConnecting();
        if (!isConnected) return LOAD_CONNECTION_PROBLEM;
        return null;
    }

    private Integer checkServerConnection(GitHub gitHub) {
        try {
            gitHub.checkApiUrlValidity();
        } catch (IOException e) {
            if (Objects.requireNonNull(e.getMessage()).contains(getString(R.string.server_connection_check_response))) {
                return LOAD_SERVER_PROBLEM;
            }
        }
        return null;
    }

    private void hideLoginView() {
        findViewById(R.id.login_container).setVisibility(View.GONE);
        findViewById(R.id.closed_captions).setVisibility(View.GONE);
        showLoaderView();
    }

    private void showLoginView() {
        findViewById(R.id.login_container).setVisibility(View.VISIBLE);
        findViewById(R.id.closed_captions).setVisibility(View.VISIBLE);
        hideLoaderView();
    }

    private void hideLoaderView() {
        findViewById(R.id.loader_layout).setVisibility(View.GONE);
    }

    private void showLoaderView() {
        findViewById(R.id.loader_layout).setVisibility(View.VISIBLE);
    }

    private void setRepositoriesAdapter() {
        RecyclerView repositoriesRecyclerView = findViewById(R.id.rv_repositories_list);
        repositoriesRecyclerView.setAdapter(repositoriesAdapter);
    }

    private GitHub authorize() throws IOException {
        return new GitHubBuilder().withAppInstallationToken(accessToken).build();
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
            List<Commit> repositoryCommits;
            try {
                repositoryCommits = getRepositoryCommits(repository.listCommits().toList());
            } catch (HttpException e) {
                if (Objects.requireNonNull(e.getMessage()).contains(getString(R.string.repository_null_data_check_response))) {
                    repositoryCommits = Collections.emptyList();
                } else {
                    throw new RuntimeException(e);
                }
            }
            Repository repositoryObject = new Repository(
                    repository.getName(),
                    repository.getDescription(),
                    repository.getForksCount(),
                    repository.getWatchersCount(),
                    repository.getOwnerName(),
                    loadAvatar(repository.getOwner().getAvatarUrl()),
                    repositoryCommits
            );
            repositories.add(repositoryObject);
        }
    }

    private List<Commit> getRepositoryCommits(List<GHCommit> repositoryCommits) throws IOException {
        List<Commit> commits = new ArrayList<>();
        for (GHCommit commit : repositoryCommits) {
            String commitAuthor = "";
            if (commit.getAuthor() != null && commit.getAuthor().getLogin() != null) {
                commitAuthor = commit.getAuthor().getLogin();
            }
            Commit commitObject = new Commit(
                    commitAuthor,
                    commit.getCommitDate().toString(),
                    commit.getCommitShortInfo().getMessage(),
                    commit.getSHA1()
            );
            commits.add(commitObject);
        }
        return commits;
    }

    private Bitmap loadAvatar(String avatarUrlString) throws IOException {
        URL avatarUrl = new URL(avatarUrlString);
        return BitmapFactory.decodeStream(avatarUrl.openConnection().getInputStream());
    }

}
