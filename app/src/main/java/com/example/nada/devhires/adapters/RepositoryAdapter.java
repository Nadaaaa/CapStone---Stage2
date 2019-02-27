package com.example.nada.devhires.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nada.devhires.R;
import com.example.nada.devhires.models.Repository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {

    private Context mContext;
    private List<Repository> mRepositoryList;

    public RepositoryAdapter(Context context, List<Repository> repositoryList) {
        mContext = context;
        mRepositoryList = repositoryList;
    }

    @NonNull
    @Override
    public RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutRepositoryItem = R.layout.item_project;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutRepositoryItem, viewGroup, false);
        return new RepositoryViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryViewHolder repositoryViewHolder, int i) {
        repositoryViewHolder.bind(mRepositoryList.get(i));
    }

    @Override
    public int getItemCount() {
        return mRepositoryList.size();
    }


    class RepositoryViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.project_name)
        TextView textView_repositoryName;

        @BindView(R.id.project_description)
        TextView textView_repositoryDescription;

        @BindView(R.id.project_url)
        TextView textView_repositoryURL;

        //Vars
        Context context;

        RepositoryViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            this.context = context;
        }

        void bind(final Repository repository) {
            textView_repositoryName.setText(repository.getName());
            if (repository.getDescription() != null) {
                textView_repositoryDescription.setText(repository.getDescription());
            } else {
                textView_repositoryDescription.setVisibility(View.GONE);
            }
            textView_repositoryURL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(repository.gethtml_url()));
                    context.startActivity(urlIntent);
                }
            });
        }

    }
}
