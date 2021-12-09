package com.deo.activitimook.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;

import java.util.Arrays;
import java.util.List;

/**
 * 多实例注册监听
 * 启动流程时，给会签节点的多实例进行赋值操作
 *
 * @date 2021-12-07
 * @since TODO
 */
public class MultiInstancesStartListener implements ExecutionListener {

//    private Expression sendType;

    @Override
    public void notify(DelegateExecution execution) {
        List<String> list = Arrays.asList("bajie", "wukong", "salaboy");
        execution.setVariable("assigneeList", list);
    }
}
