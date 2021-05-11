package com.dtl.ncode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dtl.ncode.adapter.codeRecyclerAdapter;
import com.dtl.ncode.adapter.stateAdapter;
import com.dtl.ncode.model.code;
import com.dtl.ncode.model.link;
import com.dtl.ncode.model.sharedViewModel;
import com.dtl.ncode.model.text;
import com.dtl.ncode.util.UTIL;
import com.dtl.ncode.util.textDao;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.okhttp.internal.Util;

public class MainActivity extends AppCompatActivity {
    private Uri imageUri;
    private EditText getNumbers;
    private Button addButton;
    private List<code> codes = new ArrayList<>();
    private RecyclerView recyclerView;
    private codeRecyclerAdapter codeRecyclerAdapter;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private sharedViewModel shModel;
    private BottomSheetDialog bsdCode;
    private BottomSheetDialog bsdText;
    private BottomSheetDialog bsdLink;
    private BottomSheetDialog bsdImage;
    private TabLayout tabLayout;
    private textDao dao;
    private Button addImage;
    private ImageButton SelectImage_forUploading;
    private EditText getOptionalTitle;
    private ImageView imagePreview;
    public static final String TAG = "tabs";
    private static final int SELECTIMAGECODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao = new textDao();
        shModel = new ViewModelProvider(this).get(sharedViewModel.class);
        shModel.setDb(db);
        tabLayout = findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        viewPager2.setAdapter(new stateAdapter(this,this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {
                        tab.setText("Text");
                        break;
                    }
                    case 1: {

                        tab.setText("Images");
                        break;
                    }
                    case 2: {
                        tab.setText("Codes");
                        break;
                    }
                    case 3: {
                        tab.setText("Links");
                        break;
                    }
                }

            }
        });
        tabLayoutMediator.attach();

        FloatingActionButton flt = findViewById(R.id.flt);
        flt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tabLayout.getSelectedTabPosition()){
                    case 0:{
                        bsdText = new BottomSheetDialog(MainActivity.this);
                        View viewText = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_text,findViewById(R.id.parent_bottom_sheet_text));
                        EditText titleText = viewText.findViewById(R.id.titleEdit);
                        EditText descText =  viewText.findViewById(R.id.descedit);
                        Button addText = viewText.findViewById(R.id.addTextButton);
                        addText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!TextUtils.isEmpty(titleText.getText().toString()) && !TextUtils.isEmpty(descText.getText().toString())){
                                    text tempText = new text();
                                    String title = titleText.getText().toString();
                                    String desc = descText.getText().toString();
                                    tempText.setTitle(title);
                                    tempText.setDescription(desc);
                                    dao.textAdd(tempText).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    bsdText.dismiss();
                                }
                                else{
                                    Toast.makeText(MainActivity.this,"Fields Empty",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        bsdText.setContentView(viewText);
                        bsdText.show();
                        break;
                    }
                    case 1:{
                        Intent selectImageIntent = new Intent(MainActivity.this,selectImage.class);
                        startActivity(selectImageIntent);
                        break;
                    }
                    case 2:{
                        bsdCode = new BottomSheetDialog(MainActivity.this);
                        View viewCode = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet,
                                findViewById(R.id.bottom_sheet_parent_quick_go));
                        getNumbers = viewCode.findViewById(R.id.Txtinput_quick_go);
                        addButton = viewCode.findViewById(R.id.GoButton);
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                putData_Code();
                            }
                        });
                        bsdCode.setContentView(viewCode);
                        bsdCode.show();
                        break;
                    }
                    case 3:{
                        bsdLink = new BottomSheetDialog(MainActivity.this);
                        View viewText = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_link,findViewById(R.id.parent_bottom_sheet_link));
                        EditText titleLink = viewText.findViewById(R.id.titleForLinkEdit);
                        EditText urlLink =  viewText.findViewById(R.id.linkURLedit);
                        Button addLink = viewText.findViewById(R.id.addLinkButton);
                        addLink.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!TextUtils.isEmpty(titleLink.getText().toString()) && !TextUtils.isEmpty(urlLink.getText().toString())){
                                    link tempLink = new link();
                                    String linkTitle = titleLink.getText().toString();
                                    String linkUrl = urlLink.getText().toString();
                                    tempLink.setTitleForLink(linkTitle);
                                    tempLink.setLinkURL(linkUrl);
                                    dao.LinkAdd(tempLink).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    bsdLink.dismiss();

                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Fields Empty", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        bsdLink.setContentView(viewText);
                        bsdLink.show();

                        break;
                    }
                }

            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.quickGo) {
                    BottomSheetDialog bottomSheetDialog1 = new BottomSheetDialog(MainActivity.this);
                    View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_quick_go,
                            findViewById(R.id.bottom_sheet_parent_quick_go));
                    EditText et = v.findViewById(R.id.Txtinput_quick_go);
                    Button go = v.findViewById(R.id.GoButton);
                    go.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(et.getText().toString())) {
                                String n = et.getText().toString();
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(UTIL.CODE_STARTERURL + n));
                                startActivityForResult(intent,100);
                            } else {
                                Toast.makeText(MainActivity.this, "Enter number", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    bottomSheetDialog1.setContentView(v);
                    bottomSheetDialog1.show();
                }
                return true;
            }
        });

    }

    public void putData_Code() {

        Map<String, Object> data = new HashMap<>();
        if (!TextUtils.isEmpty(getNumbers.getText().toString())) {
            String number = getNumbers.getText().toString();
            data.put(UTIL.DOCUMENT_FIELD_CODE, number);
            data.put(UTIL.DOCUMENT_FIELD_codeURL, UTIL.CODE_STARTERURL + number);
            data.put(UTIL.DOCUMENT_FIELD_CoverURL, UTIL.IMAGE_STARTURL+number+UTIL.IMAGE_ENDURL);
            data.put(UTIL.DOCUMENT_FIELD_TITLE, "working on it");
            db.collection("nCODES").document(number).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "could not add", Toast.LENGTH_SHORT).show();

                }
            });
            bsdCode.dismiss();
        } else {
            Toast.makeText(MainActivity.this, "Enter the code", Toast.LENGTH_SHORT).show();
        }

    }
//    public void getData(){
//        List<code> tmepcodes = new ArrayList<>();
//        db.collection("nCODES").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for(DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
//                    if(snapshot.exists()){
//                        String value_code = snapshot.getString(DOCUMENT_FIELD_CODE);
//                        String value_coverImage = snapshot.getString(DOCUMENT_FIELD_CoverURL);
//                        String value_title = snapshot.getString(DOCUMENT_FIELD_TITLE);
//                        String value_url = snapshot.getString(DOCUMENT_FIELD_codeURL);
//                        code oneCode = new code(value_code,value_coverImage,value_title,value_url);
//                        tmepcodes.add(oneCode);
//                    }
//                }
//                codes = tmepcodes;
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

    @Override
    protected void onStart(){
        super.onStart();
        List<code> tmepcodes = new ArrayList<>();
        List<link> tempLinks = new ArrayList<>();

        db.collection("nCODES").addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                tmepcodes.clear();
                for(DocumentSnapshot snapshot:value.getDocuments()){
                    if(snapshot.exists()){
                        String value_code = snapshot.getString(UTIL.DOCUMENT_FIELD_CODE);
                        String value_coverImage = snapshot.getString(UTIL.DOCUMENT_FIELD_CoverURL);
                        String value_title = snapshot.getString(UTIL.DOCUMENT_FIELD_TITLE);
                        String value_url = snapshot.getString(UTIL.DOCUMENT_FIELD_codeURL);
                        code oneCode = new code(value_code,value_coverImage,value_title,value_url);
                        tmepcodes.add(oneCode);
                    }

                }
                shModel.putCodes(tmepcodes);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            tabLayout.selectTab(tabLayout.getTabAt(1));
        }
    }
}