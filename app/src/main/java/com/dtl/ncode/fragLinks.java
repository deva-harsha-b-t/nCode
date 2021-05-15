package com.dtl.ncode;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dtl.ncode.adapter.ad;
import com.dtl.ncode.adapter.linkRecyclerAdapter;
import com.dtl.ncode.model.link;
import com.dtl.ncode.util.textDao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class fragLinks extends Fragment {
    private static final String TAG = "link Testing";
    private RecyclerView recyclerViewlink;
    private SwipeRefreshLayout refreshLayout;
    private List<link> links = new ArrayList<>();
    private textDao dao;
    private ad adddd;

    public fragLinks() {
    }
    public static fragLinks newInstance() {
        fragLinks fragment = new fragLinks();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dao = new textDao();
       View view1 =  inflater.inflate(R.layout.fragment_frag_links, container, false);
       recyclerViewlink = view1.findViewById(R.id.linkRecyclerVieww);
       recyclerViewlink.setHasFixedSize(true);
       recyclerViewlink.setLayoutManager(new LinearLayoutManager(getActivity()));
       dao.getLinks().addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               links.clear();
               for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                   link tl = dataSnapshot.getValue(link.class);
                   links.add(tl);
               }
               adddd = new ad(links, new ad.linkClickerHelper() {
                   @Override
                   public void linkClink(String Url) {
                       Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                       getActivity().startActivity(intent);
                   }

                   @Override
                   public void deleteLink(String link_titleQuery) {
                       new AlertDialog.Builder(getActivity())
                   .setTitle("Delete?").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dao.deleteLink(link_titleQuery);

                           }
                       }).setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               dialog.dismiss();

                           }
                       }).create().show();
                   }

                   @Override
                   public void copyContentLink(String copyContent) {
                       ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                       ClipData clipData = ClipData.newPlainText("toTheClipBoard",copyContent);
                       clipboardManager.setPrimaryClip(clipData);
                       Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();

                   }
               });
               recyclerViewlink.setAdapter(adddd);
               adddd.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
        return view1;
    }
}