package com.fs.eiim.dal.entity;

import org.mx.comps.rbac.dal.entity.DepartmentEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 描述： 组织单位信息实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午5:49
 */
@Document(collection = "org")
public class OrgEntity extends DepartmentEntity implements Org {
    private String type;

    /**
     * {@inheritDoc}
     *
     * @see Org#getType()
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     *
     * @see Org#setType(String)
     */
    @Override
    public void setType(String type) {
        this.type = type;
    }
}
