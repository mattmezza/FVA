package it.unisa.earify.ui;

import it.unisa.earify.ExtractorDelegate;
import it.unisa.earify.FeatureExtractorAbstraction;
import it.unisa.earify.FeatureExtractorTask;
import it.unisa.earify.R;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.Image;
import it.unisa.earify.config.Config;
import it.unisa.earify.database.EarifyDatabaseHelper;
import it.unisa.earify.database.acquisitions.Acquisition;
import it.unisa.earify.database.acquisitions.AcquisitionControl;
import it.unisa.earify.database.users.UsersControl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

public class MainActivity extends Activity implements ExtractorDelegate {
	private String selectedImagePath;
	private String username;
	private int actionCodeId;
	private int earCodeId;
	private int quality;
	private ProgressDialog progressDialog;
	// private static final int QUALITY = 1;
	private static final int SELECT_PICTURE = 4;

	private List<Image> im2extr = new ArrayList<Image>();

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

		Config.setContext(this);

		((Button) findViewById(R.id.select_images))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {

						// in onCreate or any event where your want the user to
						// select a file
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						startActivityForResult(
								Intent.createChooser(intent, "Select Picture"),
								SELECT_PICTURE);
					}
				});

		EarifyDatabaseHelper.init(this);

		((Button) findViewById(R.id.go_btn))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {

						// otteniamo il codice qualita, che per per default
						EditText editQuality = (EditText) findViewById(R.id.fake_quality);
						quality = Integer.parseInt(editQuality.getText()
								.toString());

						// otteniamo il nome dell'utente.
						EditText editUsr = (EditText) findViewById(R.id.username_tv);
						username = editUsr.getText().toString();

						// otteniamo l'id del radio button, dx corrisponde al
						// valore intero 0, sx corrisponde al valore intero 1.
						actionCodeId = ((RadioGroup) findViewById(R.id.action_group))
								.getCheckedRadioButtonId();

						// otteniamo l'id del radio button, dx corrisponde al
						// valore intero 0, sx corrisponde al valore intero 1.
						earCodeId = ((RadioGroup) findViewById(R.id.radio_earcode))
								.getCheckedRadioButtonId();

						// extrazione delle feature
						extractFeatures();

						// Toast.makeText(getApplicationContext(), "Running...",
						// Toast.LENGTH_SHORT).show();
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

	public String getPath(Uri uri) {
		if (uri == null) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE) {

				Uri selectedImageUri = data.getData();

				try {
					Bitmap bmp = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									selectedImageUri));
					Image image = new Image();
					image.setBitmap(bmp);
					image.setPath(selectedImagePath.toString());
					
					im2extr.add(image);
				} catch (FileNotFoundException e) {
					Toast.makeText(getApplicationContext(), e.toString(),
							Toast.LENGTH_SHORT).show();
				}

				Config config = Config.getInstance();
				int nofImages = config.numberOfImagesToUse();
				TextView nofTextView = (TextView) findViewById(R.id.nofSelectedImages);
				nofTextView.setText("Immagini selezionate: " + im2extr.size()
						+ "/" + nofImages);

				if (im2extr.size() >= nofImages) {
					Button button = ((Button) findViewById(R.id.select_images));
					button.setEnabled(false);
				}
				selectedImagePath = getPath(selectedImageUri);
				TextView myTextView = (TextView) findViewById(R.id.textView2);
				myTextView.append("\n"+selectedImagePath);
			}
		}
	}

	private int getAction(int pActionCode) {
		if (pActionCode == R.id.register_radio) {
			return FeatureExtractorAbstraction.REGISTRATION;
		} else if (pActionCode == R.id.verify_radio) {
			return FeatureExtractorAbstraction.VERIFICATION;
		} else if (pActionCode == R.id.recognition_radio) {
			return FeatureExtractorAbstraction.RECOGNITION;
		} else
			return -1;
	}

	private int getEar(int pEarCode) {
		if (pEarCode == R.id.ear_sx)
			return FeatureExtractorAbstraction.EAR_LEFT;
		else if (pEarCode == R.id.ear_dx)
			return FeatureExtractorAbstraction.EAR_RIGHT;
		else
			return -1;
	}

	private void extractFeatures() {
		progressDialog = ProgressDialog.show(this, "Processing...",
				"Please wait while your images are being processed...", true);
		progressDialog.setCancelable(false);
		progressDialog.show();
		FeatureExtractorTask task = new FeatureExtractorTask(
				getAction(actionCodeId), this.im2extr, this.username,
				getEar(this.earCodeId), this.quality);
		task.setDelegate(this);
		task.execute("");
	}

	private void read() {
		try {
			List<Acquisition> as = AcquisitionControl.getInstance()
					.getAcquisitions(UsersControl.getInstance().getUser("mm"),
							getEar(this.earCodeId));
			for (Acquisition a : as) {
				Log.d("Feature", a.toString());
			}
		} catch (RuntimeException e) {
			Log.d("ERROR", e.toString());
		}
	}

	@Override
	public void onExtractorFinished(Map<String, List<List<IFeature>>> result) {
		Toast.makeText(getApplicationContext(), "finito", Toast.LENGTH_LONG)
				.show();
		if (result != null)
			Log.d("MainActivity", result.toString());
		progressDialog.cancel();
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Wowowow");
		alertDialogBuilder
				.setMessage(
						"The features for selected images have been extracted successfully!").setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		AlertDialog ad = alertDialogBuilder.create();
		ad.show();
	}

	@Override
	public void onExtractorError(Exception e) {
		Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG)
				.show();
		Log.d("MainActivity", e.toString());
		progressDialog.cancel();
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Oops");
		alertDialogBuilder
				.setMessage(
						"Errore durante l'estrazione delle caratteristiche!\n"
								+ e.toString()).setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		AlertDialog ad = alertDialogBuilder.create();
		ad.show();
	}

}
