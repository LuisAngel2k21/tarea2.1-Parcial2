package com.example.tarea2_1_video;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{


    private static final int grabar_video = 4;
    private VideoView video;
    private Spinner spinner;
    private String[] lista;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lista = fileList();
        spinner = findViewById(R.id.spinnervideos);
        video = findViewById(R.id.videos);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        spinner.setAdapter(adapter);

    }
    public void Video_grab(View v)
    {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        startActivityForResult(intent, grabar_video);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)

    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == grabar_video && resultCode == RESULT_OK)
        {
            Uri videoUri = data.getData();
            video.setVideoURI(videoUri);
            video.start();

            try {
                AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(data.getData(), "r");
                FileInputStream in = videoAsset.createInputStream();
                FileOutputStream archivo = openFileOutput(NombreVideo(), Context.MODE_PRIVATE);
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0)
                {
                    archivo.write(buf, 0, len);
                }

            }
            catch (IOException e){
                Toast.makeText(this, "Error ", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void reproducirVideo(View v)
    {
        int pos=spinner.getSelectedItemPosition();
        video.setVideoPath(getFilesDir()+"/"+lista[pos]);
        video.start();
    }

    private String NombreVideo()
    {

        String fecha = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String nombre = fecha + ".mp4";
        return nombre;
    }
}