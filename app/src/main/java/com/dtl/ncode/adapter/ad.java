package com.dtl.ncode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dtl.ncode.R;
import com.dtl.ncode.model.link;

import java.util.List;

public class ad extends RecyclerView.Adapter<ad.vH> {
    private List<link> lss;
    private ad.linkClickerHelper linkClickerHelper;

    public ad(List<link> ls,linkClickerHelper clickerHelper) {
        this.lss = ls;
        this.linkClickerHelper = clickerHelper;

    }

    @NonNull
    @Override
    public vH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_row,parent,false);
        return new vH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull vH holder, int position) {
        holder.textView1.setText(lss.get(position).getTitleForLink());
        holder.textView2.setText(lss.get(position).getLinkURL());

    }

    @Override
    public int getItemCount() {
        return lss.size();
    }

    public class vH extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView1;
        TextView textView2;
        linkClickerHelper cH;
        ImageButton deleteButton;
        ImageButton copyButton;


        public vH(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.linkTitleID);
            textView2 = itemView.findViewById(R.id.linkUrlID);
            deleteButton = itemView.findViewById(R.id.deleteLink);
            copyButton = itemView.findViewById(R.id.copyLink);
            deleteButton.setOnClickListener(this);
            textView2.setOnClickListener(this);
            copyButton.setOnClickListener(this);
            cH = linkClickerHelper;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.linkUrlID : cH.linkClink(lss.get(getAdapterPosition()).getLinkURL());
                                      break;
                case R.id.deleteLink: cH.deleteLink(lss.get(getAdapterPosition()).getTitleForLink());
                                      break;
                case R.id.copyLink:   cH.copyContentLink(lss.get(getAdapterPosition()).getLinkURL());
            }
        }
    }
    public interface linkClickerHelper{
        void linkClink(String Url);
        void deleteLink(String link_titleQuery);
        void copyContentLink(String copyContent);
    }
}


