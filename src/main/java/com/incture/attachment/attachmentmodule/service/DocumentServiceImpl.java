package com.incture.attachment.attachmentmodule.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.incture.attachment.attachmentmodule.entity.DocumentDo;
import com.incture.attachment.attachmentmodule.repository.DocumentRepository;

@Service
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	private DocumentRepository documentRepository;
	
	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/assets/files";

	@Override
	public String saveTask(DocumentDo document, MultipartFile file) {
		try {
			// extensions
			document.setId(UUID.randomUUID().toString().split("-")[0]);
			String fileExtensions = ".PNG,.png,.JPG,.jpg,.jpeg,.JPEG";
			String Multipart_fileName = file.getOriginalFilename();
			int lastIndex = Multipart_fileName.lastIndexOf('.');
			String fileType = Multipart_fileName.substring(lastIndex, Multipart_fileName.length());

			if (!fileExtensions.contains(fileType.toLowerCase())) {
				return "File extension not supported. (should contain only .PNG,.png,.JPG,.jpg,.jpeg,.JPEG)";
			} else {
				DateFormat dateFormatter = new SimpleDateFormat("yyyymmddhhmmss");
				String currentDateTime = dateFormatter.format(new Date());
				String fileName = "DOC_" + currentDateTime
						+ file.getOriginalFilename().substring(file.getOriginalFilename().length() - 4);
				Path fileNameAndPath = Paths.get(uploadDirectory, fileName);
				try {
					Files.write(fileNameAndPath, file.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
					e.getMessage();
				}
				
				long fileSize = Files.size(fileNameAndPath) / 1024;
				document.setDocumentName(fileName);
				document.setDocumentType(fileType);
				document.setDocumentSize(fileSize);
				documentRepository.save(document);

				return "Document saved successfully.";
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "Exception caught : " + e.getMessage();
		}
		
	}

}
