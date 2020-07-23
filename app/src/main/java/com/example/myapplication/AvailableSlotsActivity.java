package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AvailableSlotsActivity extends LoginActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final int COLUMNS = 5;
    Button bookSpot;
    private User loggedUser;

    private UserDao userDao;
    public int selectedSpotNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_slots);

        loggedUser = (User) getIntent().getExtras().getSerializable(ARG_USER);

        userDao = RegisterDatabase.getMyAppDatabase(getApplicationContext()).myDao();

        final List<ParkingSpot> parkingSpotList = new ArrayList<>();
        bookSpot = findViewById(R.id.book_spot);

        for (int i = 0; i < 40; i++) {
            ParkingSpot spot = new ParkingSpot();
            spot.spotNo = i + 1;
            if(spot.spotNo == loggedUser.parkingNo) {
                spot.isBooked = true;

            } else {
                spot.isAvailable = i % 7 != 0;
            }
            parkingSpotList.add(spot);
        }

        int noOfEmptyItems = parkingSpotList.size() / 4;
        noOfEmptyItems += ((parkingSpotList.size() % 4) == 3) ? 1 : 0;
        int noOfTotalItems = parkingSpotList.size() + noOfEmptyItems;

        int indexOfParkingSpots = 0;

        List<AbstractItem> items = new ArrayList<>();
        for (int i = 0; i < noOfTotalItems; i++) {
            if (i % COLUMNS == 0 || i % COLUMNS == 4) {
                items.add(new EdgeItem(parkingSpotList.get(indexOfParkingSpots++)));
            } else if (i % COLUMNS == 1 || i % COLUMNS == 3) {
                items.add(new CenterItem(parkingSpotList.get(indexOfParkingSpots++)));
            } else {
                items.add(new EmptyItem(null));
            }
        }

        final OnSpotSelected onSpotSelectedListener = new OnSpotSelected() {
            @Override
            public void onSpotSelected(ParkingSpot selectedParkingSpot) {
                bookSpot.setText("Book spot no " + selectedParkingSpot.spotNo);
                selectedSpotNo =  selectedParkingSpot.spotNo;
            }

        };

        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        RecyclerView recyclerView = findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, onSpotSelectedListener, items);
        recyclerView.setAdapter(adapter);

        bookSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(loggedUser.getParkingNo() == 0){

                    loggedUser.setParkingNo(selectedSpotNo);
                    AddParkingStatusAsyncTask addParkingStatus = new AddParkingStatusAsyncTask();
                    addParkingStatus.execute();


                    AlertDialog.Builder builder = new AlertDialog.Builder(AvailableSlotsActivity.this);
                    builder.setTitle("Parking Spot Booking")
                            .setMessage("You will have 20 minutes to scan your QR code")
                            .setPositiveButton("Scan now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent scanNow = new Intent(getApplicationContext(), QrCodeActivity.class);
                                    startActivity(scanNow);
                                }
                            })
                            .setNegativeButton("Scan later",null);
                    //after pressing later, save the qr in database

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(AvailableSlotsActivity.this, "You already booked parking spot no " + loggedUser.getParkingNo(), Toast.LENGTH_SHORT).show();
                }

                //add the booking in database
                //change car color to red

            }

        });

    }


    private class AddParkingStatusAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {

            userDao.insertSelectedParkingNo(loggedUser.getUsername(), loggedUser.getParkingNo());
            return null;

        }

    }


}



