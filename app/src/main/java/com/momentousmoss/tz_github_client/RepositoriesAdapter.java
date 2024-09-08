package com.momentousmoss.tz_github_client;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        RecyclerView commitsRecyclerView;
        List<Commit> commits;
        CommitsAdapter commitsAdapter;
        RepositoriesViewHolder(View itemView) {
            super (itemView);
            repositoryName = itemView.findViewById(R.id.repositoryName);
            repositoryDescription = itemView.findViewById(R.id.repositoryDescription);
            repositoryForksCount = itemView.findViewById(R.id.repositoryForksCount);
            repositoryWatchesCount = itemView.findViewById(R.id.repositoryWatchesCount);
            repositoryAuthor = itemView.findViewById(R.id.repositoryAuthor);
            repositoryAuthorAvatar = itemView.findViewById(R.id.repositoryAuthorAvatar);
            commitsRecyclerView = itemView.findViewById(R.id.rv_commits_list);
        }
    }

    @NonNull
    @Override
    public RepositoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new RepositoriesViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_repository,
                viewGroup,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoriesViewHolder vh, int i) {
        Resources resources = vh.itemView.getContext().getResources();
        vh.repositoryName.setText(resources.getString(R.string.layout_repository_name, repositories.get(i).repositoryName));
        if (repositories.get(i).repositoryDescription == null) {
            vh.repositoryDescription.setVisibility(View.GONE);
        } else {
            vh.repositoryDescription.setVisibility(View.VISIBLE);
            vh.repositoryDescription.setText(resources.getString(R.string.layout_repository_description,repositories.get(i).repositoryDescription));
        }
        vh.repositoryWatchesCount.setText(resources.getString(R.string.layout_repository_watches, repositories.get(i).repositoryForksCount.toString()));
        vh.repositoryForksCount.setText(resources.getString(R.string.layout_repository_forks, repositories.get(i).repositoryWatchesCount.toString()));
        vh.repositoryAuthor.setText(resources.getString(R.string.layout_repository_author, repositories.get(i).repositoryAuthor));
        if (repositories.get(i).repositoryAuthorAvatar == null) {
            vh.repositoryAuthorAvatar.setVisibility(View.GONE);
        } else {
            vh.repositoryAuthorAvatar.setVisibility(View.VISIBLE);
            vh.repositoryAuthorAvatar.setImageBitmap(repositories.get(i).repositoryAuthorAvatar);
        }

        if (repositories.get(i).repositoryCommits.isEmpty()) {
            vh.commitsRecyclerView.setVisibility(View.GONE);
        } else {
            vh.commitsRecyclerView.setVisibility(View.VISIBLE);
        }
        vh.commits = repositories.get(i).repositoryCommits;
        vh.commitsAdapter = new CommitsAdapter(vh.commits);
        vh.commitsRecyclerView.setAdapter(vh.commitsAdapter);
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
