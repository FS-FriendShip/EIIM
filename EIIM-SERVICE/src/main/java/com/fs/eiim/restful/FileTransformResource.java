package com.fs.eiim.restful;

import com.fs.eiim.service.FileTransformService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.mx.service.rest.vo.DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

@Component("fileTransformResource")
@Path("rest/v1")
@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
public class FileTransformResource {
    private FileTransformService fileTransformService;

    @Autowired
    public FileTransformResource(FileTransformService fileTransformService) {
        super();
        this.fileTransformService = fileTransformService;
    }

    @Path("upload")
    @POST
    public DataVO<FileTransformService.FileUploadBean> uploadFile(@FormDataParam("file") InputStream in,
                                                                  @FormDataParam("file") FormDataContentDisposition detail) {
        String fileName = detail.getFileName();
        return new DataVO<>(fileTransformService.uploadFile(fileName, in));
    }
}
