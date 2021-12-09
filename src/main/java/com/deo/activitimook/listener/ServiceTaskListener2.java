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
public class ServiceTaskListener2 implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {

        // 设置流程变量
        Object aa = execution.getVariable("aa");
        System.out.println("aa=" + aa.toString());
    }
}
