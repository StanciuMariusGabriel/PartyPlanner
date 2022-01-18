package com.example.dam.mainfragments;

import static java.util.List.of;

import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dam.PartyAdapter;
import com.example.dam.PartyVM;
import com.example.dam.R;
import com.example.dam.data.LoginRepository;
import com.example.dam.data.model.Party;

import java.time.LocalDate;
import java.util.ArrayList;

public class FirstFragment extends Fragment {
    RecyclerView partyRV;
    private PartyVM viewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(PartyVM.class);
        partyRV = requireView().findViewById(R.id.idRVParty);
        viewModel.setPartiesForUser(LoginRepository.getInstance(null).getUser().getUserId());
        PartyAdapter partyAdapter = new PartyAdapter(requireActivity().getApplicationContext(), viewModel.getPartiesList(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        partyRV.setLayoutManager(linearLayoutManager);
        partyRV.setAdapter(partyAdapter);

        view.findViewById(R.id.floatingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddPartyDialog();
            }
        });

    }

    void showAddPartyDialog() {
        final Dialog dialog = new Dialog(requireActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.party_dialog);

        //Initializing the views of the dialog.
        final EditText partyName = dialog.findViewById(R.id.party_name);
        final EditText partyLocation = dialog.findViewById(R.id.party_location);
        final DatePicker datePicker = dialog.findViewById(R.id.party_date);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        datePicker.setMinDate(c.getTimeInMillis());
        Button submitButton = dialog.findViewById(R.id.submit_button);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = partyName.getText().toString();
                String location = partyLocation.getText().toString();
                LocalDate date = LocalDate.of(datePicker.getYear(), datePicker.getMonth() +1, datePicker.getDayOfMonth());
                String dateString = "";
                dateString += date.getYear();
                if(date.getMonthValue() < 10)
                    dateString += "-0" + date.getMonthValue();
                else
                    dateString += "-" + date.getMonthValue();
                if(date.getDayOfMonth() < 10)
                    dateString += "-0" + date.getDayOfMonth();
                else
                    dateString += "-" + date.getDayOfMonth();

                if (location.equals(""))
                    Toast.makeText(requireActivity().getApplicationContext(), "Location field can't be empty", Toast.LENGTH_SHORT).show();
                else {

                    if (viewModel.addParty(name, location, dateString, LoginRepository.getInstance(null).getUser().getUserId())) {
                        dialog.dismiss();

                        Toast.makeText(requireActivity().getApplicationContext(), "New party event added", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment);
                    } else {
                        dialog.dismiss();

                        Toast.makeText(requireActivity().getApplicationContext(), "Failed to add party", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();
    }


    public void itemClicked(Party party){
        viewModel.setSelectedItem(party);
        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment);
    }



}