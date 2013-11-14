package com.tt.android.directoryselector;

import java.io.File;

/**
 * Interface defining events for directory selection listeners.
 * 
 * @author Tuomas Tikka
 */
public interface DirectorySelectorListener {

	/**
	 * Event triggered when a directory is selected.
	 * 
	 * @param directory	The selected directory
	 */
	public void onDirectorySelected(File directory);
	
}
