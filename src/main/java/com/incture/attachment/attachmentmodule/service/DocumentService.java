package com.incture.attachment.attachmentmodule.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.incture.attachment.attachmentmodule.entity.DocumentDo;

public interface DocumentService {

	public String saveDocument(DocumentDo document, @RequestParam("file") MultipartFile file);

	public List<DocumentDo> getAllDocuments();

	public String downloadDocument(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException;
}
