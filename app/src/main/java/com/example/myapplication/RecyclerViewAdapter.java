package com.example.myapplication;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnSpotSelected mOnSpotSelected;

    private Context mContext;
    private List<AbstractItem> mItems;
    private LayoutInflater mLayoutInflater;

    private Pair<ParkingSpot, Integer> selectedPair;
    private Pair<ParkingSpot, Integer> bookedPair;

    public RecyclerViewAdapter(Context context, OnSpotSelected onSpotSelectedListener, List<AbstractItem> items) {
        mOnSpotSelected = onSpotSelectedListener;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).getType();
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == AbstractItem.TYPE_CENTER
                || viewType == AbstractItem.TYPE_EDGE) {
            View itemView = mLayoutInflater.inflate(R.layout.parking_spots, parent, false);
            return new ParkingSpotViewHolder(itemView);
        } else {
            View itemView = new View(mContext);
            return new EmptyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        int type = mItems.get(position).getType();

        if (type == AbstractItem.TYPE_CENTER
                || type == AbstractItem.TYPE_EDGE) {
            final AbstractItem item = mItems.get(position);
            ParkingSpotViewHolder holder = (ParkingSpotViewHolder) viewHolder;
            holder.spotNo.setText(String.valueOf(item.getParkingSpot().spotNo));
            holder.imgSpot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!item.getParkingSpot().isAvailable) {
                        Toast.makeText(mContext, "Not Available", Toast.LENGTH_SHORT).show();
                    } else {
                        toggleSelection(item.getParkingSpot(), position);
                        mOnSpotSelected.onSpotSelected(item.getParkingSpot());
                    }
                }
            });

            setParkingSpotIcon(holder, item, position);
        }
    }

    private void toggleSelection(ParkingSpot parkingSpot, int position) {
        if (selectedPair != null) {
            selectedPair.first.isSelected = false;
            notifyItemChanged(selectedPair.second);
        }

        parkingSpot.isSelected = true;
        selectedPair = new Pair<>(parkingSpot, position);
        notifyItemChanged(position);
    }

    public void bookSelectedParkingSpot() {
        if(bookedPair != null) {
            bookedPair.first.isBooked = false;
            bookedPair.first.isAvailable = true;
            notifyItemChanged(bookedPair.second);
        }
        selectedPair.first.isBooked = true;
        bookedPair = new Pair<>(selectedPair.first, selectedPair.second);
        notifyItemChanged(bookedPair.second);
    }

    private void setParkingSpotIcon(ParkingSpotViewHolder holder, AbstractItem item, int position) {
        if (item.getParkingSpot().isSelected) {
            holder.imgSpot.setImageResource(R.drawable.ic_directions_black_car);
        } else if (item.getParkingSpot().isBooked) {
            holder.imgSpot.setImageResource(R.drawable.ic_directions_green_car);
            bookedPair = new Pair<>(item.getParkingSpot(), position);
        } else if (!item.getParkingSpot().isAvailable) {
            holder.imgSpot.setImageResource(R.drawable.ic_directions_red_car);
        } else {
            holder.imgSpot.setImageResource(R.drawable.ic_directions_grey_car);
        }
    }

    private static class ParkingSpotViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSpot;
        TextView spotNo;

        public ParkingSpotViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSpot = itemView.findViewById(R.id.img_spot);
            spotNo = itemView.findViewById(R.id.spot_no);
        }
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
