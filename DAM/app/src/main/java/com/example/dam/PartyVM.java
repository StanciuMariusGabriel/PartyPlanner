package com.example.dam;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dam.data.LoginRepository;
import com.example.dam.data.model.LoggedInUser;
import com.example.dam.data.model.Party;
import com.example.dam.database.DbDataSource;

import java.util.ArrayList;

public class PartyVM extends ViewModel {
    private final MutableLiveData<Party> selectedItem = new MutableLiveData<Party>();
    private final MutableLiveData<ArrayList<Party>> partiesList = new MutableLiveData<ArrayList<Party>>();
    private DbDataSource dataSource = new DbDataSource();

    public void setSelectedItem(Party party) {
        selectedItem.setValue(party);
    }

    public Party getSelectedItem() {
        return selectedItem.getValue();
    }

    public void setPartiesForUser(int userID) {
        partiesList.setValue(dataSource.getPartiesForUser(userID));
    }

    public ArrayList<Party> getPartiesList() {
        return partiesList.getValue();
    }

    public boolean partyAccepted(int userID, int partyID) {
        return dataSource.isAcceptedForUser(userID, partyID);
    }

    public ArrayList<String> getInviteList(int partyID) {
        return dataSource.getInvitedAcceptedUsernames(partyID);
    }

    public ArrayList<String> getSearchInviteList(int partyID) {
        return dataSource.getNotInvitedUsernames(partyID);
    }

    public void updateIsAccepted(int userID, int partyID, boolean accept) {
        dataSource.updateIsAccepted(userID, partyID, accept);
    }

    public void updateParty(int partyID, String name, String location, String date) {
        if (name.equals(""))
            name = "Untitled Party";
        dataSource.updateParty(partyID, name, location, date);
        selectedItem.getValue().setPartyName(name);
        selectedItem.getValue().setPartyLocation(location);
        selectedItem.getValue().setPartyDate(date);
    }

    public boolean addParty(String name, String location, String date, int userID) {
        if (name.equals(""))
            name = "Untitled Party";
        int partyID = dataSource.addParty(name, location, date, userID);
        if (partyID != -1) {
            dataSource.addInvitation(userID, partyID, true);
            setPartiesForUser(userID);
            setSelectedItem(partiesList.getValue().get(partiesList.getValue().size() - 1));
            return true;
        }
        return false;
    }

    public boolean addInvitation(String username, int partyID) {
        int userID = dataSource.getUserIdFromUsername(username);
        if (userID != -1) {
            dataSource.addInvitation(userID, partyID, false);
            return true;
        }
        return false;
    }

    public boolean addUser(String username, String password) {
        int id = dataSource.addUser(username, password);
        if (id != -1) {
            LoginRepository.getInstance(null).login(username, password);
            return true;
        }
        return false;
    }

    public void deleteParty(int partyID){
        dataSource.deleteParty(partyID);
    }

    public void deleteInvite(int userID, int partyID){
        dataSource.deleteUserParty(userID, partyID);
    }
}
