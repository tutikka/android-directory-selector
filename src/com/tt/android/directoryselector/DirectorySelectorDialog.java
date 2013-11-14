package com.tt.android.directoryselector;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Directory selector component implemented as a dialog.
 * 
 * @author Tuomas Tikka
 */
public class DirectorySelectorDialog extends Dialog implements OnItemClickListener, android.view.View.OnClickListener {

	private static final String TAG_CANCEL = "cancel";
	
	private static final String TAG_HOME = "home";
	
	private static final String TAG_SELECT = "select";
	
	private File current;
	
	private List<DirectorySelectorListener> listeners = new ArrayList<DirectorySelectorListener>();
	
	private ArrayAdapter<File> listAdapter;
	
	private Button cancel;
	
	private Button home;
	
	private Button select;
	
	public DirectorySelectorDialog(Context context, File directory) throws IllegalArgumentException {
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
		EditText et = (EditText) findViewById(R.id.directory_selector_current);
		et.setText(current.getAbsolutePath());
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
	
}
