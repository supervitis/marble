package org.marble.commons.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.marble.commons.dao.DatasetDao;
import org.marble.commons.dao.model.Dataset;
import org.marble.commons.exception.InvalidDatasetException;
import org.marble.commons.util.MarbleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

@Service
public class DatasetServiceImpl implements DatasetService {

	@Autowired
	DatasetDao datasetDao;

	@Override
	public Dataset updateDataset(Dataset dataset, MultipartFile mfile)
			throws InvalidDatasetException, IllegalStateException, IOException {
		dataset = datasetDao.save(dataset);
		if (dataset == null) {
			throw new InvalidDatasetException();
		}
		File file = MarbleUtil.multipartToFile(mfile);
		try {
		    MongoClient mongoClient = new MongoClient("polux.det.uvigo.es",
		      27117);

		    // Now connect to your databases
		    DB db = mongoClient.getDB("datasets");
		    // System.out.println("Connect to database successfully");
		    String oldName = null;
		    try {
		     oldName = getDataset(dataset.getId()).getName();
		    } catch (Exception e) {
		     // TODO Auto-generated catch block
		    }
		    String newName = dataset.getName();
		    if(oldName == null){
		     DBCollection collection = db.getCollection(newName);
		     // Always wrap FileReader in BufferedReader.
		     BufferedReader bufferedReader = new BufferedReader(
		       new FileReader(file));

		     String line = null;
		     while ((line = bufferedReader.readLine()) != null) {
		      DBObject dbObject = (DBObject) JSON.parse(line);
		      collection.insert(dbObject);
		     }

		     bufferedReader.close();

		    }else {
		     DBCollection collection = db.getCollection(oldName);
		     collection.rename(newName);
		     // Always wrap FileReader in BufferedReader.
		     BufferedReader bufferedReader = new BufferedReader(
		       new FileReader(file));

		     String line = null;
		     while ((line = bufferedReader.readLine()) != null) {
		      DBObject dbObject = (DBObject) JSON.parse(line);
		      collection.insert(dbObject);
		     }

		     bufferedReader.close();
		    }

		   

		    // Always close files.
		   } catch (FileNotFoundException ex) {}
		return dataset;
	}

	@Override
	public Dataset getDataset(Integer id) throws InvalidDatasetException {
		Dataset dataset = datasetDao.findOne(id);
		if (dataset == null) {
			throw new InvalidDatasetException();
		}
		return dataset;
	}

	@Override
	public List<Dataset> getDatasets() {
		List<Dataset> datasets = datasetDao.findAll();
		return datasets;
	}

	@Override
	public void deleteDataset(Integer id) {
		datasetDao.delete(id);
		return;
	}

	@Override
	public Dataset createDataset(Dataset dataset, MultipartFile mfile)
			throws InvalidDatasetException, IllegalStateException, IOException {
		dataset = datasetDao.save(dataset);
		if (dataset == null) {
			throw new InvalidDatasetException();
		} else {
			File file = MarbleUtil.multipartToFile(mfile);

			// Always wrap FileWriter in BufferedWriter.

			try {
				MongoClient mongoClient = new MongoClient("polux.det.uvigo.es",
						27117);

				// Now connect to your databases
				DB db = mongoClient.getDB("datasets");
				// System.out.println("Connect to database successfully");

				DBCollection collection = db.getCollection(dataset.getName());
				// System.out.println("Collection test created successfully");

				// Always wrap FileReader in BufferedReader.
				BufferedReader bufferedReader = new BufferedReader(
						new FileReader(file));

				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					DBObject dbObject = (DBObject) JSON.parse(line);
					collection.insert(dbObject);
				}

				bufferedReader.close();

				// Always close files.
			} catch (FileNotFoundException ex) {

			} catch (IOException ex) {

				// Or we could just do this:
				// ex.printStackTrace();
			}
			// Guardar el fichero
			// Código para correr el script? Debe de ser aquí supongo
		}
		return dataset;
	}

}
