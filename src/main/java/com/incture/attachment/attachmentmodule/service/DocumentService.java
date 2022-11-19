package com.incture.attachment.attachmentmodule.service;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.incture.attachment.attachmentmodule.entity.DocumentDo;

public interface DocumentService {

	public String saveTask(DocumentDo document, @RequestParam("file") MultipartFile file);
}
