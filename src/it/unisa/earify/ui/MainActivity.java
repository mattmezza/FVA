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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private String selectedImagePath;
	private String pUsername;
	private int pEarCode;
	private int pQuality;
	//private static final int QUALITY = 1;
	private static final int SELECT_PICTURE = 4;
	
	private List<Bitmap> im2extr = new ArrayList<Bitmap>();
	
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);
        
        
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
        
      
        ((Button) findViewById(R.id.go_btn))
        .setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {

            	//otteniamo il codice qualit�, che per per default � 1.
            	EditText editQuality = (EditText) findViewById(R.id.fake_quality);
                pQuality = Integer.parseInt(editQuality.getText().toString());

            	//otteniamo il nome dell'utente.
            	EditText editUsr = (EditText) findViewById(R.id.username_tv);
                pUsername = editUsr.getText().toString();
                
                //otteniamo l'id del radio button, dx corrisponde al valore intero 0, sx corrisponde al valore intero 1.
            	pEarCode = ((RadioGroup)findViewById( R.id.radio_earcode )).getCheckedRadioButtonId();
                
            	//extrazione delle feature
            	extractFeatures();
            	
            	//Toast.makeText(getApplicationContext(), "Running...", Toast.LENGTH_SHORT).show();
            }
        });
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
        @SuppressWarnings("deprecation")
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
			result = fea.extractFeatures(FeatureExtractorAbstraction.REGISTRATION, this.im2extr, this.pUsername, this.pEarCode, this.pQuality);
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
	
}
