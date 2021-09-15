package com.example.parentapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

// LocationAdapter is based on the same logic as CallAdapter class, for comments regarding the implementation check the CallAdapter class
public class LocationAdapter extends FirebaseRecyclerAdapter<Location, LocationAdapter.LocationViewHolder> {
    public LocationAdapter(@NonNull FirebaseRecyclerOptions<Location> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull LocationAdapter.LocationViewHolder holder, int position, @NonNull Location model) {
        holder.latitude.setText(Double.toString(model.getLatitude()));
        holder.longitude.setText(Double.toString(model.getLongitude()));
        holder.timestamp.setText(model.getTimestamp());
    }

    @NonNull
    @Override
    public LocationAdapter.LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location, parent, false);
        return new LocationAdapter.LocationViewHolder(view);
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView latitude, longitude, timestamp;
        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}