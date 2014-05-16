package it.unisa.earify.database;

import android.database.sqlite.SQLiteDatabase;

/**
 * Gestisce la tabella delle acquisizioni
 * @author simone
 */
public class TableAcquisitions {
	/**
	 * Nome della tabella
	 */
	public static final String TABLE_ACQUISITIONS = "acquisitions";
	
	/**
	 * Nome della colonna ID
	 */
	public static final String COLUMN_ID = "_id";
	
	/**
	 * Nome della colonna ALGORITMO
	 */
	public static final String COLUMN_ALGORITHM = "algorithm";
	
	/**
	 * Nome della colonna ORECCHIO
	 */
	public static final String COLUMN_EAR = "ear";
	
	/**
	 * Nome della colonna relativa al campo che punta all'utente
	 */
	public static final String COLUMN_USER = "userid";
	
	/**
	 * Metodo richiamato dal manager del database alla creazione delle tabelle.
	 * NON RICHIAMARE ALTROVE
	 * @param db Istanza del database
	 */
	public static void onCreate(SQLiteDatabase db) {
		String CREATE_ACQUISITIONS_TABLE
				= "CREATE TABLE " + TABLE_ACQUISITIONS + " ("
				+ 	COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ 	COLUMN_ALGORITHM + " TEXT,"
				+ 	COLUMN_USER + " INTEGER,"
				+   COLUMN_EAR + " INTEGER,"
				+ 	"FOREIGN KEY (" + COLUMN_USER + ") REFERENCES users(" + TableUsers.COLUMN_ID + ")"
				+ ")";
		
		db.execSQL(CREATE_ACQUISITIONS_TABLE);
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
