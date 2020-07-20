package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerViewAdapter extends SelectableAdapter<RecyclerView.ViewHolder> {

    private OnSpotSelected mOnSpotSelected;

    private static class EdgeViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSpot;
        ImageView imgSpotSelected;

        public EdgeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSpot = itemView.findViewById(R.id.img_spot);
            imgSpotSelected = itemView.findViewById(R.id.img_spot_selected);
        }
    }

    private static class CenterViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSpot;
        ImageView imgSpotSelected;

        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSpot = itemView.findViewById(R.id.img_spot);
            imgSpotSelected = itemView.findViewById(R.id.img_spot_selected);
        }
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private Context mContext;
    private List<AbstractItem> mItems;
    private LayoutInflater mLayoutInflater;

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
        if (viewType == AbstractItem.TYPE_CENTER) {
            View itemView = mLayoutInflater.inflate(R.layout.parking_spots, parent, false);
            return new CenterViewHolder(itemView);
        } else if (viewType == AbstractItem.TYPE_EDGE) {
            View itemView = mLayoutInflater.inflate(R.layout.parking_spots, parent, false);
            return new EdgeViewHolder(itemView);
        } else {
            View itemView = new View(mContext);
            return new EmptyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        int type = mItems.get(position).getType();
        if (type == AbstractItem.TYPE_CENTER) {
            final CenterItem item = (CenterItem) mItems.get(position);
            CenterViewHolder holder = (CenterViewHolder) viewHolder;
            holder.imgSpot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSelection(position);
                    mOnSpotSelected.onSpotSelected(getSelectedItemCount());
                }
            });
            holder.imgSpotSelected.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        } else if (type == AbstractItem.TYPE_EDGE) {
            final EdgeItem item = (EdgeItem) mItems.get(position);
            EdgeViewHolder holder = (EdgeViewHolder) viewHolder;
            holder.imgSpot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSelection(position);
                    mOnSpotSelected.onSpotSelected(getSelectedItemCount());
                }
            });
            holder.imgSpotSelected.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        }

    }

}
