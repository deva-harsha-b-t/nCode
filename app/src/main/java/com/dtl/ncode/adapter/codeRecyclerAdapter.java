package com.dtl.ncode.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dtl.ncode.R;
import com.dtl.ncode.model.code;

import java.util.List;

public class codeRecyclerAdapter extends RecyclerView.Adapter<codeRecyclerAdapter.myViewHolder> {

    private final Context context;
    private Activity activity;
    private List<code> codes;
    private ClickerHelper cH;

    public codeRecyclerAdapter(List<code> codes, ClickerHelper cH , Context context) {
        this.cH  = cH;
        this.codes = codes;
        this.context = context;
    }

    @NonNull
    @Override
    public codeRecyclerAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.code_row,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull codeRecyclerAdapter.myViewHolder holder, int position) {
        holder.codeText.setText(codes.get(position).getCode());
        Glide.with(context).load(codes.get(position).getCoverURL()).centerCrop().placeholder(R.drawable.ic_baseline_keyboard_arrow_down_24).into(holder.coverImage);
        holder.coverImage.setVisibility(View.GONE);
        holder.read.setVisibility(View.GONE);
        holder.deleteButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return codes.size();
    }
    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView coverImage;
        private TextView codeText;
        private Button read;
        private ClickerHelper clickerHelper = cH;
        private ImageButton showCoverNread;
        private ImageButton deleteButton;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.coverImageVIew);
            codeText = itemView.findViewById(R.id.CodeText);
            read = itemView.findViewById(R.id.readBUtton);
            showCoverNread = itemView.findViewById(R.id.showCoverNread);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(this);
            showCoverNread.setOnClickListener(this);
            read.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.readBUtton) {
                clickerHelper.onClick(codes.get(getAdapterPosition()).getCodeURL());
            }
            else if(id == R.id.showCoverNread){
                clickerHelper.onShow(read,coverImage , deleteButton);
            }
            else if(id == R.id.deleteButton){
                clickerHelper.onDelete(codes.get(getAdapterPosition()).getCode());
            }

        }
    }
    public interface ClickerHelper{
        void onClick(String url);
        void onShow(Button read, ImageView coverImage , ImageButton delete);
        void onDelete(String codeNumber);
    }
}
