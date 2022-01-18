package com.example.dam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dam.data.model.Party;
import com.example.dam.mainfragments.FirstFragment;

import java.util.ArrayList;

public class PartyAdapter extends RecyclerView.Adapter<PartyAdapter.Viewholder> {

    private Context context;
    private ArrayList<Party> partiesArrayList;
    private FirstFragment fragment;

    // Constructor
    public PartyAdapter(Context context, ArrayList<Party> partiesArrayList, FirstFragment activity) {
        this.context = context;
        this.partiesArrayList = partiesArrayList;
        this.fragment = activity;
    }

    @NonNull
    @Override
    public PartyAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PartyAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        Party party = partiesArrayList.get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                fragment.itemClicked(party);
            }
        });
        holder.partyName.setText(party.getPartyName());
        holder.partyLocation.setText(party.getPartyLocation());
        holder.partyDate.setText(party.getPartyDate());
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return partiesArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private View view;
        private TextView partyName, partyLocation, partyDate;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            partyName = itemView.findViewById(R.id.idPartyName);
            partyLocation = itemView.findViewById(R.id.idPartyLocation);
            partyDate = itemView.findViewById(R.id.idPartyDate);
        }
    }
}
