package com.fs.eiim.service.impl;

import com.fs.eiim.dal.entity.Attachment;
import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.service.FileDescribeCheckService;
import com.fs.eiim.service.FileTransformService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.FileUtils;
import org.mx.StringUtils;
import org.mx.dal.EntityFactory;
import org.mx.dal.service.GeneralAccessor;
import org.mx.error.UserInterfaceSystemErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component("fileSimpleTransformService")
public class FileSimpleTransformServiceImpl implements FileTransformService {
    private static final Log logger = LogFactory.getLog(FileSimpleTransformServiceImpl.class);

    @Value("${upload.root:upload}")
    private String uploadRootPath;

    private GeneralAccessor generalAccessor;
    private FileDescribeCheckService fileDescribeCheckService;

    @Autowired
    public FileSimpleTransformServiceImpl(@Qualifier("generalAccessorMongodb") GeneralAccessor generalAccessor,
                                          FileDescribeCheckService fileDescribeCheckService) {
        super();
        this.generalAccessor = generalAccessor;
        this.fileDescribeCheckService = fileDescribeCheckService;
    }

    @Override
    public FileUploadBean uploadFile(String fileName, InputStream in) {
        String fileType = "";
        int pos = fileName.lastIndexOf('.');
        if (pos >= 0) {
            fileType = fileName.substring(pos + 1);
        }

        Attachment attachment = EntityFactory.createEntity(Attachment.class);
        attachment.setFileName(fileName);
        attachment.setFileType(fileType);
        attachment = generalAccessor.save(attachment);

        String uuid = attachment.getId();
        String path = String.format("%s/%s", uploadRootPath, uuid.substring(0, 2));
        String name = uuid.substring(2);

        try {
            FileUtils.saveFile(path, name, in);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Upload the file[%s] successfully, path: %s/%s.",
                        fileName, path, name));
            }
            Path filePath = Paths.get(path, name);
            long size = Files.size(filePath);
            attachment.setFileSize(size);
            attachment.setFileDescribe(fileDescribeCheckService.checkFileDescribe(filePath.toFile()));
            attachment = generalAccessor.save(attachment);
            return new FileUploadBean(attachment.getId(), attachment.getFileName(), attachment.getFileType(),
                    attachment.getFileDescribe(), attachment.getFileSize());
        } catch (IOException ex) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("Save the file[%s] from input stream fail.", path));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.FILE_UPLOAD_FAIL
            );
        }
    }

    @Override
    public FileDownloadBean downloadFile(String uuid) {
        if (StringUtils.isBlank(uuid)) {
            if (logger.isErrorEnabled()) {
                logger.error("The uuid is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        String path = String.format("%s/%s", uploadRootPath, uuid.substring(0, 2));
        String name = uuid.substring(2);
        Path pathFile = Paths.get(path, name);
        if (!Files.exists(pathFile) || !Files.isRegularFile(pathFile)) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The file[%s/%s] not found.", path, name));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.FILE_NOT_FOUND
            );
        }
        Attachment attachment = generalAccessor.getById(uuid, Attachment.class);
        if (attachment == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The attachment[%s] not found.", uuid));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.FILE_NOT_FOUND
            );
        }
        return new FileDownloadBean(pathFile.toFile(), attachment.getFileName(),
                attachment.getFileType(), attachment.getFileDescribe(), attachment.getFileSize());
    }
}
