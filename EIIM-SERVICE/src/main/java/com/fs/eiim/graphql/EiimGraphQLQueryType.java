package com.fs.eiim.graphql;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.Person;
import graphql.schema.DataFetchingEnvironment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.StringUtils;
import org.mx.comps.rbac.dal.entity.Role;
import org.mx.dal.entity.Base;
import org.mx.dal.service.GeneralDictAccessor;
import org.mx.error.UserInterfaceSystemErrorException;
import org.mx.service.rest.graphql.GraphQLFieldListResult;
import org.mx.service.rest.graphql.GraphQLFieldSingleResult;
import org.mx.service.rest.graphql.GraphQLTypeExecution;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("eiimGraphQLTypeExecution")
public class EiimGraphQLQueryType extends GraphQLTypeExecution implements InitializingBean {
    private static final Log logger = LogFactory.getLog(EiimGraphQLQueryType.class);

    private GeneralDictAccessor dictAccessor;

    @Autowired
    public EiimGraphQLQueryType(@Qualifier("generalDictAccessor") GeneralDictAccessor dictAccessor) {
        super("QueryType");
        this.dictAccessor = dictAccessor;
    }

    @Override
    public void afterPropertiesSet() {
        // TODO 创建自定义执行器
        super.addExecution(new FieldDefaultExecution<>("person", Person.class));
        super.addExecution(new FieldDefaultExecution<>("role", Role.class));
        super.addExecution(new FieldDefaultExecution<>("account", Account.class));
    }

    private class FieldDefaultExecution<T extends Base> implements GraphQLFieldSingleResult, GraphQLFieldListResult {
        private String fieldName;
        private Class<T> clazz;

        public FieldDefaultExecution(String fieldName, Class<T> clazz) {
            super();
            this.fieldName = fieldName;
            this.clazz = clazz;
        }

        @Override
        public List<T> executeForList(DataFetchingEnvironment environment) {
            return dictAccessor.list(clazz);
        }

        @Override
        public T executeForSingle(DataFetchingEnvironment environment) {
            String id = environment.getArgument("id");
            if (!StringUtils.isBlank(id)) {
                return dictAccessor.getById(id, clazz);
            } else {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("The %s's id is blank.", clazz.getName()));
                }
                throw new UserInterfaceSystemErrorException(
                        UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
                );
            }
        }

        @Override
        public String getFieldName() {
            return fieldName;
        }
    }
}
