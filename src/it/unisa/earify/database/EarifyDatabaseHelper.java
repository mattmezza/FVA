package it.unisa.earify.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe che gestisce il database, compresa la creazione delle tabelle e l'upgrade
 * eventuale in caso di aggiornamento.
 * @author simone
 *
 */
public class EarifyDatabaseHelper extends SQLiteOpenHelper {
	/**
	 * Versione del database
	 */
	public static final int DATABASE_VERSION = 1;
	
	/**
	 * Nome del file di database 
	 */
	public static final String DATABASE_NAME = "earify";
	
	private static EarifyDatabaseHelper db;
	
	/**
	 * Inizializza la classe, creando eventualmente il file, dato il contesto 
	 * dell'applicazione
	 * @param pContext Contesto dell'applicazione
	 */
	public static void init(Context pContext) {
		getInstance(pContext);
		return;
	}
	
	/**
	 * Inizializza la classe e ne restituisce un'istanza per permettere di
	 * accedere ai dati.
	 * @param pContext Contesto dell'applicazione
	 * @return
	 */
	public static EarifyDatabaseHelper getInstance(Context pContext) {
		db = new EarifyDatabaseHelper(pContext);
		return db;
	}
	
	/**
	 * Restituisce un'istanza del database manager, che permette di accedere
	 * ai dati.
	 * @return L'unica istanza di questa classe
	 */
	public static EarifyDatabaseHelper getInstance() {
		return db;
	}
 
	/**
	 * Metodo invocato automaticamente se il database non esiste.
	 * NON USARE
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		TableUsers.onCreate(db);
		TableAcquisitions.onCreate(db);
		TableFeatures.onCreate(db);
	}
 
	/**
	 * Metodo invocato automaticamente se la versione del database &egrave; aggiornata. 
	 * NON USARE
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	private EarifyDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
}