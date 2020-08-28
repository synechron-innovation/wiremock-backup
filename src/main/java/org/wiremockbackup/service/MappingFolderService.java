package org.wiremockbackup.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.swagger.client.model.Body;
import io.swagger.client.model.Body1;

@Service
public class MappingFolderService {

//	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
//		MappingFolderService s = new MappingFolderService();
//
//		String strMappingsSourcePath = "/Users/ravi_kalla/Desktop/softwares/wiremock/mappings";
//		String strMappingsTargetPath = "/Users/ravi_kalla/Desktop/softwares/wiremock/mappings_copied";
//
//		List<String> lstFileNames = new ArrayList<String>();
//		s.findFiles(strMappingsSourcePath, lstFileNames);
//		List<Body1> mappings = s.convertFilesToMappings(strMappingsSourcePath, lstFileNames);
//		System.out.println("File Size = " + lstFileNames.size());
//
//		s.createFilesInFolder(mappings, strMappingsTargetPath);
//	}

	public void createFilesInFolder(List<Body1> splitFile, String strParentFolder) throws JsonParseException, JsonMappingException, IOException {

		strParentFolder = updateWinFolderPath(strParentFolder);

		boolean isDirCreated;
		String[] folderAndFile;
		String strCurrentFolderName;
		String strCurrentFileName;
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		for (Body1 mapping : splitFile) {
			isDirCreated = false;

			folderAndFile = extractFolderAndFile(strParentFolder, mapping.getName());
			strCurrentFolderName = folderAndFile[0];
			strCurrentFileName = folderAndFile[1];

			File fileDirectory = new File(strCurrentFolderName);
			if (! fileDirectory.exists())
				isDirCreated = fileDirectory.mkdirs();
			else
				isDirCreated = true;

			if (isDirCreated) {
				Path path = Paths.get(strCurrentFolderName + "/"  + strCurrentFileName + "-" + mapping.getId() + ".json");
		        Files.write(path, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapping).getBytes());
			}
		}
	}

	public String[] extractFolderAndFile(String strParentFolder, String strMappingName) {
		String[] folderAndFile = new String[2];
		String strFolderName = strParentFolder;
		String strFileName;
		String[] splitMajorFolderPortion = strMappingName.split(":::");
		String[] splitMinorFolderPortion;
		String strTempFolderPortionOfFile;
		StringBuilder strChildFolders;
		if (splitMajorFolderPortion.length > 1) {
			strFileName = splitMajorFolderPortion[1];
			strTempFolderPortionOfFile = splitMajorFolderPortion[0];
			splitMinorFolderPortion = strTempFolderPortionOfFile.split("::");
			strChildFolders = new StringBuilder();
			for (String strMinorFolderPortion : splitMinorFolderPortion) {
				strChildFolders.append("/").append(strMinorFolderPortion);
			}
			strFolderName += strChildFolders.toString();
		}
		else {
			strFileName = splitMajorFolderPortion[0];
		}
		folderAndFile[0] = strFolderName;
		folderAndFile[1] = strFileName;
		return folderAndFile;
	}

	public void findFiles(String strFolderName, List<String> lstFileNames) {
		strFolderName = updateWinFolderPath(strFolderName);

		File[] files = new File(strFolderName).listFiles();
	    for (File file : files) {
	        if (file.isDirectory()) {
	            findFiles(strFolderName + "/" + file.getName(), lstFileNames);
	        } else {
	            if (file.getName().endsWith(".json"))
	            	lstFileNames.add(strFolderName + "/" + file.getName());
	        }
	    }
	}

	public List<Body> convertFilesToMappings(String strMappingsRootPath) throws JsonParseException, JsonMappingException, IOException {
		strMappingsRootPath = updateWinFolderPath(strMappingsRootPath);
		List<String> lstFileNames = new ArrayList<String>();

		findFiles(strMappingsRootPath, lstFileNames);

		//create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<Body> lstMapping = new ArrayList<Body>();

        for (String strFileName : lstFileNames) {
        	Body mapping = objectMapper.readValue(new File(strFileName), Body.class);
        	mapping.setName(getHierarchyName(strMappingsRootPath, strFileName));
        	lstMapping.add(mapping);
        }

        return lstMapping;
	}
	public String getHierarchyName(String strMappingsSourcePath, String strAbsoluteFilePath) {
		File file = new File(strAbsoluteFilePath);
		String strFileName = file.getName();
		String strFileNameWithoutExtenstion = strFileName.split("\\.")[0];
		String strFolderPath = file.getParentFile().getAbsolutePath();
		strFolderPath = strFolderPath.replace("\\\\", "\\");
		strFolderPath = strFolderPath.replace("\\", "/");
		String strDeltaFolder = strFolderPath.substring(strMappingsSourcePath.length());
		String[] splitFolders = strDeltaFolder.split("/");
		StringBuilder strBufDeltaFolderPath = new StringBuilder();
		boolean isDeltaStarted = false;
		for (String strFolder : splitFolders) {
			if (null != strFolder && !strFolder.trim().isEmpty()) {
				if (isDeltaStarted)
					strBufDeltaFolderPath.append("::");
				isDeltaStarted = true;

				strBufDeltaFolderPath.append(strFolder);
			}
		}
		String strDeltaFolderPath = strBufDeltaFolderPath.toString();
		if (null != strDeltaFolderPath && !strDeltaFolderPath.trim().isEmpty())
			strFileNameWithoutExtenstion = strDeltaFolderPath + ":::" + strFileNameWithoutExtenstion;

		return strFileNameWithoutExtenstion;
	}

	public String updateWinFolderPath(String folder) {
		folder = folder.replace("\\\\", "\\");
		folder = folder.replace("\\", "/");

		return folder;
	}
}