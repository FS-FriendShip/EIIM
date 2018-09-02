package com.fs.eiim.restful.vo.org;

import com.fs.eiim.dal.entity.Org;
import com.fs.eiim.restful.vo.person.PersonInfoVO;
import org.mx.comps.rbac.dal.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrgInfoVO {
    private String id, code, name, type, desc;
    private List<OrgInfoVO> children;
    private List<PersonInfoVO> employees;
    private PersonInfoVO manager;

    @SuppressWarnings("unchecked")
    public static OrgInfoVO valueOf(Org org) {
        if (org == null) {
            return null;
        }
        OrgInfoVO orgInfoVO = new OrgInfoVO();
        orgInfoVO.id = org.getId();
        orgInfoVO.code = org.getCode();
        orgInfoVO.name = org.getName();
        orgInfoVO.type = org.getType();
        orgInfoVO.desc = org.getDesc();
        Set<Org> children = org.getChildren();
        if (children != null && !children.isEmpty()) {
            List<OrgInfoVO> list = new ArrayList<>();
            children.forEach(child -> list.add(valueOf(child)));
            orgInfoVO.children = list;
        }
        if (org.getManager() != null) {
            orgInfoVO.manager = PersonInfoVO.valueOf(org.getManager(), null, null);
        }
        Set<User> employees = org.getEmployees();
        if (employees != null && !employees.isEmpty()) {
            List<PersonInfoVO> list = new ArrayList<>();
            employees.forEach(employee -> list.add(PersonInfoVO.valueOf(employee, null, null)));
            orgInfoVO.employees = list;
        }
        return orgInfoVO;
    }

    public static List<OrgInfoVO> valueOf(List<Org> orgs) {
        List<OrgInfoVO> list = new ArrayList<>();
        if (orgs != null && !orgs.isEmpty()) {
            orgs.forEach(org -> list.add(valueOf(org)));
        }
        return list;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public List<OrgInfoVO> getChildren() {
        return children;
    }

    public List<PersonInfoVO> getEmployees() {
        return employees;
    }

    public PersonInfoVO getManager() {
        return manager;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setChildren(List<OrgInfoVO> children) {
        this.children = children;
    }

    public void setEmployees(List<PersonInfoVO> employees) {
        this.employees = employees;
    }

    public void setManager(PersonInfoVO manager) {
        this.manager = manager;
    }
}
