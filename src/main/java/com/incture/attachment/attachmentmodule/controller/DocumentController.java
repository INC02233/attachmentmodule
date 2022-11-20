package com.incture.attachment.attachmentmodule.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.attachment.attachmentmodule.entity.DocumentDo;
import com.incture.attachment.attachmentmodule.service.DocumentService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	

	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String home() {
		return "Welcome to Document HUB !";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/documents")
	public List<DocumentDo> getAllDocuments() {
		return documentService.getAllDocuments();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	@ResponseBody
	public void saveDocument(@RequestParam("files") MultipartFile files[]) {
		for (int i = 0; i < files.length; i++) {
			documentService.saveDocument(files[i]);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/download/{id}")
	public String downloadImages(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") String id) throws IOException {
		return documentService.downloadDocument(request, response, id);
	}

}
