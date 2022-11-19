package com.incture.attachment.attachmentmodule.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.incture.attachment.attachmentmodule.entity.DocumentDo;

@Repository
public interface DocumentRepository extends MongoRepository<DocumentDo, Integer>{

}
