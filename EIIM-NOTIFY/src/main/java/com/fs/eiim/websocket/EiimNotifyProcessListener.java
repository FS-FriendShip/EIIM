package com.fs.eiim.websocket;

import com.alibaba.fastjson.JSONObject;
import org.mx.comps.notify.processor.NotifyProcessListener;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("eiimNotifyProcessListener")
public class EiimNotifyProcessListener implements NotifyProcessListener {
    /**
     * {@inheritDoc}
     *
     * @see NotifyProcessListener#before(JSONObject)
     */
    @Override
    public void before(JSONObject data) {
        // TODO
    }

    /**
     * {@inheritDoc}
     *
     * @see NotifyProcessListener#after(JSONObject, boolean, Set)
     */
    @Override
    public void after(JSONObject data, boolean success, Set<String> invalidDevices) {
        // TODO
    }
}
