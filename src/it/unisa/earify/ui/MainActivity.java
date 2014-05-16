package it.unisa.earify.ui;

import it.unisa.earify.R;
import it.unisa.earify.config.Config;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private String selectedImagePath;
	private int ear_code; /* dx = 0, sx = 1 */
	private ArrayList<String> imagesPath;
	private static final int SELECT_PICTURE = 4;
	int selectedImages = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        
        this.imagesPath = new ArrayList<String>();        
        
        ((Button) findViewById(R.id.select_images))
                .setOnClickListener(new OnClickListener() {

                    public void onClick(View arg0) {
                    	
                        // in onCreate or any event where your want the user to
                        // select a file
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Picture"), SELECT_PICTURE);
                    }
         });
        
/*        ((Button) findViewById(R.id.ear_sx))
        .setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
            
            	Toast.makeText(getApplicationContext(), "Sx ear selected", Toast.LENGTH_SHORT).show();            	
            }
        });
        
        ((Button) findViewById(R.id.ear_dx))
        .setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
            	Toast.makeText(getApplicationContext(), "Sx ear selected", Toast.LENGTH_SHORT).show();            	
            }
        });
*/
        
        ((Button) findViewById(R.id.go_btn))
        .setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
            	Toast.makeText(getApplicationContext(), "Running...", Toast.LENGTH_SHORT).show();
            }
        });
    }
	
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.ear_dx:
                if (checked)
                    ear_code = 1;
                break;
            case R.id.ear_sx:
                if (checked)
                    ear_code = 0;
                break;
        }
        
        Toast.makeText(getApplicationContext(), ear_code, Toast.LENGTH_SHORT).show();
    }
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(MainActivity.this,
					SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		return false;
	}
	
	/**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in 
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
}
	
/*	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
            	if (Intent.ACTION_SEND_MULTIPLE.equals(true) && data.hasExtra(Intent.EXTRA_STREAM)) {
    		    // retrieve a collection of selected images
    		    ArrayList<Parcelable> list = data.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
    		    
    		    for (Parcelable parcel : list) {
    		       Uri uri = (Uri) parcel;
    		       Uri selectedImageUri = data.getData();
	               this.imagesPath.add(getPath(selectedImageUri));
    		   }
    		}
            }
        }
    }
*/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
            	
            	Uri selectedImageUri = data.getData();
            	
     	        //imagesPath.add(getPath(selectedImageUri));
     	        selectedImagePath = getPath(selectedImageUri);
     	        TextView myTextView = (TextView) findViewById(R.id.textView2);
     	        myTextView.setText(selectedImagePath);
                //Toast.makeText(getApplicationContext(), selectedImagePath, Toast.LENGTH_SHORT).show();
                
            }
        }
    }
	
	private void extractFeatures() {
		Config config = new Config(this);
		
	}
	

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			return rootView;
		}
	}
	
	/*	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		PlaceholderFragment fragment = new PlaceholderFragment();
		
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, fragment).commit();
		}
		
		((Button) fragment.getView().findViewById(R.id.select_images))
        .setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
		
		this.imagesPath = new ArrayList<String>();
	}
*/	

}
