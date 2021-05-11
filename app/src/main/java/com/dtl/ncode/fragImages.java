package com.dtl.ncode;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dtl.ncode.adapter.imageRVadapter;
import com.dtl.ncode.model.image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class fragImages extends Fragment {
    private RecyclerView recyclerView;
    private imageRVadapter adapter;
    private List<image> images = new ArrayList<>();
    private MutableLiveData<List<image>> imageList = new MutableLiveData<>();
    private CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("images");
    private FirebaseStorage ref= FirebaseStorage.getInstance();


    public fragImages() {
        // Required empty public constructor
    }


    public static fragImages newInstance() {
        return new fragImages();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_images, container, false);
        recyclerView = view.findViewById(R.id.imageRecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                images.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()){
                    if(snapshot.exists()){
                        image img = snapshot.toObject(image.class);
                        images.add(img);
                    }
                }
                adapter = new imageRVadapter(getContext() ,images, new imageRVadapter.imageClickerHelper() {
                    @Override
                    public void deleteImage(image img) {
                        StorageReference imageref = ref.getReferenceFromUrl(img.getImageUrl());
                        imageref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                collectionReference.document(img.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void showFullImage(String url) {
                        Intent intent = new Intent(getActivity(),showImages.class);
                        intent.putExtra("url",url);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);

            }
        });
//        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if(!queryDocumentSnapshots.isEmpty()){
//                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
//                        image img = snapshot.toObject(image.class);
//                        images.add(img);
//                    }
//                    imageList.setValue(images);
//                }
//                else{
//                    Toast.makeText(getActivity(), "Database empty", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getActivity(), "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        imageList.observe(this, new Observer<List<image>>() {
//            @Override
//            public void onChanged(List<image> images) {
//                adapter = new imageRVadapter(getContext() ,images, new imageRVadapter.imageClickerHelper() {
//                    @Override
//                    public void deleteImage(String id) {
//
//                    }
//                });
//                recyclerView.setAdapter(adapter);
//            }
//        });
        return view;

    }
}