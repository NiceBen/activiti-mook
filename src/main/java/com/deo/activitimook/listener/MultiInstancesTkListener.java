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
public class MultiInstancesTkListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String assignee = delegateTask.getAssignee();
        System.out.println("设置的默认执行人：" + assignee);

        // 根据任务节点逻辑查询实际需要的执行人是谁
        delegateTask.setAssignee("wukong");
    }
}
