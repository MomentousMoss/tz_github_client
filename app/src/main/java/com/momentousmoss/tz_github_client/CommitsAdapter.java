package com.momentousmoss.tz_github_client;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommitsAdapter extends RecyclerView.Adapter<CommitsAdapter.CommitsViewHolder> {

    public static List<Commit> commits;
    public CommitsAdapter(List<Commit> commits) {
        CommitsAdapter.commits = commits;
    }

    public static class CommitsViewHolder extends RecyclerView.ViewHolder {
        TextView commitAuthor, commitDate, commitShortMessage, commitHash;
        CommitsViewHolder(View itemView) {
            super (itemView);
            commitAuthor = itemView.findViewById(R.id.commitAuthor);
            commitDate = itemView.findViewById(R.id.commitDate);
            commitShortMessage = itemView.findViewById(R.id.commitShortMessage);
            commitHash = itemView.findViewById(R.id.commitHash);
        }
    }

    @NonNull
    @Override
    public CommitsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new CommitsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_commit,
                viewGroup,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull CommitsViewHolder vh, int i) {
        Resources resources = vh.itemView.getContext().getResources();
        vh.commitAuthor.setText(resources.getString(R.string.layout_commit_author, commits.get(i).commitAuthor));
        vh.commitDate.setText(resources.getString(R.string.layout_commit_date, commits.get(i).commitDate));
        vh.commitShortMessage.setText(resources.getString(R.string.layout_commit_short_message, commits.get(i).commitShortMessage));
        vh.commitHash.setText(resources.getString(R.string.layout_commit_hash, commits.get(i).commitHash));
    }

    @Override
    public int getItemCount() {
        if (commits == null) {
            return 0;
        } else {
            return commits.size();
        }
    }
}
