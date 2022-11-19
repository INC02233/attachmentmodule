package com.incture.attachment.attachmentmodule.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.incture.attachment.attachmentmodule.entity.DocumentDo;
import com.incture.attachment.attachmentmodule.service.DocumentService;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public String home() {
		return "Welcome to Document HUB !";
	}

	
	@RequestMapping(method = RequestMethod.POST, value = "/upload")
	@ResponseBody
	public void saveTask(DocumentDo document, @RequestParam("file") MultipartFile file) {
		documentService.saveTask(document, file);
	}
}
