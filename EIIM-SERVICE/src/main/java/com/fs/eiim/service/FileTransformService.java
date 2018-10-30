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

    FileDownloadBean downloadAvatarFile(String accountId);

    class FileUploadBean {
        private String id, fileName, fileType, fileDescribe;
        private long fileSize;

        public FileUploadBean(String id, String fileName, String fileType, String fileDescribe, long fileSize) {
            super();
            this.id = id;
            this.fileName = fileName;
            this.fileType = fileType;
            this.fileDescribe = fileDescribe;
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

        public String getFileDescribe() {
            return fileDescribe;
        }
    }

    class FileDownloadBean {
        private File file;
        private String fileName, fileType, fileDescribe;
        private long fileSize;

        public FileDownloadBean(File file, String fileName, String fileType, String fileDescribe, long fileSize) {
            super();
            this.file = file;
            this.fileName = fileName;
            this.fileType = fileType;
            this.fileDescribe = fileDescribe;
            this.fileSize = fileSize;
        }

        public File getFile() {
            return file;
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

        public String getFileDescribe() {
            return fileDescribe;
        }
    }
}
