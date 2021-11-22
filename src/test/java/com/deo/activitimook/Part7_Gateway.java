package com.deo.activitimook;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/22 下午2:19
 * @since TODO
 */
@SpringBootTest
public class Part7_Gateway {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 启动流程实例
     * myProcess_Parallel
     */
    @Test
    public void initProcessInstance() {
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myProcess_Inclusive", "bKey003");

        System.out.println("流程实例ID:" + processInstance.getProcessInstanceId());
    }

    /**
     * 完成任务
     * 9a5977f8-4b64-11ec-8148-acde48001122
     */
    @Test
    public void completeTask() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("day", "1");
        taskService.complete("5b6f09ab-4b6b-11ec-9230-acde48001122", variables);
        System.out.println("完成任务");
    }


}
