package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import javax.crypto.AEADBadTagException;

public class QrCodeActivity extends AvailableSlotsActivity {
    TextView showSpotNo;
    ImageView qrImage;
    Button cancel;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        showSpotNo = findViewById(R.id.show_spot_no);
        qrImage = findViewById(R.id.qr_image);
        cancel = findViewById(R.id.cancelBooking);

        showSpotNo.setText("You selected spot no " + selectedSpotNo);

        String text = "Book parking lot no " + selectedSpotNo;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 238, 274);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QrCodeActivity.this);
                builder.setTitle("Cancel Booking")
                        .setMessage("Are you sure you want to cancel your booking?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //delete the booking from database

                                Intent back = new Intent(getApplicationContext(), AvailableSlotsActivity.class);
                                startActivity(back);

                            }
                        })
                        .setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }
}
