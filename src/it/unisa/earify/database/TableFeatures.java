package it.unisa.earify.database;

import android.database.sqlite.SQLiteDatabase;

public class TableFeatures {
	/**
	 * Nome della tabella
	 */
	public static final String TABLE_FEATURES = "features";
	
	/**
	 * Nome della colonna ID
	 */
	public static final String COLUMN_ID = "_id";
	
	/**
	 * Nome della colonna DATI
	 */
	public static final String COLUMN_DATA = "data";
	
	/**
	 * Nome della colonna relativa al campo che punta all'acquisizione
	 */
	public static final String COLUMN_ACQUISITION = "acquisitionid";
	
	/**
	 * Metodo richiamato dal manager del database alla creazione delle tabelle.
	 * NON RICHIAMARE ALTROVE
	 * @param db Istanza del database
	 */
	public static void onCreate(SQLiteDatabase db) {
		String CREATE_FEATURES_TABLE
				= "CREATE TABLE " + TABLE_FEATURES + " ("
				+ 	COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ 	COLUMN_DATA + " BLOB,"
				+ 	COLUMN_ACQUISITION + " INTEGER,"
				+ 	"FOREIGN KEY (" + COLUMN_ACQUISITION + ") REFERENCES users(" + TableAcquisitions.COLUMN_ID + ")"
				+ ")";
		
		db.execSQL(CREATE_FEATURES_TABLE);
	}
 
	/**
	 * Metodo richiamato dal manager del database all'aggiornamento del database.
	 * NON RICHIAMARE ALTROVE
	 * @param db Istanza del database
	 * @param oldVersion Vecchia versione
	 * @param newVersion Nuova versione
	 */
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
