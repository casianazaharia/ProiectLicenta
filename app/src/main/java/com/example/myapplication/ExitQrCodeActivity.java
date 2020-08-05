package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import static com.example.myapplication.AvailableSlotsActivity.SPOT_NO;
import static com.example.myapplication.LoginActivity.ARG_USER;

public class ExitQrCodeActivity extends AppCompatActivity {
    TextView showSpotNo;
    ImageView qrExit;
    TextView spotNumber;
    Button cancel;

    private int spotNo;
    private User loggedUserQr;
    private DbDao dbDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_qr_code);

        showSpotNo = findViewById(R.id.show_spot_no);
        qrExit = findViewById(R.id.qr_exit);
        cancel = findViewById(R.id.cancelBooking);
        spotNumber = findViewById(R.id.spot_num);
        dbDao = RegisterDatabase.getMyAppDatabase(getApplicationContext()).myDao();

        spotNo = getIntent().getIntExtra(SPOT_NO, 0);
        loggedUserQr = (User) getIntent().getSerializableExtra(ARG_USER);

        showSpotNo.setText("Your booked number ");
        spotNumber.setText("" + spotNo);

        String text = "Exit the parking area";

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 238, 274);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrExit.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        loggedUserQr.setParkingNo(spotNo);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ExitQrCodeActivity.this);
                builder.setTitle("Cancel Booking")
                        .setMessage("Are you sure you want to cancel your booking?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                new ExitQrCodeActivity.DeleteParkingSpot().execute();

                            }
                        })
                        .setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    public class DeleteParkingSpot extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            dbDao.updateUserSelectedParkingSpot(loggedUserQr.getUsername(), 0);
            dbDao.resetBookedParkingSpot(loggedUserQr.getParkingNo());
            loggedUserQr.setParkingNo(0);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent back = new Intent(getApplicationContext(), AvailableSlotsActivity.class);
            back.putExtra(ARG_USER, loggedUserQr);
            startActivity(back);

        }
    }
}


