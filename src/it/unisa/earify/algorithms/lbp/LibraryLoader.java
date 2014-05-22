package it.unisa.earify.algorithms.lbp;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import android.app.Activity;
import android.util.Log;

public class LibraryLoader extends  BaseLoaderCallback {

	private String libPath;

	public LibraryLoader(Activity AppContext,String librarisPath) {
		super(AppContext);
		this.libPath=librarisPath;	
	}

	@Override
	public void onManagerConnected(int status) {
		switch (status) {
		case LoaderCallbackInterface.SUCCESS:
		{
			Log.i("messaggio", "OpenCV loaded successfully");

			// Load native library after OpenCV initialization
			System.loadLibrary(this.libPath);			
		} break;
		default:
		{
			super.onManagerConnected(status);
		} break;
		}
	}
}
