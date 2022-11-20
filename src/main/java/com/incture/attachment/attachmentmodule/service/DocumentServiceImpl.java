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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.incture.attachment.attachmentmodule.entity.DocumentDo;
import com.incture.attachment.attachmentmodule.entity.DocumentMaster;
import com.incture.attachment.attachmentmodule.repository.DocumentMasterRepository;
import com.incture.attachment.attachmentmodule.repository.DocumentRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

@Service
public class DocumentServiceImpl implements DocumentService {

	@Autowired
	private DocumentRepository documentRepository;
	
	@Autowired
	private DocumentMasterRepository documentMasterRepository;
	
	@Autowired
	private GridFsTemplate gridfs;

	@Autowired
	private GridFsOperations operations;
	
//	public final String uploadDirectory = new ClassPathResource("static/files/").getFile().getAbsolutePath();
	public static String uploadDirectory = new File("src\\main\\resources\\static\\uploads").getAbsolutePath()+"\\";
	public DocumentServiceImpl() throws IOException {
		
	}
	@Override
	public String saveDocument(MultipartFile file) {
		try {
			// extensions
			DocumentDo document = new DocumentDo();
			String Multipart_fileName = file.getOriginalFilename();
			int lastIndex = Multipart_fileName.lastIndexOf('.');
			String fileType = Multipart_fileName.substring(lastIndex, Multipart_fileName.length());
			String fileOrgName = Multipart_fileName.substring(0, lastIndex);
//			if (!fileExtensions.contains(fileType.toLowerCase())) {
//				return "File extension not supported. (should contain only .PNG,.png,.JPG,.jpg,.jpeg,.JPEG)";
//			} else {
				DateFormat dateFormatter = new SimpleDateFormat("yyyymmddhhmmss");
				String currentDateTime = dateFormatter.format(new Date());
				String fileName = "DOC_" + currentDateTime
						+ file.getOriginalFilename().substring(file.getOriginalFilename().length() - fileType.length());
				Path fileNameAndPath = Paths.get(uploadDirectory, fileName);
				
				
				try {
					Files.write(fileNameAndPath, file.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
					e.getMessage();
				}
				long fileSize = Files.size(fileNameAndPath) / 1024;
				if(fileSize > 10240) {
					System.out.println("Maximum File size not allowed.");
					return "Maximum File size not allowed. ";
				}
				document.setDocumentId(fileName);
				document.setDocumentOrgName(fileOrgName);
				document.setDocumentType(fileType);
				document.setDocumentSize(fileSize);
				documentRepository.save(document);

				return "Document saved successfully.";
//			}
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
	public String downloadDocument(HttpServletRequest request, HttpServletResponse response, String id) throws IOException{
		Path fileNameAndPath = Paths.get(uploadDirectory, id);
		File file = new File(fileNameAndPath+"");
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
	
	
	public String addFile(MultipartFile file) throws IOException {
		DBObject metadata = new BasicDBObject();
		metadata.put("fileSize", file.getSize());
		Object fileID = gridfs.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(),
				metadata);
		return fileID.toString();
	}
	
	public DocumentMaster downloadFile(String id) throws IOException {

        GridFSFile gridFSFile = gridfs.findOne( new Query(Criteria.where("_id").is(id)) );

        DocumentMaster documentMaster = new DocumentMaster();

        if (gridFSFile != null && gridFSFile.getMetadata() != null) {
        	documentMaster.setFilename( gridFSFile.getFilename() );
        	documentMaster.setFileType( gridFSFile.getMetadata().get("_contentType").toString() );
        	documentMaster.setFileSize( gridFSFile.getMetadata().get("fileSize").toString() );
        	documentMaster.setFile(IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()) );
        }

        return documentMaster;
    }


}
