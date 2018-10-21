package com.fs.eiim.restful;

import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.service.FileTransformService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.mx.error.UserInterfaceSystemErrorException;
import org.mx.service.rest.vo.DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Component("fileTransformResource")
@Path("rest/v1")
public class FileTransformResource {
    private static final Log logger = LogFactory.getLog(FileTransformResource.class);

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
        try {
            String fileName = new String(detail.getFileName().getBytes("iso8859-1"), "utf-8");
//            URLDecoder.decode(detail.getFileName(), "UTF-8");
            return new DataVO<>(fileTransformService.uploadFile(fileName, in));
        } catch (UnsupportedEncodingException ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Decode the filename fail.", ex);
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            );
        }
    }

    @Path("download/{uuid}")
    @GET
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response downloadFile(@PathParam("uuid") String uuid) {
        FileTransformService.FileDownloadBean downloadBean = fileTransformService.downloadFile(uuid);
        if (downloadBean != null) {
            try {
                String filename = URLEncoder.encode(downloadBean.getFileName(), "UTF-8");
                return Response.ok(downloadBean.getFile())
                        .header("Content-disposition", "attachment;filename* = UTF-8''" + filename)
                        .header("Content-Length", downloadBean.getFileSize())
                        .header("Cache-Control", "no-cache").build();
            } catch (UnsupportedEncodingException ex) {
                throw new UserInterfaceSystemErrorException(
                        UserInterfaceSystemErrorException.SystemErrors.SYSTEM_NO_SUCH_ALGORITHM
                );
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
