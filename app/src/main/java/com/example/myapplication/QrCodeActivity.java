package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class QrCodeActivity extends AppCompatActivity {
    TextView showSpotNo;
    ImageView qrImage;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        showSpotNo = findViewById(R.id.show_spot_no);
        qrImage = findViewById(R.id.qr_image);



    }
}
