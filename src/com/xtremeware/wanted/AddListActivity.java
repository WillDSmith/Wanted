package com.xtremeware.wanted;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;


/**
 * Created by Shakey on 12/14/13.
 */
public class AddListActivity extends Activity implements View.OnKeyListener {

    private EditText newListNameEditText;
    private Spinner addlistSpinner;

    private ItemListDB db;
    private boolean editMode;
    private String currentTabName = "";
    private List list;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);

        // Get references to widgets
        newListNameEditText = (EditText)findViewById(R.id.listNameEditText);
        addlistSpinner = (Spinner) findViewById(R.id.addlistSpinner);

        // Set Listeners
        newListNameEditText.setOnKeyListener(this);

        // Get the database object
        db = new ItemListDB(this);

        // Set the adapter for the spinner for the list items in the database
        ArrayList<List> lists = db.getLists();
        ArrayAdapter<List> adapter = new ArrayAdapter<List>(this, R.layout.spinner_list, lists);
        addlistSpinner.setAdapter(adapter);


        // Get the edit mode from the intent
        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode", false);

        // If editing
        if (editMode) {
            // Get List
            long listId = intent.getLongExtra("listId", -1);
            list = db.getList(listId);

            // Update UI with the list
            newListNameEditText.setText(list.getName());
        }

        // Set the correct list for the spinner
        long listID;
        if(editMode){
            // Edit Mode - Use the same list as the selected tab
            listID = (int) list.getId();
        }
        else {
            // Add Mode - Use the list for the current tab
            currentTabName = intent.getStringExtra("tab");
            listID = (int) db.getList(currentTabName).getId();

        }

        // Subtract 1 from the database ID to get correct list position
        int listPosition = (int) listID - 1;
        addlistSpinner.setSelection(listPosition);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSave:
                saveToDatbase();
                this.finish();
                break;
            case R.id.menuCancel:
                this.finish();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void saveToDatbase() {
        // Get the data from the widgets
        int listID = addlistSpinner.getSelectedItemPosition() + 1;
        String name = newListNameEditText.getText().toString();


        // If there is no item name, then exit the method
        if ( name == null || name.equals("")) {
            return;
        }

        // If in add mode, create a new list item
        if(!editMode) {
            list = new List();
        }

        // Put some data in the list
        list.setId(listID);
        list.setName(name);


        // Update or insert item data
        if(editMode) {
            db.updateList(list);

        }
        else {
            db.insertList(list);
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            // Hide the soft keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveToDatbase();
            return false;
        }
        return false;
    }


}
