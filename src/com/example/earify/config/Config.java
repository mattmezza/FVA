package com.example.earify.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Config {

	public Config(Context context) {
		this.context = context;
		this.sp = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public boolean lbp() {
		return this.sp.getBoolean("extr_algo_use_lbp", false);
	}
	
	public boolean sift() {
		return this.sp.getBoolean("extr_algo_use_sift", false);
	}
	
	public int numberOfImagesToUse() {
		return Integer.parseInt(this.sp.getString("nof_images", "1"));
	}
	
	private Context context;
	private SharedPreferences sp;
}
