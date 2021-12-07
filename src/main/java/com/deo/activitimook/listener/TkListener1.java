package com.deo.activitimook.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 任务监听器
 * 通过任务监听器获取到当前执行人，可以根据执行人进行相关操作
 *
 * @date 2021-12-07
 * @since TODO
 */
public class TkListener1 implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String assignee = delegateTask.getAssignee();

        // 根据用户名查询用户，并调用发送短信接口
        System.out.println("执行人：" + assignee);

        // 可以在当前流程设置参数
        delegateTask.setVariable("delegateAssignee", assignee);



    }
}
