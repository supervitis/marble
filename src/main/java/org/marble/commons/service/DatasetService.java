package org.marble.commons.service;

import java.io.IOException;
import java.util.List;

import org.marble.commons.dao.model.Dataset;
import org.marble.commons.dao.model.UploadedStatus;
import org.marble.commons.exception.InvalidDatasetException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.mongodb.DBCursor;

public interface DatasetService {

	
	public Dataset updateDataset(Dataset dataset, MultipartRequest mpreq) throws InvalidDatasetException, IllegalStateException, IOException;

	public Dataset getDataset(Integer id) throws InvalidDatasetException;

	List<Dataset> getDatasets();
	

	public void deleteDataset(Integer id);

	public Dataset createDataset(Dataset dataset, MultipartRequest mpreq) throws InvalidDatasetException, IllegalStateException, IOException;

	public Dataset getDatasetByName(String name) throws InvalidDatasetException;

	public List<UploadedStatus> downloadDataset(
			Integer datasetId);

	DBCursor findCursorByDatasetId(Integer datasetId);




}
