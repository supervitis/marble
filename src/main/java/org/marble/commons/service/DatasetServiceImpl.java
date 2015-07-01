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
		/*File file = MarbleUtil.multipartToFile(mfile);
		FileWriter fileWriter =
                new FileWriter("datasetupload.txt");

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter =
                new BufferedWriter(fileWriter);
        try {
        	String line = null;
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(new FileReader(file));

            while((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }    

            // Always close files.
            bufferedReader.close();     
            bufferedWriter.close();
        }
        catch(FileNotFoundException ex) {
                     
        }
        catch(IOException ex) {
                              
            // Or we could just do this: 
            // ex.printStackTrace();
        }*/
		//Guardar el fichero
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
	public Dataset createDataset(Dataset dataset, MultipartFile mfile)
			throws InvalidDatasetException, IllegalStateException, IOException {
		dataset = datasetDao.save(dataset);
		if (dataset == null) {
			throw new InvalidDatasetException();
		}else{
			/*File file = MarbleUtil.multipartToFile(mfile);
			FileWriter fileWriter =
	                new FileWriter("datasetupload.txt");

	            // Always wrap FileWriter in BufferedWriter.
	            BufferedWriter bufferedWriter =
	                new BufferedWriter(fileWriter);
	        try {
	        	String line = null;
	            // Always wrap FileReader in BufferedReader.
	            BufferedReader bufferedReader = 
	                new BufferedReader(new FileReader(file));

	            while((line = bufferedReader.readLine()) != null) {
	                bufferedWriter.write(line);
	                bufferedWriter.newLine();
	            }    

	            // Always close files.
	            bufferedReader.close();     
	            bufferedWriter.close();
	        }
	        catch(FileNotFoundException ex) {
	                     
	        }
	        catch(IOException ex) {
	                              
	            // Or we could just do this: 
	            // ex.printStackTrace();
	        }*/
			//Guardar el fichero
			//Código para correr el script? Debe de ser aquí supongo
		}
		return dataset;
	}

}
