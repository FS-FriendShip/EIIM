package com.fs.eiim.service;

import java.io.File;
import java.io.InputStream;

/**
 * 描述：
 *
 * @author : john date : 2018/9/17 下午8:46
 */
public interface FileTransformService {
    FileUploadBean uploadFile(String fileName, InputStream in);

    FileDownloadBean downloadFile(String uuid);

    class FileUploadBean {
        private String id, fileName, fileType;
        private long fileSize;

        public FileUploadBean(String id, String fileName, String fileType, long fileSize) {
            super();
            this.id = id;
            this.fileName = fileName;
            this.fileType = fileType;
            this.fileSize = fileSize;
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

        public long getFileSize() {
            return fileSize;
        }
    }

    class FileDownloadBean {
        File file;
        String fileName;

        public FileDownloadBean(File file, String fileName) {
            super();
            this.file = file;
            this.fileName = fileName;
        }

        public File getFile() {
            return file;
        }

        public String getFileName() {
            return fileName;
        }
    }
}
