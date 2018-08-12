package com.fs.eiim.restful.vo.role;

import org.mx.comps.rbac.dal.entity.Role;

public class RoleInfoVO {
    private String id, code, name, desc;

    public static RoleInfoVO valueOf(Role role) {
        if (role == null) {
            return null;
        }
        RoleInfoVO roleInfoVO = new RoleInfoVO();
        roleInfoVO.id = role.getId();
        roleInfoVO.code = role.getCode();
        roleInfoVO.name = role.getName();
        roleInfoVO.desc = role.getDesc();
        return roleInfoVO;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
