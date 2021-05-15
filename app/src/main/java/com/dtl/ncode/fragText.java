package com.dtl.ncode;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dtl.ncode.adapter.textRecyclerAdapter;
import com.dtl.ncode.model.text;
import com.dtl.ncode.util.textDao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragText extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private textRecyclerAdapter recyclerAdapter;
    private textDao DAO;
    private List<text> texts = new ArrayList<>();

    public fragText() {
        // Required empty public constructor
    }

    public static fragText newInstance() {
        return new fragText();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DAO = new textDao();
        View view = inflater.inflate(R.layout.fragment_frag_text, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipeTexts);
        recyclerView = view.findViewById(R.id.textRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DAO.getTexts().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                texts.clear();
                for (DataSnapshot data : snapshot.getChildren()){
                    text temp = data.getValue(text.class);
                    texts.add(temp);
                }
                recyclerAdapter = new textRecyclerAdapter(texts, new textRecyclerAdapter.TextClickerHelper() {
                    @Override
                    public void delteText(String title_forQuery) {
                        new AlertDialog.Builder(getActivity()).setTitle("Delete?")
                                .setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DAO.deleteText(title_forQuery);
                                Toast.makeText(getActivity(), "Deleted "+title_forQuery, Toast.LENGTH_SHORT).show();

                            }
                        }).create().show();

                    }

                    @Override
                    public void copyTextToClip(String copyContent) {
                        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("toTheClipBoard",copyContent);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(getActivity(), "Copied", Toast.LENGTH_SHORT).show();
                    }
                });
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

}