package com.dtl.ncode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dtl.ncode.model.image;
import com.dtl.ncode.model.sharedViewModel;
import com.dtl.ncode.util.textDao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class selectImage extends AppCompatActivity {
    private ImageView previewImage;
    private ImageButton getImage_fromDevice;
    private Button addImage;
    private EditText optionalTitle;
    public static final int SELECTIMAGECODE = 1;
    private ProgressBar progressBar;
    private Uri imageUri;
    private textDao dao;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("images");
    private StorageReference firebaseStorageRef = FirebaseStorage.getInstance().getReference();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = new textDao();
        setContentView(R.layout.activity_select_image);
        previewImage = findViewById(R.id.imagePreviewAC);
        getImage_fromDevice = findViewById(R.id.selectImageAC);
        optionalTitle = findViewById(R.id.EnterOptionalTitleAC);
        addImage = findViewById(R.id.AddImageButtonAC);
        progressBar = findViewById(R.id.progressBarImage);
        progressBar.setVisibility(View.INVISIBLE);
        getImage_fromDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,SELECTIMAGECODE);
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri!=null){
                    //uploadToFirebase(imageUri);
                    putImagesToFirebase(imageUri);
                }
                else{
                    Toast.makeText(selectImage.this, "Select a image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void putImagesToFirebase(Uri imageUri) {
        progressBar.setVisibility(View.VISIBLE);
        String title = "default title";
        if(TextUtils.isEmpty(optionalTitle.getText())){
            title = "Should have put a title";
        }else{
            title = optionalTitle.getText().toString();
        }
        StorageReference storageReference = firebaseStorageRef.child("Images")
                .child("image_"+ Timestamp.now().getSeconds());

        String finalTitle = title;
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        image img = new image();
                        img.setImageTitle(finalTitle);
                        img.setImageUrl(uri.toString());
                        img.setTimestamp(new Timestamp(new Date()));
                        img.setId("1");
                        collectionReference.add(img).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String id = documentReference.getId();
                                img.setId(id);
                                collectionReference.document(id).set(img);
                                progressBar.setVisibility(View.INVISIBLE);
                                Intent intentBack = new Intent(selectImage.this,MainActivity.class);
                                intentBack.putExtra("chooseImageTab","YES");
                                setResult(100,intentBack);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(selectImage.this, "error :"+e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(selectImage.this, "error :"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(selectImage.this, "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
    }

//    private void uploadToFirebase(Uri uri) {
//        progressBar.setVisibility(View.VISIBLE);
//        String imageName = System.currentTimeMillis() + "." + getFileExtension(uri);
//        StorageReference ref = storageReference.child(imageName);
//        ref.putFile(uri).addOnSuccessListener(this,new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String title;
//                        if(!TextUtils.isEmpty(optionalTitle.getText().toString())){
//                            title = optionalTitle.getText().toString();
//                        }else{
//                            title = imageName;
//                        }
//                        dao.imageAdd(title,uri).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(selectImage.this, "added", Toast.LENGTH_SHORT).show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(selectImage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
//                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                progressBar.setVisibility(View.INVISIBLE);
//                Toast.makeText(selectImage.this, "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECTIMAGECODE && resultCode == RESULT_OK){
            if(data!=null){
                imageUri = data.getData();
                previewImage.setImageURI(imageUri);
            }
        }
    }
}