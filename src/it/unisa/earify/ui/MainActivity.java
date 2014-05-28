package it.unisa.earify.ui;

import it.unisa.earify.ExtractorDelegate;
import it.unisa.earify.FeatureExtractorAbstraction;
import it.unisa.earify.FeatureExtractorTask;
import it.unisa.earify.R;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.Image;
import it.unisa.earify.algorithms.lbp.LibraryLoader;
import it.unisa.earify.config.Config;
import it.unisa.earify.database.EarifyDatabaseHelper;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.opencv.android.OpenCVLoader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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

	private Button addImgBtn;
	private Button goBtn;
	private EditText qualityEditText;
	private EditText usernameEditText;
	private RadioGroup actionRadioGroup;
	private RadioGroup earRadioGroup;

	private static final int SELECT_PICTURE = 4;

	private List<Image> im2extr;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		
		this.im2extr = new ArrayList<Image>();
		if (savedInstanceState != null) {
			Serializable images = savedInstanceState.getSerializable("images");
			if (images != null && images instanceof List<?>) {
				this.im2extr = (List<Image>)images;
			} else
				savedInstanceState.putSerializable("images", (Serializable)im2extr);
		}

		Config.setContext(this);
		EarifyDatabaseHelper.init(this);

		if (!OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this,
				new LibraryLoader(this, "Earify"))) {
			Log.e("err", "Cannot connect to OpenCV Manager");
		}

		this.qualityEditText = (EditText) findViewById(R.id.fake_quality);
		this.usernameEditText = (EditText) findViewById(R.id.username_tv);
		this.earRadioGroup = (RadioGroup) findViewById(R.id.radio_earcode);
		this.actionRadioGroup = (RadioGroup) findViewById(R.id.action_group);

		this.addImgBtn = (Button) findViewById(R.id.select_images);
		this.addImgBtn.setOnClickListener(new OnClickListener() {
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

		this.goBtn = (Button) findViewById(R.id.go_btn);
		this.goBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// otteniamo il codice qualita, che per per default
				quality = Integer
						.parseInt(qualityEditText.getText().toString());
				// otteniamo il nome dell'utente.
				username = usernameEditText.getText().toString();
				// otteniamo l'id del radio button, dx corrisponde al
				// valore intero 0, sx corrisponde al valore intero 1.
				actionCodeId = actionRadioGroup.getCheckedRadioButtonId();
				// otteniamo l'id del radio button, dx corrisponde al
				// valore intero 0, sx corrisponde al valore intero 1.
				earCodeId = earRadioGroup.getCheckedRadioButtonId();
				// extrazione delle feature
				extractFeatures();
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
				selectedImagePath = getPath(selectedImageUri);

				try {
					Bitmap bmp = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									selectedImageUri));
					Image image = new Image();
					image.setBitmap(bmp);
					image.setPath(selectedImagePath.toString());
					im2extr.add(image);
					Log.d("Debug", "Aggiunta immagine " + image.getPath() + "; totale: "+ im2extr.size());
				} catch (FileNotFoundException e) {
					Log.d("Error", e.getMessage());
					Toast.makeText(getApplicationContext(), e.toString(),
							Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					Log.d("Error", e.getMessage());
					Toast.makeText(getApplicationContext(), e.toString(),
							Toast.LENGTH_LONG).show();
				}

				int nofImages = Config.getInstance().numberOfImagesToUse();
				TextView nofTextView = (TextView) findViewById(R.id.nofSelectedImages);
				nofTextView.setText("Immagini selezionate: " + im2extr.size()
						+ "/" + nofImages);

				if (im2extr.size() >= nofImages) {
					Button button = ((Button) findViewById(R.id.select_images));
					button.setEnabled(false);
				}

				TextView myTextView = (TextView) findViewById(R.id.textView2);
				myTextView.append("\n" + selectedImagePath);
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

	// private void read() {
	// try {
	// List<Acquisition> as = AcquisitionControl.getInstance()
	// .getAcquisitions(UsersControl.getInstance().getUser("mm"),
	// getEar(this.earCodeId));
	// for (Acquisition a : as) {
	// Log.d("Feature", a.toString());
	// }
	// } catch (RuntimeException e) {
	// Log.d("ERROR", e.toString());
	// }
	// }
	//
	// private void testLBP() {
	// String path = "/storage/extSdCard/DCIM/Camera/20130521_173230.jpg";
	// try {
	// int[] res = new LBPNativeLibrary().extractFeatures(path, 5, 5);
	//
	// for (int i = 0; i < 25; i++) {
	// int currentSum = 0;
	// for (int j = 0; j < 256; j++) {
	// int index = i * 256 + j;
	// currentSum += res[index];
	// }
	//
	// Log.d("Total Value area " + i, String.valueOf(currentSum));
	// }
	// Log.d("OOOOk", "Yeah");
	// } catch (Exception e) {
	// Log.d("ERROR", e.toString());
	// }
	// Log.d("Ok", "Alright!");
	// }

	@Override
	public void onExtractorFinished(Map<String, List<List<IFeature>>> result) {
		if (result != null)
			Log.d("MainActivity", result.toString());
		progressDialog.cancel();
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Wowowow");
		alertDialogBuilder
				.setMessage(
						"The features for selected images have been extracted successfully!")
				.setCancelable(false)
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
