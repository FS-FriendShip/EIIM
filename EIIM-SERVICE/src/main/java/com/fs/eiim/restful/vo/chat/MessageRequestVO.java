package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.service.ChatRoomService;
import org.mx.dal.Pagination;

public class MessageRequestVO {
    private String lastMessageId;
        private ChatRoomService.Direction direction = ChatRoomService.Direction.FORWARD;
    private boolean pageable = false;
    private int page = 1, pageSize = 20;

    public Pagination getPagination() {
        if (pageable) {
            Pagination pagination = new Pagination();
            pagination.setPage(page);
            pagination.setSize(pageSize);
            return pagination;
        } else {
            return null;
        }
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public ChatRoomService.Direction getDirection() {
        return direction;
    }

    public void setDirection(ChatRoomService.Direction direction) {
        this.direction = direction;
    }

    public boolean isPageable() {
        return pageable;
    }

    public void setPageable(boolean pageable) {
        this.pageable = pageable;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
