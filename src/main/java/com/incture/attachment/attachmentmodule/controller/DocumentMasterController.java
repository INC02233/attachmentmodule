package com.incture.attachment.attachmentmodule.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.attachment.attachmentmodule.entity.DocumentMaster;
import com.incture.attachment.attachmentmodule.repository.DocumentMasterRepository;
import com.incture.attachment.attachmentmodule.service.DocumentService;
import com.mongodb.client.gridfs.model.GridFSFile;

@RestController
@RequestMapping("/document")
public class DocumentMasterController {

	@Autowired
	DocumentService documentService;
	
	@Autowired
	DocumentMasterRepository documentMasterRepository;
	
	@Autowired
	private GridFsTemplate gridfs;
	
	@RequestMapping(method = RequestMethod.GET, value = "/documents")
	public List<DocumentMaster> getAllDocuments() {
		List<GridFSFile> fileList = new ArrayList<GridFSFile>();
		gridfs.find(new Query()).into(fileList);
		List<DocumentMaster> documentMaster = new ArrayList<DocumentMaster>();
		for(GridFSFile fs: fileList) {
			DocumentMaster dto = new DocumentMaster();
			dto.setId(fs.getObjectId().toString());
			dto.setFilename(fs.getFilename());
			dto.setFileType(fs.getFilename().substring(fs.getFilename().lastIndexOf('.') + 1, fs.getFilename().length()));
			dto.setFileSize(String.valueOf(fs.getLength()));
			dto.setUpload_date(fs.getUploadDate());
			documentMaster.add(dto);
		}
		
		
		return documentMaster;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/upload")
    public String upload(@RequestParam("files") MultipartFile files[]) throws IOException {
		for(int i = 0; i<files.length; i++) {
			documentService.addFile(files[i]);
		}
        return "";
    }

	@RequestMapping(method = RequestMethod.GET, value = "/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String id) throws IOException {
        DocumentMaster documentMaster = documentService.downloadFile(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(documentMaster.getFileType() ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + documentMaster.getFilename() + "\"")
                .body(new ByteArrayResource(documentMaster.getFile()));
    }
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/delete/{id}")
	public String deleteDocument(@PathVariable String id) {
		try {
			gridfs.delete(new Query(Criteria.where("_id").is(id)));
			return "File Deleted Successfully";
		} catch(Exception err) {
			return "Could not delete : " + err.getMessage();
		}
		
	}
	
}
