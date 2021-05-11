package com.dtl.ncode.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dtl.ncode.R;
import com.dtl.ncode.model.image;

import java.util.List;

public class imageRVadapter extends RecyclerView.Adapter<imageRVadapter.myViewHolder> {
    private Context context;
    private List<image> imageList;
    private imageClickerHelper clickerHelper;

    public imageRVadapter(Context context , List<image> imageList , imageClickerHelper clickerHelper) {
        this.context = context;
        this.clickerHelper = clickerHelper;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_row,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Glide.with(context).load(imageList.get(position).getImageUrl()).centerCrop().placeholder(R.drawable.ic_baseline_keyboard_arrow_down_24).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private ImageButton imageButton;
        private imageClickerHelper cH;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cH = clickerHelper;
            imageView = itemView.findViewById(R.id.eachImage);
            imageView.setOnClickListener(this);
            imageButton = itemView.findViewById(R.id.deleteImage);
            imageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.deleteImage){
                cH.deleteImage(imageList.get(getAdapterPosition()));
            }
            else{
                cH.showFullImage(imageList.get(getAdapterPosition()).getImageUrl());
            }

        }
    }
    public interface imageClickerHelper{
        void deleteImage(image img);
        void showFullImage(String url);
    }
}
