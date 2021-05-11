package com.dtl.ncode.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class sharedViewModel extends ViewModel {
    private final MutableLiveData<List<code>> codes = new MutableLiveData<List<code>>();
    private FirebaseFirestore db;
    private int tabPosition;
    private int fromImageSelection = 0;

    public void putCodes(List<code> Codes){
        codes.setValue(Codes);
    }
    public LiveData<List<code>> getCodes(){
        return codes;
    }
    public void setDb(FirebaseFirestore database){
        db = database;
    }
    public FirebaseFirestore getDb(){
        return db;
    }
}
