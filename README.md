android-directory-selector
==========================

Simple and customizable directory selector component for Android.

Screenshots
-----------

Screenshot on Android 4.3 using the default styles:

![ScreenShot](/screenshots/ss-1.png)

Screenshot on Android 2.3.3 using the default styles:

![ScreenShot](/screenshots/ss-2.png)

Usage
-----

Integrating the component to your existing project is very simple. Probably the easiest approach is (using Eclipse):

1. Clone the project
2. Import the project into your workspace
3. Run the sample application and see how the it works from the source code
4. Turn the project into a library (project properties, Android, Is Library)
5. Reference the project as a library from your own stuff

Example
-------

Example code from SampleActivity.java:

<pre>
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

public class SampleActivity extends Activity implements OnClickListener, DirectorySelectorListener {

	private static final String TAG_SELECT = "select";
	
	private File selectedDirectory = Environment.getExternalStorageDirectory();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample);
		Button select = (Button) findViewById(R.id.sample_select);
		select.setTag(TAG_SELECT);
		select.setOnClickListener(this);
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
</pre>

Customization
-------------

TODO

Credits
-------

TODO  