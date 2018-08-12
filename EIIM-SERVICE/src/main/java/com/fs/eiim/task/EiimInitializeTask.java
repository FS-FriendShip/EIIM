package com.fs.eiim.task;

import com.fs.eiim.service.InitializeTaskService;
import org.mx.spring.task.BaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 描述： 内置字典数据初始化任务
 *
 * @author john peng
 * Date time 2018/8/12 下午10:30
 */
@Component("eiimInitializeTask")
public class EiimInitializeTask extends BaseTask {
    private InitializeTaskService initializeTaskService;

    /**
     * 默认的构造函数
     *
     * @param initializeTaskService 初始化任务服务接口
     */
    @Autowired
    public EiimInitializeTask(InitializeTaskService initializeTaskService) {
        // 同步执行任务
        super("内置字典数据初始化任务", false);
        this.initializeTaskService = initializeTaskService;
    }

    /**
     * {@inheritDoc}
     *
     * @see BaseTask#invoke()
     */
    @Override
    public void invoke() {
        initializeTaskService.initializeInternalData();
    }
}
