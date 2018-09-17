package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "attachment")
public class AttachmentEntity extends MongoBaseEntity implements Attachment {
    private String fileName, fileType;
    private long fileSize;

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileType() {
        return fileType;
    }

    @Override
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    @Override
    public void setFileSize(long size) {
        this.fileSize = size;
    }
}
