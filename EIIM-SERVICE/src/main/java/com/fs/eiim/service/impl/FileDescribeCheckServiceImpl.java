package com.fs.eiim.service.impl;

import com.alibaba.fastjson.JSON;
import com.fs.eiim.service.FileDescribeCheckService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class FileDescribeCheckServiceImpl implements FileDescribeCheckService {
    private static final Log logger = LogFactory.getLog(FileDescribeCheckServiceImpl.class);

    @Override
    public String checkFileDescribe(File file) {
        // 判断监测图片文件，获取图片的长和宽
        try {
            Image image = ImageIO.read(file);
            PicDescribe picDescribe = new PicDescribe(((BufferedImage) image).getWidth(),
                    ((BufferedImage) image).getHeight());
            return JSON.toJSONString(picDescribe);
        } catch (Exception ex) {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("The file[%s] is not image file.", file.getAbsoluteFile()));
            }
        }
        return null;
    }

    private class PicDescribe {
        private int width, height;

        public PicDescribe(int width, int height) {
            super();
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }
}
