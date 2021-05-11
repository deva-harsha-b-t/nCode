package com.dtl.ncode.util;

import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dtl.ncode.model.image;
import com.dtl.ncode.model.link;
import com.dtl.ncode.model.text;
import com.dtl.ncode.selectImage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class textDao {
    private DatabaseReference databaseReferenceText;
    private DatabaseReference databaseReferenceLink;
    private DatabaseReference databaseReferenceImage;
    private StorageReference storageReference;

    public textDao() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReferenceText = db.getReference(text.class.getSimpleName());
        databaseReferenceLink = db.getReference(link.class.getSimpleName());
        databaseReferenceImage = db.getReference(image.class.getSimpleName());
    }

    public Task<Void> textAdd(text txt){
         return databaseReferenceText.push().setValue(txt);
    }

    public Task<Void> LinkAdd(link lnk){
        return databaseReferenceLink.push().setValue(lnk);
    }

    public Query getTexts(){
        return databaseReferenceText.orderByKey();
    }
    public Query getLinks(){
        return databaseReferenceLink.orderByKey();
    }

    public void deleteText(String title){
        Query query = databaseReferenceText.orderByChild("title").equalTo(title);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                snapshot.getRef().setValue(null);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteLink(String linkTitle){
        Query query = databaseReferenceLink.orderByChild("titleForLink").equalTo(linkTitle);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                snapshot.getRef().setValue(null);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
