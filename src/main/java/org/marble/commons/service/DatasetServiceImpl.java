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
import org.marble.commons.dao.model.UploadedStatus;
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
	
	@Autowired
    DatastoreService datastoreService;


	@Override
	public Dataset updateDataset(Dataset dataset, MultipartFile mfile)
			throws InvalidDatasetException, IllegalStateException, IOException {
		dataset = datasetDao.save(dataset);
		if (dataset == null) {
			throw new InvalidDatasetException();
		}
		
		File file = MarbleUtil.multipartToFile(mfile);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(
				file));

		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			Object status =  JSON.parse(line);
			try {
				datastoreService.save(new UploadedStatus(dataset.getId(), status));
			} catch (Exception e) {
			}
		}

		bufferedReader.close();
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
	public Dataset getDatasetByName(String name) throws InvalidDatasetException {
		return datasetDao.findDatasetByName(name);

	}

	@Override
	public List<Dataset> getDatasets() {
		List<Dataset> datasets = datasetDao.findAll();
		return datasets;
	}

	@Override
	public void deleteDataset(Integer id) {
		datastoreService.findAllAndRemoveByDatasetId(id, UploadedStatus.class);
		datasetDao.delete(id);
		return;
	}

	@Override
	public List<UploadedStatus> downloadDataset(Integer datasetId){
		return datastoreService.findAll(UploadedStatus.class);
	}
	@Override
	public Dataset createDataset(Dataset dataset, MultipartFile mfile)
			throws InvalidDatasetException, IllegalStateException, IOException {
		dataset = datasetDao.save(dataset);
		if (dataset == null) {
			throw new InvalidDatasetException();
		}
		
		File file = MarbleUtil.multipartToFile(mfile);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(
				file));

		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			Object status =  JSON.parse(line);
			try {
				datastoreService.save(new UploadedStatus(dataset.getId(), status));
			} catch (Exception e) {
			}
		}

		bufferedReader.close();
		return dataset;
	}
	

}
