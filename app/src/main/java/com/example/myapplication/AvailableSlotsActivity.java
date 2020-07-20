package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AvailableSlotsActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private static final int COLUMNS = 5;
    private TextView txtSeatSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_slots);



        List<AbstractItem> items = new ArrayList<>();
        for (int i = 0; i < 40; i++) {

            if (i % COLUMNS == 0 || i % COLUMNS == 4) {
                items.add(new EdgeItem(String.valueOf(i)));
            } else if (i % COLUMNS == 1 || i % COLUMNS == 3) {
                items.add(new CenterItem(String.valueOf(i)));
            } else {
                items.add(new EmptyItem(String.valueOf(i)));
            }
        }

        OnSpotSelected onSpotSelectedListener = new OnSpotSelected() {
            @Override
            public void onSpotSelected(int count) {

            }
        };

        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        RecyclerView recyclerView = findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, onSpotSelectedListener, items);
        recyclerView.setAdapter(adapter);


    }


}

