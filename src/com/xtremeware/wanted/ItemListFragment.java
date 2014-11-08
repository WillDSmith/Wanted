package com.xtremeware.wanted;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

/**
 * Created by Shakey on 12/10/13.
 */
public class ItemListFragment extends Fragment{

    private ListView itemListView;
    private String currentTabTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        
        
        // Get the reference to the widgets
        itemListView = (ListView)view.findViewById(R.id.itemListView);


        
        // Get the currentTab
        TabHost tabHost = (TabHost) container.getParent().getParent();
        currentTabTag = tabHost.getCurrentTabTag();


        // Refresh the item list view
        refreshItemList();


        // Return the view
        return view;

    }
    
    public void refreshItemList() {
    	// Get the list for the current tab from database
    	Context context = getActivity().getApplicationContext();
    	ItemListDB db = new ItemListDB(context);
    	ArrayList<Item> items = db.getItems(currentTabTag);
        //ArrayList<List> lists = db.getLists();



        //String text = "This is the " + currentTabTag + " list.";
    	
    	// Create adapter and set it in the ListView widget
    	ItemListAdapter adapter = new ItemListAdapter(context, items);
    	itemListView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
    	super.onResume();
    	refreshItemList();


    }

}

