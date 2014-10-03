package com.tt.android.directoryselector;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.tt.android.directorypicker.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DirectorySelectorDialog extends Dialog implements OnItemClickListener, android.view.View.OnClickListener {

	public static final int SORT_NAME = 0;
	
	public static final int SORT_MODIFIED = 1;
	
	public static final int ORDER_ASCENDING = 1;
	
	public static final int ORDER_DESCENDING = -1;
	
	private static final String TAG_CANCEL = "cancel";
	
	private static final String TAG_HOME = "home";
	
	private static final String TAG_SELECT = "select";
	
	private File current;
	
	private List<DirectorySelectorListener> listeners = new ArrayList<DirectorySelectorListener>();
	
	private ArrayAdapter<File> listAdapter;
	
	private Button cancel;
	
	private Button home;
	
	private Button select;
	
	private DirectoryListComparator comparator;
	
	public static class Builder {
		
		private Context context;
		
		private File directory = Environment.getExternalStorageDirectory();
		
		private int sortBy = DirectorySelectorDialog.SORT_NAME;
		
		private int orderBy = DirectorySelectorDialog.ORDER_ASCENDING;
		
		public Builder(Context context) {
			this.context = context;
		}
		
		public Builder directory(File directory) {
			this.directory = directory;
			return (this);
		}
		
		public Builder sortBy(int sortBy) {
			this.sortBy = sortBy;
			return (this);
		}
		
		public Builder orderBy(int orderBy) {
			this.orderBy = orderBy;
			return (this);
		}
		
		public DirectorySelectorDialog build() throws IllegalArgumentException {
			return (new DirectorySelectorDialog(context, directory, sortBy, orderBy));
		}
		
	}
	
	private DirectorySelectorDialog(Context context, File directory, int sortBy, int orderBy) throws IllegalArgumentException {
		super(context);
		
		if (directory == null) {
			throw new IllegalArgumentException("entry directory is null");
		}
		
		if (!directory.exists()) {
			throw new IllegalArgumentException("entry directory does not exist");
		}
		
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException(directory.getAbsolutePath() + " is not a directory");
		}
		
		if (!directory.canRead()) {
			throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable");
		}
		
		if (sortBy != SORT_NAME && sortBy != SORT_MODIFIED) {
			throw new IllegalArgumentException("invalid sort criteria");
		}
		
		if (orderBy != ORDER_ASCENDING && orderBy != ORDER_DESCENDING) {
			throw new IllegalArgumentException("invalid order criteria");
		}
		
		setTitle(R.string.directory_selector_dialog_title);
		setContentView(R.layout.directory_selector_dialog);
		
		listAdapter = new DirectoryListAdapter(getContext(), new ArrayList<File>());
		listAdapter.setNotifyOnChange(true);
		
		ListView lv = (ListView) findViewById(R.id.directory_selector_list);
		lv.setAdapter(listAdapter);
		lv.setOnItemClickListener(this);
		
		cancel = (Button) findViewById(R.id.directory_selector_cancel);
		cancel.setTag(TAG_CANCEL);
		cancel.setOnClickListener(this);
		
		home = (Button) findViewById(R.id.directory_selector_home);
		home.setTag(TAG_HOME);
		home.setOnClickListener(this);
		
		select = (Button) findViewById(R.id.directory_selector_select);
		select.setTag(TAG_SELECT);
		select.setOnClickListener(this);
		
		current = directory;
		
		comparator = new DirectoryListComparator(sortBy, orderBy);
		
		list(current);
	}	
	
	public void addDirectorySelectorListener(DirectorySelectorListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void removeDirectorySelectorListener(DirectorySelectorListener listener) {
		listeners.remove(listener);
	}
	
	private void list(File directory) {
		TextView tv = (TextView) findViewById(R.id.directory_selector_current);
		tv.setText(current.getAbsolutePath());
		listAdapter.clear();
		File[] files = directory.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return (pathname.isDirectory());
			}
		});
		if (directory.getParent() != null) {
			listAdapter.add(null);
		}
		if (files != null) {
			for (File file : files) {
				listAdapter.add(file);
			}
		}
		listAdapter.sort(comparator);
	}
	
	public void onClick(View view) {
		if (TAG_CANCEL.equals(view.getTag())) {
			cancel();
		}
		if (TAG_HOME.equals(view.getTag())) {
			current = Environment.getExternalStorageDirectory();
			list(current);
		}
		if (TAG_SELECT.equals(view.getTag())) {
			for (DirectorySelectorListener listener : listeners) {
				listener.onDirectorySelected(current);
			}
			cancel();
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		File file = listAdapter.getItem(arg2);
		if (file == null) {
			current = current.getParentFile();
		} else {
			current = file;
		}
		list(current);
	}
	
	private class DirectoryListAdapter extends ArrayAdapter<File> {
		
		public DirectoryListAdapter(Context context, List<File> dirs) {
			super(context, 0, dirs);
		}
		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View v = inflater.inflate(R.layout.directory_list_item, null);
	        
	        TextView name = (TextView) v.findViewById(R.id.directory_name);
	        
	        ImageView icon = (ImageView) v.findViewById(R.id.directory_icon);
	        
	        File f = getItem(position);
	        if (f == null) {
	        	name.setText(R.string.directory_selector_dialog_parent);
	        	icon.setImageResource(R.drawable.up);
	        } else {
	        	name.setText(f.getName());
	        	icon.setImageResource(R.drawable.folder);
	        }
	        return (v);
	    }
	 
	}	
	
	private class DirectoryListComparator implements Comparator<File> {

		private int sort;
		
		private int order;
		
		private DirectoryListComparator(int sort, int order) {
			this.sort = sort;
			this.order = order;
		}
		
		@Override
		public int compare(File lhs, File rhs) {
			if (lhs == null || rhs == null) {
				return (1);
			}
			switch (sort) {
			case SORT_NAME : return (order * lhs.getName().compareTo(rhs.getName()));
			case SORT_MODIFIED : return (order * Long.valueOf(lhs.lastModified()).compareTo(Long.valueOf(rhs.lastModified())));
			default: return (order * lhs.getName().compareTo(rhs.getName()));
			}
			
		}
		
	}
	
}
