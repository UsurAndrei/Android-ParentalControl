package com.example.parentapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

// MessageAdapter is based on the same logic as CallAdapter class, for comments regarding the implementation check the CallAdapter class
public class MessageAdapter extends FirebaseRecyclerAdapter<Message, MessageAdapter.MessageViewHolder> {
    public MessageAdapter(@NonNull FirebaseRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
        holder.message.setText(model.getMessage());
        holder.number.setText(model.getNumber());
        holder.timestamp.setText(model.getTimestamp());
        holder.type.setText(model.getType());
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MessageAdapter.MessageViewHolder(view);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView message, number, timestamp, type;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            number = itemView.findViewById(R.id.number);
            timestamp = itemView.findViewById(R.id.timestamp);
            type = itemView.findViewById(R.id.type);
        }
    }
}
