package com.incture.attachment.attachmentmodule.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.incture.attachment.attachmentmodule.entity.DocumentDo;
import com.incture.attachment.attachmentmodule.entity.DocumentMaster;

public interface DocumentService {

	public String saveDocument(@RequestParam("file") MultipartFile file);

	public List<DocumentDo> getAllDocuments();

	public String downloadDocument(HttpServletRequest request, HttpServletResponse response, String id) throws IOException;

	public String addFile(MultipartFile file) throws IOException;

	public DocumentMaster downloadFile(String id) throws IOException;
}
