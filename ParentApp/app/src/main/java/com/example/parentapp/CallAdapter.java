package com.example.parentapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CallAdapter extends FirebaseRecyclerAdapter<Call, CallAdapter.CallViewHolder> {
    // CallAdapter constructor
    public CallAdapter(@NonNull FirebaseRecyclerOptions<Call> options) {
        super(options);
    }

    // onBindViewHolder is called by the RecyclerView when it must bind the data on the created ViewHolder to the layout.
    // It basically binds the row view in the Card view with data from our model class
    @Override
    protected void onBindViewHolder(@NonNull CallViewHolder holder, int position, @NonNull Call model) {
        // Get caller from our model class (Call) and set it on the caller view inside the Card view, do the same for other views too
        holder.caller.setText(model.getCaller());
        holder.timestamp.setText(model.getTimestamp());
        holder.duration.setText(model.getDuration());
        holder.status.setText(model.getStatus());
        holder.type.setText(model.getType());
    }

    // onCreateViewHolder is called by the RecyclerView expecting us to return a ViewHolder class that we are creating below
    // This is the moment in which we inflate the view of our item
    // We are basically telling the Adapter "Hey this is the layout I want for my RecyclerView" meaning call.xml Card view
    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call, parent, false);
        return new CallAdapter.CallViewHolder(view);
    }
    // Our CallViewHolder class
    public class CallViewHolder extends RecyclerView.ViewHolder {
        TextView caller, timestamp, duration, status, type;
        // Constructor
        public CallViewHolder(@NonNull View itemView) {
            super(itemView);
            caller = itemView.findViewById(R.id.caller);
            timestamp = itemView.findViewById(R.id.timestamp);
            duration = itemView.findViewById(R.id.duration);
            status = itemView.findViewById(R.id.status);
            type = itemView.findViewById(R.id.type);
        }
    }
}
