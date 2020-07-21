package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AvailableSlotsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final int COLUMNS = 5;

    Button bookSpot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_slots);

        List<ParkingSpot> parkingSpotList = new ArrayList<>();
        bookSpot = findViewById(R.id.book_spot);

        for (int i = 0; i < 40; i++) {
            ParkingSpot spot = new ParkingSpot();
            spot.spotNo = i + 1;
            spot.isAvailable = i % 3 == 0;

            parkingSpotList.add(spot);
        }

        int noOfEmptyItems = parkingSpotList.size() / 4;
        noOfEmptyItems += parkingSpotList.size() % 4 == 3 ? 1 : 0;

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

        OnSpotSelected onSpotSelectedListener = new OnSpotSelected() {
            @Override
            public void onSpotSelected(ParkingSpot selectedParkingSpot) {
                bookSpot.setText("Book spot no " + selectedParkingSpot.spotNo);
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

            }
        });

    }

}



