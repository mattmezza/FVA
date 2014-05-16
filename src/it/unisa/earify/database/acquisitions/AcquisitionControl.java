package it.unisa.earify.database.acquisitions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.database.EarifyDatabaseHelper;
import it.unisa.earify.database.TableAcquisitions;
import it.unisa.earify.database.TableFeatures;
import it.unisa.earify.database.TableUsers;
import it.unisa.earify.database.users.User;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Controlla la persistenza delle acquisizioni nel database.
 * @author simone
 */
public class AcquisitionControl {
	private static AcquisitionControl instance;
	
	/**
	 * Restituisce l'unica istanza del controller.
	 * @return
	 */
	public static AcquisitionControl getInstance() {
		if (instance == null)
			instance = new AcquisitionControl();
		
		return instance;
	}
	
	/**
	 * Registra un'acquisizione nel database
	 * @param pAcquisition Acquisizione da salvare
	 */
	public void registerAcquisition(Acquisition pAcquisition) {
		SQLiteDatabase db = EarifyDatabaseHelper.getInstance().getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(TableAcquisitions.COLUMN_ALGORITHM, pAcquisition.getAlgorithm());
		values.put(TableAcquisitions.COLUMN_USER, pAcquisition.getUser().getId());
		this.registerFeatures(pAcquisition, pAcquisition.getFeatures());
		
		db.insert(TableAcquisitions.TABLE_ACQUISITIONS, null, values);
		db.close();
	}
	
	/**
	 * Restituisce una lista di acquisizioni relative ad un particolare utente e 
	 * un particolare orecchio
	 * @param pUser Utente di cui si cercano le acquisizioni
	 * @param pEar Orecchio di cui si cercano le acquisizioni
	 * @return
	 */
	public List<Acquisition> getAcquisitions(User pUser, int pEar) {
		SQLiteDatabase db = EarifyDatabaseHelper.getInstance().getReadableDatabase();
		
		String query = "SELECT A." + TableAcquisitions.COLUMN_ALGORITHM + ", A." + TableAcquisitions.COLUMN_ID + " " +
					   "FROM " + TableAcquisitions.TABLE_ACQUISITIONS + " as A, " + TableUsers.TABLE_USERS + " as U " +
					   "WHERE " +
					   		"U." + TableUsers.COLUMN_ID + " = A." + TableAcquisitions.COLUMN_USER + " AND " +
					   		"U." + TableUsers.COLUMN_USERNAME + " = ? AND " +
					   		"A." + TableAcquisitions.COLUMN_EAR + " = ?";
		
		Cursor cursor = db.rawQuery(query, new String[] {pUser.getUsername(), String.valueOf(pEar)});
		
		
	    if (cursor == null)
	    	return null;
        cursor.moveToFirst();
	    
	    List<Acquisition> acquisitions = new ArrayList<Acquisition>();
	    
	    do {
		    Acquisition acquisition = new Acquisition();
		    acquisition.setId(cursor.getInt(1));
		    acquisition.setAlgorithm(cursor.getString(0));
		    acquisition.setUser(pUser);
		    acquisition.setFeatures(this.getFeatures(acquisition));
		    acquisitions.add(acquisition);
	    } while (cursor.moveToNext());
	    
	    return acquisitions;
	}
	
	/**
	 * Elimina un'acquisizione salvata precedentemente
	 * @param pAcquisition Acquisizione da eliminare
	 */
	public void deleteAcquisition(Acquisition pAcquisition) {
		SQLiteDatabase db = EarifyDatabaseHelper.getInstance().getWritableDatabase();
		
		db.beginTransaction();
		this.deleteFeatures(db, pAcquisition);
		
		db.delete(TableAcquisitions.TABLE_ACQUISITIONS,
				  TableAcquisitions.COLUMN_ID + " = ?",
				  new String[] {String.valueOf(pAcquisition.getId())});
		
		db.endTransaction();
		db.close();
	}
	
	/*
	 * Registra le feature specificate
	 */
	private void registerFeatures(Acquisition pAcquisition, List<IFeature> pFeatures) {
		SQLiteDatabase db = EarifyDatabaseHelper.getInstance().getWritableDatabase();
		
		for (IFeature feature : pFeatures) {
			ContentValues values = new ContentValues();
			values.put(TableFeatures.COLUMN_ACQUISITION, pAcquisition.getId());
			values.put(TableFeatures.COLUMN_DATA, getSerializated(feature));
			
			
			db.insert(TableFeatures.TABLE_FEATURES, null, values);
		}
		db.close();
	}
	
	/*
	 * Restituisce la lista di feature dell'acquisizione specificata
	 */
	private List<IFeature> getFeatures(Acquisition pAcquisition) {
		SQLiteDatabase db = EarifyDatabaseHelper.getInstance().getReadableDatabase();
		
		Cursor cursor = db.query(TableFeatures.TABLE_FEATURES, 
								new String[] {TableFeatures.COLUMN_ID, TableFeatures.COLUMN_DATA},  
								TableFeatures.COLUMN_ACQUISITION + "= ?", 
								new String[] {String.valueOf(pAcquisition.getId())}, 
								null, null, null);
		
		if (cursor == null)
			return null;
		cursor.moveToFirst();
		
		List<IFeature> features = new ArrayList<IFeature>();
		do {
			try {
				Object deserialized = this.getDeserialized(cursor.getBlob(1));
				if (deserialized != null && deserialized instanceof IFeature) {
					IFeature feature = (IFeature)deserialized;
					features.add(feature);
				} else
					throw new RuntimeException("Database error. Inconsistency: a saved feature is not an IFeature");
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Database error");
			}
	    } while (cursor.moveToNext());
		
		return features;
 	}
	
	/*
	 * Elimina le feature dell'acquisizione specificata.
	 */
	private void deleteFeatures(SQLiteDatabase pDatabase, Acquisition pAcquisition) {		
		pDatabase.delete(TableFeatures.TABLE_FEATURES, 
				  TableFeatures.COLUMN_ACQUISITION + " = ?",
				  new String[] {String.valueOf(pAcquisition.getId())});
	}
	
	/*
	 * Restituisce la deserializzazione dei dati passati.
	 */
	private Object getDeserialized(byte[] pData) throws ClassNotFoundException {
		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(pData));
			Object o  = ois.readObject();
			ois.close();
			return o;
		} catch (IOException e) {
			return null;
		}
	}
	
	/*
	 * Restituisce una serializzazione della classe passata
	 */
	private byte[] getSerializated(Serializable pObject) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ObjectOutputStream oos = new ObjectOutputStream(baos);
	        oos.writeObject(pObject);
	        oos.close();
	        
	        return baos.toByteArray();
		} catch (IOException e) {
			return new byte[] {};
		}
	}
	
	private AcquisitionControl() {}
}
