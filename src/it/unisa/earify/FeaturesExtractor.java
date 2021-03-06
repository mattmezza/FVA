package it.unisa.earify;

import it.unisa.earify.algorithms.ExtractionAlgorithm;
import it.unisa.earify.algorithms.FeatureExtraction;
import it.unisa.earify.algorithms.IFeature;
import it.unisa.earify.algorithms.Image;
import it.unisa.earify.database.acquisitions.Acquisition;
import it.unisa.earify.database.acquisitions.AcquisitionControl;
import it.unisa.earify.database.exceptions.AlreadyRegisteredUserException;
import it.unisa.earify.database.users.User;
import it.unisa.earify.database.users.UsersControl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.opencv.android.OpenCVLoader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
/**
 * Componente specifica in grado di estrarre le caratteristiche di una lista di immagini.
 * @author simone
 *
 */
public class FeaturesExtractor implements IFeatureExtractor {
	// private static final String DATABASE_PATH = "data.db";

	/**
	 * Registra l'utente specificato nel database, utilizzando la lista di immagini passate relative all'orecchio
	 * specificato.
	 * 
	 * @param pImages Lista delle immagini
	 * @param pUsername Nome dell'utente da registrare
	 * @param pEar Identificativo delle orecchie da acquisire. Usare {@link FeatureExtractorAbstraction#EAR_LEFT EAR_LEFT} e 
	 * {@link FeatureExtractorAbstraction#EAR_RIGHT EAR_RIGHT}
	 */
	@Override
	public void register(List<Image> pImages, String pUsername, int pEar,
			float pQuality) {

		try {
			UsersControl.getInstance().registerUser(pUsername);
		} catch (AlreadyRegisteredUserException e) {
			// user is already registered... skipping registration
		}

		Map<String, List<List<IFeature>>> features = this.extractFeature(
				pImages, pUsername, pEar, pQuality);
		for (Map.Entry<String, List<List<IFeature>>> entry : features
				.entrySet()) {

			for (List<IFeature> imFeatures : entry.getValue()) {
				Acquisition acquisition = new Acquisition();
				acquisition.setAlgorithm(entry.getKey());
				acquisition.setEar(pEar);
				acquisition.setFeatures(imFeatures);
				acquisition.setUser(UsersControl.getInstance().getUser(
						pUsername));
				AcquisitionControl.getInstance().registerAcquisition(
						acquisition);
			}

		}
	}

	/**
	 * Vedi il metodo {@link FeatureExtractor#extractFeatures(int, List, String, int, float) extractFeature}
	 */
	@Override
	public Map<String, List<List<IFeature>>> extractFeature(
			List<Image> pImages, String pUsername, int pEar, float pQuality) {
		FeatureExtraction context = new FeatureExtraction();
		Set<ExtractionAlgorithm> algo = context
				.getFeatureExtractionAlgorithms();
		Map<String, List<List<IFeature>>> result = new HashMap<String, List<List<IFeature>>>();

		for (ExtractionAlgorithm extractionAlgorithm : algo) {
			List<List<IFeature>> imFeatures = new ArrayList<List<IFeature>>();

			for (Image image : pImages) {
				List<IFeature> features = extractionAlgorithm.calculate(image);
				imFeatures.add(features);
			}

			result.put(extractionAlgorithm.getName(), imFeatures);
		}

		return result;
	}
}
