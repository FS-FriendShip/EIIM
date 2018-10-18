package com.fs.eiim.dal.entity;

import org.mx.dal.entity.Base;

/**
 * 描述： 聊天消息信息接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午1:25
 */
public interface ChatMessage extends Base {
    ChatRoom getChatRoom();

    void setChatRoom(ChatRoom chatRoom);

    Account getSender();

    void setSender(Account sender);

    long getSentTime();

    void setSentTime(long sentTime);

    MessageType getMessageType();

    void setMessageType(MessageType messageType);

    Object getMessage();

    void setMessage(Object message);

    Message getMessageByType();

    void setMessageByType(Message message);

    enum MessageType {
        TEXT, FILE
    }

    interface Message {
        //
    }

    class TextMessage implements Message {
        private String text;

        public TextMessage(String text) {
            super();
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    class FileMessage implements Message {
        private String id, fileName, fileType, fileDescribe;
        private long fileSize;

        public FileMessage(Attachment attachment) {
            super();
            this.id = attachment.getId();
            this.fileName = attachment.getFileName();
            this.fileType = attachment.getFileType();
            this.fileSize = attachment.getFileSize();
            this.fileDescribe = attachment.getFileDescribe();
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
}
