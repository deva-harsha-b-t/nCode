package com.dtl.ncode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dtl.ncode.adapter.codeRecyclerAdapter;
import com.dtl.ncode.model.code;
import com.dtl.ncode.model.sharedViewModel;
import com.dtl.ncode.util.UTIL;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class fragCodes extends Fragment{
   private RecyclerView recyclerViewCodes;
    private List<code> codes = new ArrayList<>();
   // private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private codeRecyclerAdapter codeRecyclerAdapter;
    private static final String TAG = "TESTING DATA RETRIEVAL";
    private FirebaseFirestore db;
    private com.dtl.ncode.model.sharedViewModel sharedViewModel;


    public fragCodes() {
        // Required empty public constructor
    }

    public static fragCodes newInstance() {
        return new fragCodes();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_codes, container, false);
        recyclerViewCodes = view.findViewById(R.id.codesRecyclerView);
        recyclerViewCodes.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewCodes.setHasFixedSize(true);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(com.dtl.ncode.model.sharedViewModel.class);
        sharedViewModel.getCodes().observe(this, new Observer<List<code>>() {
            @Override
            public void onChanged(List<code> CODES) {
                recyclerViewCodes.setAdapter(new codeRecyclerAdapter(CODES, new codeRecyclerAdapter.ClickerHelper() {

                    @Override
                    public void onClick(String url) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                    }

                    @Override
                    public void onShow(Button read, ImageView coverImage , ImageButton delete) {
                        if (read.getVisibility() == View.GONE) {
                            read.setVisibility(View.VISIBLE);
                            coverImage.setVisibility(View.VISIBLE);
                            delete.setVisibility(View.VISIBLE);
                        } else if (read.getVisibility() == View.VISIBLE) {
                            read.setVisibility(View.GONE);
                            coverImage.setVisibility(View.GONE);
                            delete.setVisibility(View.GONE);

                        }

                    }

                    @Override
                    public void onDelete(String codeNumber) {
                        new AlertDialog.Builder(getActivity()).setTitle("Delete?")
                                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sharedViewModel.getDb().collection("nCODES").document(codeNumber).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getActivity(), "failed to delete", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                }).setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }

                },getContext()));

            }
        });
        return view;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        recyclerViewCodes = view.findViewById(R.id.codesRecyclerView);
//        recyclerViewCodes.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerViewCodes.setHasFixedSize(true);
//
//
//
//
//        db.collection("nCODES").addSnapshotListener(this,new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                tmepcodes.clear();
//                for(DocumentSnapshot snapshot:value.getDocuments()){
//                    if(snapshot.exists()){
//                        String value_code = snapshot.getString(DOCUMENT_FIELD_CODE);
//                        String value_coverImage = snapshot.getString(DOCUMENT_FIELD_CoverURL);
//                        String value_title = snapshot.getString(DOCUMENT_FIELD_TITLE);
//                        String value_url = snapshot.getString(DOCUMENT_FIELD_codeURL);
//                        code oneCode = new code(value_code,value_coverImage,value_title,value_url);
//                        tmepcodes.add(oneCode);
//                    }
//
//                }
//                codeRecyclerAdapter = new codeRecyclerAdapter(tmepcodes,MainActivity.this);
//                recyclerView.setAdapter(codeRecyclerAdapter);
//
//            }
//        });
//        codes = tmepcodes;
//    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        sharedViewModel = new ViewModelProvider(requireActivity()).get(com.dtl.ncode.model.sharedViewModel.class);
//        List<code> tempCodes = new ArrayList<>();
//        db = sharedViewModel.getDb();
//        db.collection("nCodes").addSnapshotListener(new EventListener<QuerySnapshot>() {
//
//
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                tempCodes.clear();
//                for (DocumentSnapshot snapshot : value.getDocuments()) {
//                    if (snapshot.exists()) {
//                        String value_code = snapshot.getString(UTIL.DOCUMENT_FIELD_CODE);
//                        String value_coverImage = snapshot.getString(UTIL.DOCUMENT_FIELD_CoverURL);
//                        String value_title = snapshot.getString(UTIL.DOCUMENT_FIELD_TITLE);
//                        String value_url = snapshot.getString(UTIL.DOCUMENT_FIELD_codeURL);
//                        code oneCode = new code(value_code, value_coverImage, value_title, value_url);
//                        tempCodes.add(oneCode);
//                    }
//                }
//                    sharedViewModel.putCodes(tempCodes);
//            }
//        });
//    }
}
