package com.incture.attachment.attachmentmodule.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.incture.attachment.attachmentmodule.entity.DocumentDo;
import com.incture.attachment.attachmentmodule.repository.DocumentRepository;

@Service
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	private DocumentRepository documentRepository;
	
	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/assets/files";

	@Override
	public String saveDocument(DocumentDo document, MultipartFile file) {
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

	@Override
	public List<DocumentDo> getAllDocuments() {
		return documentRepository.findAll();
	}

	@Override
	public String downloadDocument(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException{
		File file = new File(uploadDirectory + "/" +fileName);
		if(file.exists()) {
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if(mimeType == null) {
				mimeType = "application/octet-stream";
			}
			response.setContentType(mimeType);
			response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
			response.setContentLength((int) file.length());
			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			FileCopyUtils.copy(inputStream, response.getOutputStream());
			return "Image downloaded successfully.";
		}
		else {
			return "File not found.";
		}
	}

}
