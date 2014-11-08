package com.xtremeware.wanted;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemLayout extends RelativeLayout implements OnClickListener{
	 
	private CheckBox deletedCheckBox;
	private TextView nameTextView;
	private TextView notesTextView;
    private TextView quantityTextView;
    private TextView sizeTextView;
	
	private Item item;
	private ItemListDB db;
	private Context context;
	
	public ItemLayout(Context context) {
		super(context);
	}
	
	public ItemLayout(Context context, Item i) {
		super(context);
		
		// Set context and get the database object
		this.context = context;
		db = new ItemListDB(context);
		
		// Inflate the layout
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.list_view_item, this, true);
		
		// Get the references to the layouts widgets
		deletedCheckBox = (CheckBox)findViewById(R.id.deletedCheckBox);
		nameTextView = (TextView)findViewById(R.id.nameTextView);
		notesTextView = (TextView)findViewById(R.id.notesTextView);
        quantityTextView = (TextView)findViewById(R.id.quantityTextView);
        sizeTextView = (TextView)findViewById(R.id.sizeTextView);
		
		// Set the listeners
		deletedCheckBox.setOnClickListener(this);
		this.setOnClickListener(this);
		
		// Set The Item data on the widgets
		setItem(i);
		
	}

	public void setItem(Item i) {
		item = i;
		nameTextView.setText(item.getName());
        quantityTextView.setText(item.getQuantity());
        sizeTextView.setText(item.getSize());
		
		// Remove the notes if it is empty
		if(item.getNotes().equalsIgnoreCase("")) {
			notesTextView.setVisibility(GONE);
		}
		else {
			notesTextView.setText(item.getNotes());
		}

        if (item.getDeletedDateMillis() > 0) {
            deletedCheckBox.setChecked(true);
        }
        else {
            deletedCheckBox.setChecked(false);
        }
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.deletedCheckBox:
                if (deletedCheckBox.isChecked()){
                    item.setDeletedDate(System.currentTimeMillis());
                }
                else
                {
                    item.setDeletedDate(0);
                }
				db.updateItem(item);
				break;
			default:
				Intent intent = new Intent(context, AddEditActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("itemId", item.getId());
				intent.putExtra("editMode", true);
				context.startActivity(intent);
				break;
		}
		
	}

}
