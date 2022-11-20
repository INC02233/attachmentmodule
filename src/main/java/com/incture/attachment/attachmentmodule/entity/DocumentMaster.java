package com.incture.attachment.attachmentmodule.entity;

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
public class DocumentMaster {

	private String id;
	private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
    
}
