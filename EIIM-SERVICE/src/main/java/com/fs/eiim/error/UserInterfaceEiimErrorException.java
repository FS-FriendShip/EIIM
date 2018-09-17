package com.fs.eiim.error;

import org.mx.error.UserInterfaceError;
import org.mx.error.UserInterfaceException;

/**
 * 人机界面错误异常类
 *
 * @author : john.peng created on date : 2017/10/8
 */
public class UserInterfaceEiimErrorException extends UserInterfaceException {
    /**
     * 默认的构造函数
     */
    public UserInterfaceEiimErrorException() {
        this(EiimErrors.EIIM_OTHER_FAIL);
    }

    /**
     * 构造函数
     *
     * @param error 人机界面错误枚举
     */
    public UserInterfaceEiimErrorException(EiimErrors error) {
        super(error.getErrorCode(), error.getErrorMessage());
    }

    /**
     * EIIM(企业内部即时通信系统)操作错误枚举定义
     */
    public enum EiimErrors implements UserInterfaceError {
        ACCOUNT_BLANK_CODE("账户代码为空。"),
        ACCOUNT_BLANK_TOKEN("账户令牌为空。"),
        ACCOUNT_BLANK_PASSWORD("账户密码为空。"),
        ACCOUNT_PASSWORD_STRENGTHEN_LOW("账户密码强度太低。"),
        ACCOUNT_STATE_SAVE_FAIL("保存账户登录状态失败。"),
        ACCOUNT_SAVE_FAIL("保存指定的账户信息失败。"),
        ACCOUNT_NOT_FOUND("指定的账户不存在。"),
        PASSWORD_CHANGE_FAIL("修改账户密码失败。"),

        TOKEN_EXPIRED("账户令牌已经过期。"),

        NO_LOGIN_RECORD("指定账户没有登入系统的记录。"),

        ORG_NOT_FOUND("指定的组织单位不存在。"),
        ORG_SAVE_FAIL("保存指定的组织单位失败。"),

        PERSON_NOT_FOUND("指定的人员不存在。"),
        PERSON_SAVE_FAIL("保存指定的人员信息失败。"),

        CHATROOM_NOT_FOUND("指定的聊天室不存在。"),
        CHATROOM_TOP_FAIL("将指定聊天室置顶失败。"),
        CHATROOM_SAVE_FAIL("保存指定的聊天室信息失败。"),
        CHATROOM_DELETE_FAIL("删除指定的聊天室失败。"),
        CHATROOM_MEMBER_NOT_FOUND("指定的账户不在指定的聊天室中。"),

        CHATROOM_MEMBER_STATE_SAVE_FAIL("保存指定聊天成员状态失败。"),

        CHATROOM_MESSAGE_SAVE_FAIL("保存指定的聊天消息失败。"),

        CHATROOM_NOTICE_SAVE_FAIL("保存指定的聊天室公告失败。"),

        FILE_UPLOAD_FAIL("文件上传保存失败。"),
        FILE_DOWNLOAD_FAIL("文件下载读取失败。"),

        EIIM_OTHER_FAIL("未知的EIIM(企业内部即时通信系统)错误。");

        public static final int BASE_ORDINAL = 100;
        private String errorMessage;

        /**
         * 构造函数
         *
         * @param errorMessage 错误信息
         */
        EiimErrors(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        /**
         * {@inheritDoc}
         *
         * @see UserInterfaceError#getErrorCode()
         */
        @Override
        public int getErrorCode() {
            return BASE_ORDINAL + super.ordinal();
        }

        /**
         * {@inheritDoc}
         *
         * @see UserInterfaceError#getErrorMessage()
         */
        @Override
        public String getErrorMessage() {
            return this.errorMessage;
        }
    }
}
