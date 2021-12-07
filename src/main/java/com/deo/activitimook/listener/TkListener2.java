package com.deo.activitimook.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 任务监听器
 * 通过任务监听器获取到当前执行人，可以根据执行人进行相关操作
 *
 * 模拟场景，第一个发了文章，由他对应的主管领导进行审核
 *
 * @date 2021-12-07
 * @since TODO
 */
public class TkListener2 implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String delegateAssignee = (String) delegateTask.getVariable("delegateAssignee");
        // 从全局变量中获取执行人
        System.out.println("执行人2：" + delegateAssignee);

        // 根据执行人 username，获取组织机构代码，加工后得到领导是wukong
        // 动态添加当前节点执行人
        delegateTask.setAssignee("wukong");


    }
}
