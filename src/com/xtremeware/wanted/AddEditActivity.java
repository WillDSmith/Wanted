package com.xtremeware.wanted;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class AddEditActivity extends Activity implements OnKeyListener{
    private EditText nameEditText;
    private EditText notesEditText;
    private EditText sizeEditText;
    private EditText quantityEditText;
    private Spinner listSpinner;

    private ItemListDB db;
    private boolean editMode;
    private String currentTabName = "";
    private  Item item;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        // Get the references to the widgets
        listSpinner = (Spinner) findViewById(R.id.listSpinner);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        notesEditText = (EditText) findViewById(R.id.notesEditText);
        sizeEditText = (EditText) findViewById(R.id.sizeEditText);
        quantityEditText = (EditText) findViewById(R.id.quantityEditText);

        // Set the listeners
        nameEditText.setOnKeyListener(this);
        notesEditText.setOnKeyListener(this);
        sizeEditText.setOnKeyListener(this);
        quantityEditText.setOnKeyListener(this);

        // The the database object
        db = new ItemListDB(this);

        // Set the adapter for the spinner for the list items in the database
        ArrayList<List> lists = db.getLists();
        ArrayAdapter<List> adapter = new ArrayAdapter<List>(this, R.layout.spinner_list, lists);
        listSpinner.setAdapter(adapter);

        // Get the edit mode from the intent
        Intent intent = getIntent();
        editMode = intent.getBooleanExtra("editMode", false);

        // If in edit mode
        if (editMode) {
            // Get The Item
            long itemId = intent.getLongExtra("itemId", -1);
            item = db.getItem(itemId);

            // Update UI with the Item
            nameEditText.setText(item.getName());
            notesEditText.setText(item.getNotes());
            quantityEditText.setText(item.getQuantity());
            sizeEditText.setText(item.getSize());
        }

        // Set the correct list for the spinner
        long listID;
        if(editMode){
            // Edit Mode - Use the same list as the selected tab
            listID = (int) item.getListId();
        }
        else {
            // Add Mode - Use the list for the current tab
            currentTabName = intent.getStringExtra("tab");
            listID = (int) db.getList(currentTabName).getId();
        }

        // Subtract 1 from the database ID to get correct list position
        int listPosition = (int) listID - 1;
        listSpinner.setSelection(listPosition);
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
        int listID = listSpinner.getSelectedItemPosition() + 1;
        String name = nameEditText.getText().toString();
        String notes = notesEditText.getText().toString();
        String quantity = quantityEditText.getText().toString();
        String size = sizeEditText.getText().toString();

        // If there is no item name, then exit the method
        if ( name == null || name.equals("")) {
            return;
        }

        // If in add mode, create a new item
        if(!editMode) {
            item = new Item();
        }

        // Put some data in the item
        item.setListId(listID);
        item.setName(name);
        item.setNotes(notes);
        item.setQuantity(quantity);
        item.setSize(size);

        // Update or insert item data
        if(editMode) {
            db.updateItem(item);
        }
        else {
            db.insertItem(item);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            // Hide the soft keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveToDatbase();
            return false;
        }
        return false;
    }
}
