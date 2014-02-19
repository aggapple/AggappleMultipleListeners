package com.aggapple.multiplelisteners;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView mList;
	private ItemAdapter<Item> mAdapter;
	private ArrayList<Item> mItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mList = (ListView) findViewById(R.id.list);
		mAdapter = new ItemAdapter<Item>();
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(onItemClick);

		setData();
	}

	private void setData() {
		mItems = new ArrayList<Item>();
		final int itemSize = 10;
		for (int i = 0; i < itemSize; i++) {
			mItems.add(new Item("item" + i));
		}
		mAdapter.set(mItems);
		mAdapter.notifyDataSetChanged();
	}

	public enum CHECK_STATE { // 체크 상태
		checked, unchecked
	}

	public class Item {
		public String isChecked;
		public String text;

		public Item(String text) {
			this.text = text;
			this.isChecked = CHECK_STATE.unchecked.toString();
		}
	}

	private OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long resId) {
			Toast.makeText(MainActivity.this, ((Item) parent.getItemAtPosition(position)).text, Toast.LENGTH_SHORT).show();
		}
	};

	public class OnCheckedChange implements OnCheckedChangeListener {
		Item mItem;

		public void setItem(Item item) {
			mItem = item;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			mItem.isChecked = (isChecked ? CHECK_STATE.checked.toString() : CHECK_STATE.unchecked.toString());
			mAdapter.notifyDataSetChanged();
		}
	}

	public class OnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// View의 Tag를 이용하는 방식
			Holder h = (Holder) v.getTag();
			Toast.makeText(MainActivity.this, h.text.getText() + " Button", Toast.LENGTH_SHORT).show();
		}

	}

	public class Holder {
		public TextView text;
		public CheckBox checkBox;
		public Button button;
		public OnCheckedChange onCheckedChange = new OnCheckedChange();
		public OnClick onClick = new OnClick();

		public View set(View v) {
			v.setTag(this);

			text = (TextView) v.findViewById(R.id.text);
			// item을 연결하는 방식
			checkBox = (CheckBox) v.findViewById(R.id.checkbox);
			checkBox.setOnCheckedChangeListener(onCheckedChange);
			// View의 Tag를 이용하는 방식
			button = (Button) v.findViewById(R.id.button);
			button.setOnClickListener(onClick);
			button.setTag(this);
			return v;
		}
	}

	public class ItemAdapter<T extends Item> extends BaseAdapter {
		public ArrayList<T> mListItem = new ArrayList<T>(20);

		public void set(Collection<T> collection) {
			mListItem.clear();
			mListItem.addAll(collection);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mListItem.size();
		}

		@Override
		public Object getItem(int position) {
			return mListItem.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = new Holder().set(LayoutInflater.from(getBaseContext()).inflate(R.layout.list_item, parent, false));

			final Holder h = (Holder) convertView.getTag();
			final Item item = (Item) getItem(position);

			h.text.setText(item.text);
			h.checkBox.setText(item.isChecked);

			// Listener에 해당 아이템을 연결
			h.onCheckedChange.setItem(item);

			return convertView;
		}
	}

}
