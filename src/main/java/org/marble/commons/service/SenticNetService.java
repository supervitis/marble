package org.marble.commons.service;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface SenticNetService {
    public void insertDataFromFile(MultipartFile file) throws IllegalStateException, IOException, SAXException, ParserConfigurationException;

    Float getPolarity(String sentence);
}
