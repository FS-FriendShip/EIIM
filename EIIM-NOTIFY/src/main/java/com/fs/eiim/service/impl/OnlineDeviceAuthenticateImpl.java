package com.fs.eiim.service.impl;

import com.fs.eiim.bean.ExtraDeviceData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.StringUtils;
import org.mx.comps.notify.online.OnlineDevice;
import org.mx.comps.notify.online.OnlineDeviceAuthenticate;
import org.mx.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 描述： 终端注册时的身份鉴别实现类
 *
 * @author john peng
 * Date time 2018/8/26 上午8:59
 */
@Component("onlineDeviceAuthenticate")
public class OnlineDeviceAuthenticateImpl implements OnlineDeviceAuthenticate {
    private static final Log logger = LogFactory.getLog(OnlineDeviceAuthenticateImpl.class);

    private JwtService jwtService;

    @Autowired
    public OnlineDeviceAuthenticateImpl(JwtService jwtService) {
        super();
        this.jwtService = jwtService;
    }

    /**
     * {@inheritDoc}
     *
     * @see OnlineDeviceAuthenticate#authenticate(OnlineDevice)
     */
    @Override
    public boolean authenticate(OnlineDevice onlineDevice) {
        if (onlineDevice != null && onlineDevice.getExtraData() != null) {
            ExtraDeviceData extraDeviceData = onlineDevice.getExtraData().getJSONObject("data").toJavaObject(ExtraDeviceData.class);
            if (extraDeviceData != null && !StringUtils.isBlank(extraDeviceData.getToken())) {
                String accountCode = extraDeviceData.getAccountCode(),
                        token = extraDeviceData.getToken();
                JwtService.JwtVerifyResult result = jwtService.verifyToken(token,
                        JwtService.JwtVerifyPredicateBuilder.eq("code", accountCode));
                return result.isPassed();
            }
        }
        if (logger.isWarnEnabled()) {
            logger.warn(String.format("The device authenticate fail, device id: %s.",
                    onlineDevice != null ? onlineDevice.getDeviceId() : ""));
        }
        return false;
    }
}
