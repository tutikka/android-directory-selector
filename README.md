android-directory-selector
==========================

Simple and customizable directory selector component for Android.

Screenshots
-----------

Using the default style resources in Android 4.3:

![ScreenShot](/screenshots/ss-1.png)

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

Create the directory selector dialog:

<pre>
DirectorySelectorDialog directorySelector = new DirectorySelectorDialog(this, Environment.getExternalStorageDirectory());
directorySelector.addDirectorySelectorListener(this);
directorySelector.show();	
</pre>

Listen for a directory selection:

<pre>
@Override
public void onDirectorySelected(File directory) {
	Toast.makeText(this, "Selected '" + directory.getAbsolutePath() + "'", Toast.LENGTH_SHORT).show();
}	
</pre>

Please check SampleActivity.java for the full sample source code.

Customization
-------------

TODO

Credits
-------

TODO  