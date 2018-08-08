package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseDictTreeEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/**
 * 描述： 组织单位信息实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午5:49
 */
@Document(collection = "organization")
public class OrgEntity extends MongoBaseDictTreeEntity<OrgEntity> implements Org {
    private String type;
    @DBRef
    private Person manager;
    @DBRef
    private Set<Person> employees;

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

    /**
     * {@inheritDoc}
     *
     * @see Org#getManager()
     */
    @Override
    public Person getManager() {
        return manager;
    }

    /**
     * {@inheritDoc}
     *
     * @see Org#setManager(Person)
     */
    @Override
    public void setManager(Person manager) {
        this.manager = manager;
    }

    /**
     * {@inheritDoc}
     *
     * @see Org#getEmployees()
     */
    @Override
    public Set<Person> getEmployees() {
        return employees;
    }

    /**
     * {@inheritDoc}
     *
     * @see Org#setEmployees(Set)
     */
    @Override
    public void setEmployees(Set<Person> employees) {
        this.employees = employees;
    }
}
