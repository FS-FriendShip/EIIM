package com.fs.eiim.restful;

import com.fs.eiim.service.FileTransformService;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.mx.service.rest.vo.DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

@Component("fileTransformResource")
@Path("rest/v1")
public class FileTransformResource {
    private FileTransformService fileTransformService;

    @Autowired
    public FileTransformResource(FileTransformService fileTransformService) {
        super();
        this.fileTransformService = fileTransformService;
    }

    @Path("upload")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public DataVO<FileTransformService.FileUploadBean> uploadFile(@FormDataParam("file") InputStream in,
                                                                  @FormDataParam("file") FormDataContentDisposition detail) {
        String fileName = detail.getFileName();
        return new DataVO<>(fileTransformService.uploadFile(fileName, in));
    }

    @Path("download/{uuid}")
    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response downloadFile(@PathParam("uuid") String uuid) {
        FileTransformService.FileDownloadBean downloadBean = fileTransformService.downloadFile(uuid);
        if (downloadBean != null) {
            return Response.ok(downloadBean.getFile())
                    .header("Content-disposition", "attachment;filename=" + downloadBean.getFileName())
                    .header("Cache-Control", "no-cache").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
