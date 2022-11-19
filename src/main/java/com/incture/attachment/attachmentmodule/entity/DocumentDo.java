package com.incture.attachment.attachmentmodule.entity;

import javax.annotation.Generated;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "DOCUMENT_MASTER")
public class DocumentDo {

	@Id
	private String id;
	private String documentName;
	private String documentType;
	private long documentSize;
	
}
