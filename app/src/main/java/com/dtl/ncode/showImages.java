package com.dtl.ncode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;

public class showImages extends AppCompatActivity {
    private ImageView imageView;
    private Button button;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        imageView = findViewById(R.id.imageViewSingle);
        button = findViewById(R.id.saveToGallery);
        url  = getIntent().getExtras().getString("url");
        Glide.with(this).load(url).placeholder(R.drawable.ic_baseline_keyboard_arrow_down_24).into(imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(showImages.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    saveImage();
                }
                else{
                    askPermissions();

                }

            }
        });
    }
    private void saveImage(){
        FileOutputStream outputStream = null;
        File dir = new File(this.getExternalFilesDir(null), "/nCOde");
        if(!dir.exists()){
            dir.mkdir();
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        String imageName = String.format("%d.png",System.currentTimeMillis());
        File outFile = new File(dir,imageName);
        try{
            outputStream = new FileOutputStream(outFile);

        }catch (Exception e){
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        Toast.makeText(this, "saved to :"+dir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        try{
            outputStream.flush();

        }catch(Exception e){
            e.printStackTrace();

        }
        try{
            outputStream.close();

        }catch(Exception e){
            e.printStackTrace();

        }


    }
    private void askPermissions(){
        ActivityCompat.requestPermissions(showImages.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},99);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 99){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage();
            }
            else {
                askPermissions();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}