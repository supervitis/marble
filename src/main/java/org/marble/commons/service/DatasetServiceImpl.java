package org.marble.commons.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.UnknownHostException;
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

		try {

			MongoClient mongoClient = new MongoClient("polux.det.uvigo.es",
					27117);

			// Now connect to your databases
			DB db = mongoClient.getDB("datasets");
			String oldName = null;
			try {
				oldName = getDataset(dataset.getId()).getName();
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
			String newName = dataset.getName();
			DBCollection collection = db.getCollection(oldName);
			DBCollection newCollection = null;
			if (!oldName.equals(newName)) {
				collection = db.getCollection(oldName);
				newCollection = collection.rename(newName, true);
			} else {
				newCollection = collection;
			}
			// Always wrap FileReader in BufferedReader.
			File file = MarbleUtil.multipartToFile(mfile);
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					file));

			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				DBObject dbObject = (DBObject) JSON.parse(line);
				try {
					newCollection.insert(dbObject);
				} catch (Exception e) {

				}
			}

			bufferedReader.close();
		} catch (Exception ex) {

		}
		dataset = datasetDao.save(dataset);
		if (dataset == null) {
			throw new InvalidDatasetException();
		}
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
		try {
			MongoClient mongoClient = new MongoClient("polux.det.uvigo.es",
					27117);
			DB db = mongoClient.getDB("datasets");
			String oldName = null;
			try {
				oldName = getDataset(id).getName();
			} catch (InvalidDatasetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DBCollection collection = db.getCollection(oldName);
			collection.drop();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Now connect to your databases

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
			try {
				//TODO: Poner los parametros en pom.properties
				File file = MarbleUtil.multipartToFile(mfile);
				MongoClient mongoClient = new MongoClient("polux.det.uvigo.es",
						27117); 
				DB db = mongoClient.getDB("datasets");

				DBCollection collection = db.getCollection(dataset.getName());
				BufferedReader bufferedReader = new BufferedReader(
						new FileReader(file));

				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					DBObject dbObject = (DBObject) JSON.parse(line);
					collection.insert(dbObject);
				}

				bufferedReader.close();
			} catch (Exception ex) {

			}
		}
		return dataset;
	}

}
