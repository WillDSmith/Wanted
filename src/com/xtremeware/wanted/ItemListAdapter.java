package com.xtremeware.wanted;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ItemListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Item> items;

	
	public ItemListAdapter(Context context, ArrayList<Item> items){
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemLayout itemLayout = null;
		Item item = items.get(position);
		
		if (convertView == null) {
			itemLayout = new ItemLayout(context, item);
		}
		else {
			itemLayout = (ItemLayout) convertView;
			itemLayout.setItem(item);
		}
		return itemLayout;
	}

}
