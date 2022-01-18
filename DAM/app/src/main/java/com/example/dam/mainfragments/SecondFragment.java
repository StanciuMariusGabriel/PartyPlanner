package com.example.dam.mainfragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.dam.PartyVM;
import com.example.dam.R;
import com.example.dam.data.LoginRepository;

import java.time.LocalDate;
import java.util.ArrayList;

public class SecondFragment extends Fragment {
    private PartyVM viewModel;
    ArrayList<String> inviteList;

    private ListView mainListView;
    private TextView partyName;
    private TextView partyLocation;
    private TextView partyDate;
    private TextView noOfParticipants;

    private ListView searchListView;
    private SearchView searchView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(PartyVM.class);

        partyName = view.findViewById(R.id.party_name_text);
        partyName.setText(viewModel.getSelectedItem().getPartyName());
        partyLocation = view.findViewById(R.id.party_location_text);
        partyLocation.setText(viewModel.getSelectedItem().getPartyLocation());
        partyDate = view.findViewById(R.id.party_date_text);
        partyDate.setText(viewModel.getSelectedItem().getPartyDate().replace('-', '/'));
        noOfParticipants = view.findViewById(R.id.party_participants);

        mainListView = view.findViewById(R.id.invite_list);

        inviteList = viewModel.getInviteList(viewModel.getSelectedItem().getId());
        noOfParticipants.setText(inviteList.size() + (inviteList.size() == 1 ? " Participant" : " Participants"));

        ArrayAdapter arrayAdapter = new ArrayAdapter(requireActivity().getApplicationContext(), R.layout.list_items_layout, inviteList);

        mainListView.setAdapter(arrayAdapter);

        if (LoginRepository.getInstance(null).getUser().getUserId() == viewModel.getSelectedItem().getIdUserPlanner()) {
            view.findViewById(R.id.editFloatingBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEditPartyDialog();
                }
            });

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            viewModel.deleteParty(viewModel.getSelectedItem().getId());
                            NavHostFragment.findNavController(SecondFragment.this)
                                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            view.findViewById(R.id.removeFloatingBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Are you sure you want to cancel this party?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });

            view.findViewById(R.id.invite_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showInviteDialog();
                }
            });
        } else {
            view.findViewById(R.id.editFloatingBtn).setVisibility(View.GONE);

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            viewModel.deleteInvite(LoginRepository.getInstance(null).getUser().getUserId(), viewModel.getSelectedItem().getId());
                            NavHostFragment.findNavController(SecondFragment.this)
                                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            view.findViewById(R.id.removeFloatingBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Are you sure you want to remove this invitation?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
            });
            view.findViewById(R.id.invite_button).setVisibility(View.GONE);

            if (viewModel.partyAccepted(LoginRepository.getInstance(null).getUser().getUserId(), viewModel.getSelectedItem().getId())) {
                view.findViewById(R.id.decline_invite_button).setVisibility(View.VISIBLE);
                view.findViewById(R.id.decline_invite_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.updateIsAccepted(LoginRepository.getInstance(null).getUser().getUserId(), viewModel.getSelectedItem().getId(), false);
                        NavHostFragment.findNavController(SecondFragment.this)
                                .navigate(R.id.action_SecondFragment_to_SecondFragment);
                    }
                });
                view.findViewById(R.id.accept_invite_button).setVisibility(View.GONE);
            } else {
                view.findViewById(R.id.accept_invite_button).setVisibility(View.VISIBLE);
                view.findViewById(R.id.accept_invite_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewModel.updateIsAccepted(LoginRepository.getInstance(null).getUser().getUserId(), viewModel.getSelectedItem().getId(), true);
                        NavHostFragment.findNavController(SecondFragment.this)
                                .navigate(R.id.action_SecondFragment_to_SecondFragment);
                    }
                });
                view.findViewById(R.id.decline_invite_button).setVisibility(View.GONE);
            }
        }

    }

    void showEditPartyDialog() {
        final Dialog dialog = new Dialog(requireActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.party_dialog);

        TextView title = dialog.findViewById(R.id.party_dialog_title);
        title.setText("Edit Party");

        final EditText editPartyName = dialog.findViewById(R.id.party_name);
        editPartyName.setText(partyName.getText());

        final EditText editPartyLocation = dialog.findViewById(R.id.party_location);
        editPartyLocation.setText(partyLocation.getText());

        String[] splitDate = partyDate.getText().toString().split("/");
        final DatePicker datePicker = dialog.findViewById(R.id.party_date);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        datePicker.setMinDate(c.getTimeInMillis());
        datePicker.updateDate(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]) - 1, Integer.parseInt(splitDate[2]));

        Button submitButton = dialog.findViewById(R.id.submit_button);
        submitButton.setText("Confirm Changes");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editPartyName.getText().toString();
                String location = editPartyLocation.getText().toString();
                LocalDate date = LocalDate.of(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());
                String dateString = "";
                dateString += date.getYear();
                if (date.getMonthValue() < 10)
                    dateString += "-0" + date.getMonthValue();
                else
                    dateString += "-" + date.getMonthValue();
                if (date.getDayOfMonth() < 10)
                    dateString += "-0" + date.getDayOfMonth();
                else
                    dateString += "-" + date.getDayOfMonth();

                if (location.equals(""))
                    Toast.makeText(requireActivity().getApplicationContext(), "Location field can't be empty", Toast.LENGTH_SHORT).show();
                else {
                    viewModel.updateParty(viewModel.getSelectedItem().getId(), name, location, dateString);

                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_SecondFragment);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    void showInviteDialog() {
        final Dialog dialog = new Dialog(requireActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.invite_dialog);

        searchView = (SearchView) dialog.findViewById(R.id.people_search_view);
        searchListView = dialog.findViewById(R.id.invite_search_list);

        inviteList = viewModel.getSearchInviteList(viewModel.getSelectedItem().getId());
        inviteList.remove(LoginRepository.getInstance(null).getUser().getDisplayName());

        ArrayAdapter arrayAdapter = new ArrayAdapter(requireActivity().getApplicationContext(), R.layout.list_items_layout, inviteList);
        searchListView.setAdapter(arrayAdapter);
        arrayAdapter.getFilter().filter(".");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (inviteList.contains(query)) {
                    arrayAdapter.getFilter().filter(query);
                } else {
                    Toast.makeText(requireActivity().getApplicationContext(), "No Match found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(""))
                    arrayAdapter.getFilter().filter(".");
                else {
                    arrayAdapter.getFilter().filter(newText);
                    searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (viewModel.addInvitation(parent.getItemAtPosition(position).toString(), viewModel.getSelectedItem().getId()))
                                Toast.makeText(requireActivity().getApplicationContext(), "Sent invite to " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(requireActivity().getApplicationContext(), "Failed to send invite", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }
                return false;
            }
        });

        dialog.show();
    }
}