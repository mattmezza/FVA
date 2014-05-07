package it.unisa.earify.ui;

import it.unisa.earify.config.Config;

import it.unisa.earify.R;
import it.unisa.earify.R.id;
import it.unisa.earify.R.layout;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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
			showSettings(rootView);
			return rootView;
		}
		
		private void showSettings(View view) {
			Config config = new Config(getActivity());
			StringBuilder builder = new StringBuilder();
			builder.append("\n num of img: "
					+ config.numberOfImagesToUse());

			builder.append("\n use sift: "
					+ config.sift());

			builder.append("\n use lbp: "
					+ config.lbp());
			TextView settingsTextView = (TextView) view.findViewById(R.id.settings_tv);
			settingsTextView.setText(builder.toString());
		}
	}

}
