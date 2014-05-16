package it.unisa.earify.ui;

import it.unisa.earify.FeatureExtractorAbstraction;
import it.unisa.earify.R;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.config.Config;
import it.unisa.earify.database.EarifyDatabaseHelper;
import it.unisa.earify.exceptions.InvalidActionException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
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
	private List<Bitmap> im2extr = new ArrayList<Bitmap>();

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
        
        Config.setContext(this);
        EarifyDatabaseHelper.init(this);
        
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
            	//Toast.makeText(getApplicationContext(), "Running...", Toast.LENGTH_SHORT).show();
            	extractFeatures();
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
            	
            	try {
					Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
					im2extr.add(bmp);
				} catch (FileNotFoundException e) {
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
				}
            	
     	        //imagesPath.add(getPath(selectedImageUri));
     	        selectedImagePath = getPath(selectedImageUri);
     	        TextView myTextView = (TextView) findViewById(R.id.textView2);
     	        myTextView.setText(selectedImagePath);
                //Toast.makeText(getApplicationContext(), selectedImagePath, Toast.LENGTH_SHORT).show();
                
            }
        }
    }
	
	private void extractFeatures() {
		FeatureExtractorAbstraction fea = new FeatureExtractorAbstraction();
		Map<String,List<List<IFeature>>> result = null;
		try {
			result = fea.extractFeatures(FeatureExtractorAbstraction.REGISTRATION, this.im2extr, "Pippo", 0, 1);
			Toast.makeText(getApplicationContext(), "finito", Toast.LENGTH_SHORT).show();
			Log.d("MainActivity", result.toString());
			
		} catch (InvalidActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeException e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
			Log.d("MainActivity", e.toString());
		}
		
		
		
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
