package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.LoginActivity.ARG_USER;

public class AvailableSlotsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int COLUMNS = 5;
    private Button bookSpot;
    private User loggedUser;

    private DbDao dbDao;
    private int selectedSpotNo;
    private RecyclerViewAdapter adapter;

    public static final String SPOT_NO = "SpotNo";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private TextView tvName;
    private TextView tvMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_available_slots);

        loggedUser = (User) getIntent().getSerializableExtra(ARG_USER);

        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvName = navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        tvMail = navigationView.getHeaderView(0).findViewById(R.id.tv_mail);

        tvName.setText("Name: " + loggedUser.getName());
        tvMail.setText("Email: " + loggedUser.getEmail());


        bookSpot = findViewById(R.id.book_spot);

        dbDao = RegisterDatabase.getMyAppDatabase(getApplicationContext()).myDao();

        bookSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loggedUser.getParkingNo() == 0) {
                    loggedUser.setParkingNo(selectedSpotNo);
                    adapter.bookSelectedParkingSpot();
                    new AddParkingStatusAsyncTask().execute();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AvailableSlotsActivity.this);
                    builder.setTitle("Change booked spot")
                            .setMessage("You already booked spot no " + loggedUser.getParkingNo() + ". Are you sure you want to change your booking to spot no " + selectedSpotNo + "?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new UpdateParkingSpot().execute();
                                }
                            })
                            .setNegativeButton("No", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        new LoadParkingSpots().execute();
    }

    private void setupParkingSpotsList(List<ParkingSpot> parkingSpotList) {
        if (parkingSpotList == null) {
            return;
        }

        final OnSpotSelected onSpotSelectedListener = new OnSpotSelected() {
            @Override
            public void onSpotSelected(ParkingSpot selectedParkingSpot) {
                bookSpot.setText("Book spot no " + selectedParkingSpot.spotNo);
                selectedSpotNo = selectedParkingSpot.spotNo;
            }
        };

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

        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        RecyclerView recyclerView = findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerViewAdapter(this, onSpotSelectedListener, items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.nav_booking:

                if (loggedUser.getParkingNo() != 0) {
                    Intent intent = new Intent(AvailableSlotsActivity.this, QrCodeActivity.class);
                    intent.putExtra(SPOT_NO, loggedUser.getParkingNo());
                    intent.putExtra(ARG_USER, loggedUser);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "You don't have any booking!", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.nav_logout:

                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private class LoadParkingSpots extends AsyncTask<Void, Void, List<ParkingSpot>> {
        @Override
        protected List<ParkingSpot> doInBackground(Void... voids) {
            List<ParkingSpot> dbList = dbDao.getAllParkingSpots();
            for (ParkingSpot spot : dbList) {
                if (loggedUser.getUsername().equals(spot.getIsBookedByUsername())) {
                    spot.setBooked(true);
                }
            }
            return dbList;
        }

        @Override
        protected void onPostExecute(List<ParkingSpot> parkingSpots) {
            super.onPostExecute(parkingSpots);
            setupParkingSpotsList(parkingSpots);
        }
    }

    private class AddParkingStatusAsyncTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            dbDao.updateBookedParkingSpot(loggedUser.getParkingNo(), loggedUser.getUsername());
            dbDao.updateUserSelectedParkingSpot(loggedUser.getUsername(), loggedUser.getParkingNo());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            AlertDialog.Builder builder = new AlertDialog.Builder(AvailableSlotsActivity.this);
            builder.setTitle("Parking Spot Booking")
                    .setMessage("You will have 20 minutes to scan your QR code")
                    .setPositiveButton("Scan now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent scanNow = new Intent(AvailableSlotsActivity.this, QrCodeActivity.class);
                            scanNow.putExtra(SPOT_NO, selectedSpotNo);
                            scanNow.putExtra(ARG_USER, loggedUser);
                            startActivity(scanNow);

                        }
                    })
                    .setNegativeButton("Scan later", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    public class UpdateParkingSpot extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            dbDao.updateUserSelectedParkingSpot(loggedUser.getUsername(), 0);
            dbDao.resetBookedParkingSpot(loggedUser.getParkingNo());
            loggedUser.setParkingNo(0);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loggedUser.setParkingNo(selectedSpotNo);
            adapter.bookSelectedParkingSpot();
            new AddParkingStatusAsyncTask().execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}



