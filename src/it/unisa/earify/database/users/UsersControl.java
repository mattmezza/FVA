package it.unisa.earify.database.users;

import java.util.ArrayList;
import java.util.List;

import it.unisa.earify.database.EarifyDatabaseHelper;
import it.unisa.earify.database.TableUsers;
import it.unisa.earify.database.acquisitions.Acquisition;
import it.unisa.earify.database.acquisitions.AcquisitionControl;
import it.unisa.earify.database.exceptions.AlreadyRegisteredUserException;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Controlla la persistenza degli utenti nel database
 * @author simone
 */
public class UsersControl {
	private static UsersControl instance;
	
	/**
	 * Restituisce l'unica istanza del controller.
	 * @return
	 */
	public static UsersControl getInstance() {
		if (instance == null)
			instance = new UsersControl();
		
		return instance;
	}
	
	/**
	 * Registra un utente nel sistema
	 * @param pUsername Nome utente
	 * @throws AlreadyRegisteredUserException Se l'utente &egrave; gi&agrave; presente nel database
	 */
	public void registerUser(String pUsername) throws AlreadyRegisteredUserException {
		if (this.getUser(pUsername) != null)
			throw new AlreadyRegisteredUserException("User already registered");
		SQLiteDatabase db = EarifyDatabaseHelper.getInstance().getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(TableUsers.COLUMN_USERNAME, pUsername);
		db.insert(TableUsers.TABLE_USERS, null, values);
		
		db.close();
	}
	
	/**
	 * Restituisce l'utente con l'username specificato
	 * @param pUsername Nome utente
	 * @return
	 */
	public User getUser(String pUsername) {
		SQLiteDatabase db = EarifyDatabaseHelper.getInstance().getReadableDatabase();
		
		Cursor cursor = db.query(TableUsers.TABLE_USERS,
				 new String[] {TableUsers.COLUMN_ID, TableUsers.COLUMN_USERNAME},
				 TableUsers.COLUMN_USERNAME + " = ?", 
				 new String[] {pUsername},
				 null, null, null);
		
		if (cursor == null)
			return null;
		if (!cursor.moveToFirst())
			return null;
		
		User user = new User();
		user.setId(cursor.getInt(0));
		user.setUsername(cursor.getString(1));
		
		db.close();
		
		return user;
	}
	
	/**
	 * Restituisce l'utente con l'identificativo specificato
	 * @param pId ID dell'utente
	 * @return
	 */
	public User getUser(int pId) {
		SQLiteDatabase db = EarifyDatabaseHelper.getInstance().getReadableDatabase();
		
		Cursor cursor = db.query(TableUsers.TABLE_USERS,
				 new String[] {TableUsers.COLUMN_ID, TableUsers.COLUMN_USERNAME},
				 TableUsers.COLUMN_ID + " = ?", 
				 new String[] {String.valueOf(pId)},
				 null, null, null);
		
		if (cursor == null)
			return null;
		if (!cursor.moveToFirst())
			return null;
		
		User user = new User();
		user.setId(cursor.getInt(0));
		user.setUsername(cursor.getString(1));
		
		db.close();
		
		return user;
	}
	
	/**
	 * Elimina l'utente specificato dal database
	 * @param pUsername Nome dell'utente da eliminare
	 */
	public void deleteUser(String pUsername) {
		AcquisitionControl acquisitionControl = AcquisitionControl.getInstance();
		List<Acquisition> acquisitions = new ArrayList<Acquisition>();
		
		User user = this.getUser(pUsername);
		
		acquisitions.addAll(acquisitionControl.getAcquisitions(user, Acquisition.EAR_LEFT));
		acquisitions.addAll(acquisitionControl.getAcquisitions(user, Acquisition.EAR_RIGHT));
		
		for (Acquisition acquisition : acquisitions) 
			acquisitionControl.deleteAcquisition(acquisition);
		
		SQLiteDatabase db = EarifyDatabaseHelper.getInstance().getWritableDatabase();
		db.delete(TableUsers.TABLE_USERS, 
				  TableUsers.COLUMN_ID + " = ?", 
				  new String[] {String.valueOf(user.getId())});
	}
	
	private UsersControl() {};
}
