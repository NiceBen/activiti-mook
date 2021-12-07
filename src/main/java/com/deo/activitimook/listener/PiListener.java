package com.deo.activitimook.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;

/**
 * 执行监听器，请对比“任务监听器”
 *
 * @date 2021-12-07
 * @since TODO
 */
public class PiListener implements ExecutionListener {

    private Expression sendType;

    @Override
    public void notify(DelegateExecution execution) {
        System.out.println("任务名称：" + execution.getEventName());
        System.out.println("节点id名称：" + execution.getCurrentActivityId());
        System.out.println("流程实例ID" + execution.getProcessInstanceId());

        if ("start".equals(execution.getEventName())) {
            // 记录节点开始时间，通过创建一张表记录，流程实例ID，节点id，开始时间，结束时间
        } else if ("end".equals(execution.getEventName())) {
            // 记录节点结束时间
        }

        System.out.println("sendType:" + sendType.getValue(execution).toString());
    }
}
