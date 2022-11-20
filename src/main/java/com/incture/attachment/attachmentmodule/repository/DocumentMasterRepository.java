package com.incture.attachment.attachmentmodule.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.incture.attachment.attachmentmodule.entity.DocumentMaster;

@Repository
public interface DocumentMasterRepository extends MongoRepository<DocumentMaster, String> {

}
