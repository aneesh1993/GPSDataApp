package com.example.aneesh.gpsdataapp;


import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServerFragment extends Fragment {


    public ServerFragment() {
        // Required empty public constructor
    }

    ListView serverList;
    TextView serverStatus, networkType;
    String networkDetails = "";
    Button syncButton;

    ArrayList<String> serverDBList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_server, container, false);

        serverList = (ListView) view.findViewById(R.id.serverList);
        serverStatus = (TextView)view.findViewById(R.id.serverStatus);
        networkType = (TextView)view.findViewById(R.id.networkType);
        syncButton = (Button)view.findViewById(R.id.syncButton);

        Bundle args = getArguments();

        if(!networkDetails.equals("")){
            networkType.setText(networkDetails);
        }

        if(args != null){
            networkType.setText(args.getString("networkInfo"));
            serverStatus.setText(args.getString("serverStatus"));
        }


        return view;
    }

    public void populateList(ArrayList<String> list){
        System.out.println("HEREEE!!");
        serverDBList = list;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, list);
        serverList.setAdapter(adapter);
    }

    public void setNetworkStatus(String networkInfo){
        networkType.setText(networkInfo);
        networkDetails = networkInfo;
    }

    public void setConnectionStatus(String conn){
        serverStatus.setText(conn);
    }

}
