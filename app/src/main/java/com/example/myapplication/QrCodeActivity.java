package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import static com.example.myapplication.AvailableSlotsActivity.SPOT_NO;
import static com.example.myapplication.LoginActivity.ARG_USER;

public class QrCodeActivity extends AppCompatActivity {
    TextView showSpotNo;
    ImageView qrImage;
    Button cancel;
    Bitmap bitmap;

    private int spotNo;
    private User loggedUser;
    private DbDao dbDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        showSpotNo = findViewById(R.id.show_spot_no);
        qrImage = findViewById(R.id.qr_image);
        cancel = findViewById(R.id.cancelBooking);

        spotNo = (int) getIntent().getSerializableExtra(SPOT_NO);

        loggedUser = (User) getIntent().getSerializableExtra(ARG_USER);

        dbDao = RegisterDatabase.getMyAppDatabase(getApplicationContext()).myDao();


        showSpotNo.setText("You selected spot no " + spotNo);

        String text = "Book parking lot no " + spotNo;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 238, 274);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loggedUser.setParkingNo(spotNo);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(QrCodeActivity.this);
                builder.setTitle("Cancel Booking")
                        .setMessage("Are you sure you want to cancel your booking?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new DeleteParkingSpot().execute();

                            }
                        })
                        .setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    private class DeleteParkingSpot extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            dbDao.updateUserSelectedParkingSpot(loggedUser.getUsername(), 0);
            dbDao.resetBookedParkingSpot(loggedUser.getParkingNo());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent back = new Intent(getApplicationContext(), AvailableSlotsActivity.class);
            back.putExtra(ARG_USER,loggedUser);
            startActivity(back);
        }
    }


}
