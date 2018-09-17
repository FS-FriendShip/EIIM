package com.fs.eiim.service.impl;

import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.service.FileTransformService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.DigestUtils;
import org.mx.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component("fileSimpleTransformService")
public class FileSimpleTransformServiceImpl implements FileTransformService {
    private static final Log logger = LogFactory.getLog(FileSimpleTransformServiceImpl.class);

    @Value("${upload.root:upload}")
    private String uploadRootPath;

    private String createPath(String uuid) {
        return String.format("%s/%s/%s", uploadRootPath, uuid.substring(0, 2), uuid.substring(2));
    }

    @Override
    public FileUploadBean uploadFile(String fileName, InputStream in) {
        String fileType = "";
        int pos = fileName.lastIndexOf('.');
        if (pos >= 0) {
            fileType = fileName.substring(pos + 1);
        }

        String uuid = DigestUtils.uuid();
        String path = String.format("%s/%s", uploadRootPath, uuid.substring(0, 2));
        String name = uuid.substring(2);
        try {
            FileUtils.saveFile(path, name, in);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Upload the file[%s] successfully, path: %s/%s.",
                        fileName, path, name));
            }
            return new FileUploadBean(uuid, fileName, fileType, Files.size(Paths.get(path, name)));
        } catch (IOException ex) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("Save the file[%s] from input stream fail.", path));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.FILE_UPLOAD_FAIL
            );
        }
    }
}
