package org.marble.commons.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import org.marble.commons.dao.model.SenticItem;
import org.marble.commons.dao.model.ValidationItem;
import org.marble.commons.util.MarbleUtil;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVReadProc;

@Service
public class ValidationDataServiceImpl implements ValidationDataService {

    private static final Logger log = LoggerFactory.getLogger(ValidationDataServiceImpl.class);

    @Autowired
    DatastoreService datastoreService;

    @Override
    public void insertDataFromFile(MultipartFile file) throws IllegalStateException, IOException, SAXException,
            ParserConfigurationException {
        log.warn("Processing uploaded validation data from file <" + file.getOriginalFilename() + ">");
        insertDataFromFile(MarbleUtil.multipartToFile(file));

    }

    public void insertDataFromFile(File file) throws IllegalStateException, IOException, SAXException,
            ParserConfigurationException {
        datastoreService.removeCollection(ValidationItem.class);

        CSV csv = CSV
                .separator(',') // delimiter of fields
                .quote('"') // quote character
                .create(); // new instance is immutable

        csv.read(file, new CSVReadProc() {
            public void procRow(int rowIndex, String... values) {
                ValidationItem item = new ValidationItem();
                item.setId(rowIndex);
                item.setPolarity(Integer.parseInt(values[0]));
                item.setText(values[1]);
                datastoreService.save(item);
            }
        });

    }

}
