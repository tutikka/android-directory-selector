package com.tt.android.directoryselector.sample;

import java.io.File;

import com.tt.android.directorypicker.R;
import com.tt.android.directoryselector.DirectorySelectorDialog;
import com.tt.android.directoryselector.DirectorySelectorListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Sample activity for the Directory Picker library.
 * 
 * @author Tuomas Tikka
 */
public class SampleActivity extends Activity implements OnClickListener, DirectorySelectorListener {

	private static final String TAG_SELECT = "select";
	
	private File selectedDirectory = Environment.getExternalStorageDirectory();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.sample);
		
		Button pick = (Button) findViewById(R.id.sample_select);
		pick.setTag(TAG_SELECT);
		pick.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (TAG_SELECT.equals(view.getTag())) {
			try {
				DirectorySelectorDialog directorySelector = new DirectorySelectorDialog(this, selectedDirectory);
				directorySelector.addDirectorySelectorListener(this);
				directorySelector.show();
			} catch (IllegalArgumentException e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onDirectorySelected(File directory) {
		selectedDirectory = directory;
		Toast.makeText(this, "Selected '" + directory.getAbsolutePath() + "'", Toast.LENGTH_SHORT).show();
	}
	
}
