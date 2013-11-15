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

1. Clone and import the project into your workspace
2. Reference the project as a library from your application
3. Check out the example below how to take it into use

For more information on using Android library projects, see:

http://developer.android.com/tools/projects/projects-eclipse.html

Example
-------

```java
package com.tt.android.directoryselector.sample;

import java.io.File;

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
				DirectorySelectorDialog dialog = new DirectorySelectorDialog.Builder(this)
					.directory(selectedDirectory)
					.sortBy(DirectorySelectorDialog.SORT_NAME)
					.orderBy(DirectorySelectorDialog.ORDER_ASCENDING)
					.build();				
				dialog.addDirectorySelectorListener(this);
				dialog.show();
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
``

Customization
-------------

TODO

Credits
-------

TODO  