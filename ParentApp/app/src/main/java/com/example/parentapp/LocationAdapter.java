package com.example.parentapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;

// LocationAdapter is based on the same logic as CallAdapter class, for comments regarding the implementation check the CallAdapter class
public class LocationAdapter extends FirebaseRecyclerAdapter<Location, LocationAdapter.LocationViewHolder> {
    private Context context;
    // Passing context to the constructor as its needed to launch the maps activity
    public LocationAdapter(@NonNull FirebaseRecyclerOptions<Location> options, Context context) {
        super(options);
        this.context = context;
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

    public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView latitude, longitude, timestamp;
        Button mapButton;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            timestamp = itemView.findViewById(R.id.timestamp);

            mapButton = itemView.findViewById(R.id.mapButton);
            mapButton.setOnClickListener(this);
        }
        // Set up the onClick method for the mapButton for each view, getting the coordinates and launching the MapsActivity with them
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.mapButton) {
                String lat = latitude.getText().toString();
                String lng = longitude.getText().toString();

                // Create intent, put String extras (coordinates and timestamp) and startActivity
                Intent mapIntent = new Intent(context, MapsActivity.class);
                mapIntent.putExtra("latitude", lat);
                mapIntent.putExtra("longitude", lng);
                mapIntent.putExtra("timestamp", timestamp.getText());
                context.startActivity(mapIntent);
            }
        }
    }
}