package com.fs.eiim.restful.vo.baseData;

import com.fs.eiim.service.BaseDataService;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author : john date : 2018/8/19 上午10:01
 */
public class BaseDataItemVO {
    private String code, name, value, parentCode;

    public static BaseDataItemVO valueOf(BaseDataService.BaseDataItem baseDataItem) {
        if (baseDataItem == null) {
            return null;
        }
        BaseDataItemVO vo = new BaseDataItemVO();
        vo.code = baseDataItem.getCode();
        vo.name = baseDataItem.getName();
        vo.value = baseDataItem.getValue();
        vo.parentCode = baseDataItem.getParentCode();
        return vo;
    }

    public static List<BaseDataItemVO> valueOf(List<BaseDataService.BaseDataItem> baseDataItems) {
        List<BaseDataItemVO> vos = new ArrayList<>();
        if (baseDataItems != null && !baseDataItems.isEmpty()) {
            baseDataItems.forEach(item -> vos.add(valueOf(item)));
        }
        return vos;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
}
