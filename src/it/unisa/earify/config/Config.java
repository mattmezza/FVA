package it.unisa.earify.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Gestisce la configurazione
 * @author simone
 *
 */
public class Config {

	public static Config getInstance() {
		if (Config.INSTANCE == null) {
			Config.INSTANCE = new Config();
		}
		return Config.INSTANCE;
	}
	
	public boolean lbp() {
		return this.sp.getBoolean("extr_algo_use_lbp", true);
	}
	
	public boolean sift() {
		return this.sp.getBoolean("extr_algo_use_sift", true);
	}
	
	public int numberOfImagesToUse() {
		return Integer.parseInt(this.sp.getString("nof_images", "1"));
	}
	
	public static void setContext(Context context) {
		Config.CONTEXT = context;
	}
	
	private Config() {
		this.sp = PreferenceManager.getDefaultSharedPreferences(Config.CONTEXT);
	}
	

	private static Config INSTANCE;
	private static Context CONTEXT;
	private SharedPreferences sp;
}
