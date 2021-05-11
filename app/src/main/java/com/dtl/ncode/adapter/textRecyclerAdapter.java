package com.dtl.ncode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtl.ncode.R;
import com.dtl.ncode.model.text;

import java.util.List;

public class textRecyclerAdapter extends RecyclerView.Adapter<textRecyclerAdapter.myViewHolder> {
    List<text> texts;
    private TextClickerHelper textClickerHelper;

    public textRecyclerAdapter(List<text> texts,TextClickerHelper textClickerHelper) {
        this.textClickerHelper = textClickerHelper;
        this.texts = texts;
    }

    @NonNull
    @Override
    public textRecyclerAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull textRecyclerAdapter.myViewHolder holder, int position) {
        holder.title.setText(texts.get(position).getTitle());
        holder.desc.setText(texts.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }
    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private TextView desc;
        private ImageButton deleteButton;
        private TextClickerHelper TextcH;
        private ImageButton copyButton;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            TextcH = textClickerHelper;
            title = itemView.findViewById(R.id.row_title);
            desc = itemView.findViewById(R.id.row_desc);
            deleteButton = itemView.findViewById(R.id.deleteText);
            copyButton = itemView.findViewById(R.id.copyText);
            deleteButton.setOnClickListener(this);
            copyButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.deleteText)
                TextcH.delteText(texts.get(getAdapterPosition()).getTitle());
            else if(v.getId() == R.id.copyText)
                TextcH.copyTextToClip(texts.get(getAdapterPosition()).getDescription());
        }
    }
    public interface TextClickerHelper{
        public void delteText(String title_forQuery);
        public void copyTextToClip(String copyContent);
    }
}
