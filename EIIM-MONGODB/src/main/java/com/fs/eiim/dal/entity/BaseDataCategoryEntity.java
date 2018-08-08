package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseDictEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 描述： 基础数据分类实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午3:55
 */
@Document(collection = "basedata-category")
public class BaseDataCategoryEntity extends MongoBaseDictEntity implements BaseDataCategory {
}
