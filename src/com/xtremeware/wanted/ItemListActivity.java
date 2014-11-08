package com.xtremeware.wanted;

import java.util.ArrayList;

import com.google.tabmanager.TabManager;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

public class ItemListActivity extends FragmentActivity {
	
	TabHost tabHost;
	TabManager tabManager;
    ItemListDB db;
    private TabWidget tabwidget;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_list);
		
		// Get The Tab Manager
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabManager = new TabManager(this, tabHost, R.id.realtabcontent);
		
		// Get The Lists From The Database
		db = new ItemListDB(getApplicationContext());

		
		// Add A Tab For Each List
        ArrayList<List> lists = db.getLists();
		if (lists != null && lists.size() > 0) {

			for (List list : lists) {
				TabSpec tabSpec = tabHost.newTabSpec(list.getName());
				tabSpec.setIndicator(list.getName());
				tabManager.addTab(tabSpec, ItemListFragment.class, null);
			}
		}
		
		// Set current tab to the last tab opened
		if (savedInstanceState != null) {
			tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		}
		
	}

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();



    }

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
        outState.putString("tab", tabHost.getCurrentTabTag());

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_item_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAddItem:
                Intent intent = new Intent(this, AddEditActivity.class);
                intent.putExtra("tab", tabHost.getCurrentTabTag());
                startActivity(intent);
                break;
            case R.id.menuAddList:
                Intent listIntent = new Intent(this, AddListActivity.class);
                listIntent.putExtra("tab", tabHost.getCurrentTabTag());
                startActivity(listIntent);
                break;
            case R.id.menuDelete:
                // Hide all items marked as delete
                ArrayList<Item> items = db.getItems(tabHost.getCurrentTabTag());
                for (Item mitem : items) {
                    if(mitem.getDeletedDateMillis() > 0) {
                       mitem.setHidden(Item.TRUE);
                       db.updateItem(mitem);
                    }
                }
                // Refresh list
                ItemListFragment currentFragment = (ItemListFragment) getSupportFragmentManager().findFragmentByTag(tabHost.getCurrentTabTag());
                currentFragment.refreshItemList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
