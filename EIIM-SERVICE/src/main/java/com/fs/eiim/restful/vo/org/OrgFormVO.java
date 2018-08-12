package com.fs.eiim.restful.vo.org;

import com.fs.eiim.dal.entity.Org;
import com.fs.eiim.dal.entity.Person;
import org.mx.StringUtils;
import org.mx.comps.rbac.dal.entity.User;
import org.mx.dal.EntityFactory;

import java.util.HashSet;
import java.util.Set;

public class OrgFormVO {
    private String id, code, name, type, parentId, managerId;
    private String[] employeeIds;

    public Org get() {
        Org org = EntityFactory.createEntity(Org.class);
        org.setId(id);
        org.setCode(code);
        org.setName(name);
        org.setType(type);
        if (!StringUtils.isBlank(parentId)) {
            Org parent = EntityFactory.createEntity(Org.class);
            parent.setId(parentId);
            org.setParent(parent);
        }
        if (!StringUtils.isBlank(managerId)) {
            Person person = EntityFactory.createEntity(Person.class);
            person.setId(managerId);
            org.setManager(person);
        }
        if (employeeIds != null && employeeIds.length > 0) {
            Set<User> employees = new HashSet<>();
            for (String employeeId : employeeIds) {
                if (!StringUtils.isBlank(employeeId)) {
                    Person employee = EntityFactory.createEntity(Person.class);
                    employee.setId(employeeId);
                    employees.add(employee);
                }
            }
            org.setEmployees(employees);
        }
        return org;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String[] getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(String[] employeeIds) {
        this.employeeIds = employeeIds;
    }
}
