package com.fs.eiim.service;

import java.io.InputStream;

/**
 * 描述：
 *
 * @author : john date : 2018/9/17 下午8:46
 */
public interface FileTransformService {
    FileUploadBean uploadFile(String fileName, InputStream in);

    class FileUploadBean {
        private String id, fileName, fileType;
        private long size;

        public FileUploadBean(String id, String fileName, String fileType, long size) {
            super();
            this.id = id;
            this.fileName = fileName;
            this.fileType = fileType;
            this.size = size;
        }

        public String getId() {
            return id;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFileType() {
            return fileType;
        }

        public long getSize() {
            return size;
        }
    }
}
