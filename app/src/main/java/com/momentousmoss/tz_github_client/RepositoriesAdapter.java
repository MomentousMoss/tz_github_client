package com.momentousmoss.tz_github_client;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.RepositoriesViewHolder> {

    public static List<Repository> repositories;
    public RepositoriesAdapter(List<Repository> repositories) {
        RepositoriesAdapter.repositories = repositories;
    }

    public static class RepositoriesViewHolder extends RecyclerView.ViewHolder {
        TextView repositoryName, repositoryDescription, repositoryForksCount, repositoryWatchesCount, repositoryAuthor;
        ImageView repositoryAuthorAvatar;
        RepositoriesViewHolder(View itemView) {
            super (itemView);
            repositoryName = (TextView) itemView.findViewById(R.id.repositoryName);
            repositoryDescription = (TextView) itemView.findViewById(R.id.repositoryDescription);
            repositoryForksCount = (TextView) itemView.findViewById(R.id.repositoryForksCount);
            repositoryWatchesCount = (TextView) itemView.findViewById(R.id.repositoryWatchesCount);
            repositoryAuthor = (TextView) itemView.findViewById(R.id.repositoryAuthor);
            repositoryAuthorAvatar = (ImageView) itemView.findViewById(R.id.repositoryAuthorAvatar);
        }
    }

    @NonNull
    @Override
    public RepositoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_repository, viewGroup, false);
        return new RepositoriesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoriesViewHolder repositoriesViewHolder, int i) {
        repositoriesViewHolder.repositoryName.setText("RepoName: " + repositories.get(i).repositoryName);
        if (repositories.get(i).repositoryDescription == null) {
            repositoriesViewHolder.repositoryDescription.setVisibility(View.GONE);
        } else {
            repositoriesViewHolder.repositoryDescription.setVisibility(View.VISIBLE);
            repositoriesViewHolder.repositoryDescription.setText("RepoDescription: " + repositories.get(i).repositoryDescription);
        }
        repositoriesViewHolder.repositoryWatchesCount.setText("Watches: " + repositories.get(i).repositoryForksCount.toString());
        repositoriesViewHolder.repositoryForksCount.setText("Forks: " + repositories.get(i).repositoryWatchesCount.toString());
        repositoriesViewHolder.repositoryAuthor.setText("Created by: \n" + repositories.get(i).repositoryAuthor);
        if (repositories.get(i).repositoryAuthorAvatar == null) {
            repositoriesViewHolder.repositoryAuthorAvatar.setVisibility(View.GONE);
        } else {
            repositoriesViewHolder.repositoryAuthorAvatar.setVisibility(View.VISIBLE);
            repositoriesViewHolder.repositoryAuthorAvatar.setImageBitmap(repositories.get(i).repositoryAuthorAvatar);
        }
    }

    @Override
    public int getItemCount() {
        if (repositories == null) {
            return 0;
        } else {
            return repositories.size();
        }
    }
}
