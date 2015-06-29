package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.DatasetDao;
import org.marble.commons.dao.model.Dataset;
import org.marble.commons.exception.InvalidDatasetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatasetServiceImpl implements DatasetService {

	@Autowired
	DatasetDao datasetDao;

	@Override
	public Dataset updateDataset(Dataset dataset)
			throws InvalidDatasetException {
		dataset = datasetDao.save(dataset);
		if (dataset == null) {
			throw new InvalidDatasetException();
		}
		return dataset;
	}

	@Override
	public Dataset getDataset(Integer id)
			throws InvalidDatasetException {
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
	public Dataset createDataset(Dataset dataset)
			throws InvalidDatasetException {
		dataset = datasetDao.save(dataset);
		if (dataset == null) {
			throw new InvalidDatasetException();
		}
		return dataset;
	}

}
