package org.marble.commons.service;

import java.util.List;

import org.marble.commons.dao.model.Dataset;
import org.marble.commons.exception.InvalidDatasetException;

public interface DatasetService {

	public Dataset updateDataset(Dataset dataset) throws InvalidDatasetException;

	public Dataset getDataset(Integer id) throws InvalidDatasetException;

	List<Dataset> getDatasets();
	

	public void deleteDataset(Integer id);

	public Dataset createDataset(Dataset dataset) throws InvalidDatasetException;

}
