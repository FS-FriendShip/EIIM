package com.fs.eiim.dal.entity;

import org.mx.dal.entity.Base;

public interface Attachment extends Base {
    String getFileName();

    void setFileName(String fileName);

    String getFileType();

    void setFileType(String fileType);

    long getFileSize();

    void setFileSize(long size);
}
