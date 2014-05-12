package it.unisa.earify.database;

import android.database.sqlite.SQLiteDatabase;

public class TableUsers {
	/**
	 * Nome della tabella
	 */
	public static final String TABLE_USERS = "users";
	
	/**
	 * Nome della colonna ID
	 */
	public static final String COLUMN_ID = "_id";
	
	/**
	 * Nome della colonna USERNAME
	 */
	public static final String COLUMN_USERNAME = "username";
	
	/**
	 * Metodo richiamato dal manager del database alla creazione delle tabelle.
	 * NON RICHIAMARE ALTROVE
	 * @param db Istanza del database
	 */
	public static void onCreate(SQLiteDatabase db) {
		String CREATE_USERS_TABLE
				= "CREATE TABLE " + TABLE_USERS + " ("
				+ 	COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ 	COLUMN_USERNAME + " TEXT"
				+ ")";
		
		db.execSQL(CREATE_USERS_TABLE);
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
