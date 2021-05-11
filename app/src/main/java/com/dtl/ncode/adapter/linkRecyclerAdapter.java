//deprecated by me


package com.dtl.ncode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtl.ncode.R;
import com.dtl.ncode.model.link;

import java.util.List;

public class linkRecyclerAdapter extends RecyclerView.Adapter<linkRecyclerAdapter.myViewHolder> {
    List<link> links;

    public linkRecyclerAdapter(List<link> links) {
        this.links = links;
    }

    @NonNull
    @Override
    public linkRecyclerAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_row,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull linkRecyclerAdapter.myViewHolder holder, int position) {
        holder.linkTitle.setText(links.get(position).getTitleForLink());
        holder.linkUrl.setText(links.get(position).getLinkURL());
    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        private TextView linkTitle;
        private TextView linkUrl;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            linkTitle = itemView.findViewById(R.id.linkTitleID);
            linkUrl = itemView.findViewById(R.id.linkUrlID);
        }
    }
}
