package com.fs.eiim.restful.vo.baseData;

import com.fs.eiim.service.BaseDataService;

import java.util.ArrayList;
import java.util.List;

public class BaseDataVO {
    private String id, code, name;
    private List<BaseDataItemVO> items;

    public static BaseDataVO valueOf(BaseDataService.BaseData baseData) {
        if (baseData == null) {
            return null;
        }
        BaseDataVO vo = new BaseDataVO();
        vo.id = baseData.getId();
        vo.code = baseData.getCode();
        vo.name = baseData.getName();
        vo.items = BaseDataItemVO.valueOf(baseData.getItems());
        return vo;
    }

    public static List<BaseDataVO> valueOf(List<BaseDataService.BaseData> baseDatas) {
        List<BaseDataVO> vos = new ArrayList<>();
        if (baseDatas != null && !baseDatas.isEmpty()) {
            baseDatas.forEach(baseData -> vos.add(valueOf(baseData)));
        }
        return vos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BaseDataItemVO> getItems() {
        return items;
    }

    public void setItems(List<BaseDataItemVO> items) {
        this.items = items;
    }
}
