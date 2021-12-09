package com.deo.activitimook.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 服务任务
 *
 * @author SL Zhou
 * @date 2021-12-08
 * @since TODO
 */
public class ServiceTaskListener1 implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println(execution.getEventName());
        System.out.println(execution.getProcessDefinitionId());
        System.out.println(execution.getProcessInstanceId());

        // 设置流程变量
        execution.setVariable("aa", "bb");
    }
}
