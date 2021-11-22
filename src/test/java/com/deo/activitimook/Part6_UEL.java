package com.deo.activitimook;

import com.deo.activitimook.pojo.UEL_POJO;
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
 * @date 2021/11/22 上午10:00
 * @since TODO
 */
@SpringBootTest
public class Part6_UEL {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 启动流程实例带参数，执行执行人
     */
    @Test
    public void initProcessInstanceWithArgs() {
        // 流程变量${ZhiXingRen}
        Map<String, Object> variables = new HashMap<>();
        variables.put("ZhiXingRen", "wukong");
//        variables.put("aa", "bb");

        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(
                        "myProcess_UEL_V1",
                        "bKey002",
                        variables);

        System.out.println("流程实例ID:" + processInstance.getProcessInstanceId());
    }

    /**
     * 完成任务带参数，指定流程变量
     * myProcess_UEL_V2
     */
    @Test
    public void completeTaskWithArgs() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("pay", "101");
//        variables.put("aa", "bb");
        taskService.complete("283c639e-4b3c-11ec-9b32-acde48001122", variables);
        System.out.println("完成任务");

    }

    /**
     * 启动流程实例带参数，使用实体类
     *
     */
    @Test
    public void initProcessInstanceWithClassArgs() {
        UEL_POJO uel_pojo = new UEL_POJO();
        uel_pojo.setZhixingren("bajie");

        Map<String, Object> variables = new HashMap<>();
        variables.put("uelpojo", uel_pojo);
//        variables.put("aa", "bb");




        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(
                        "myProcess_UEL_V3",
                        "bKey002",
                        variables);

        System.out.println("流程实例ID:" + processInstance.getProcessInstanceId());
    }

    /**
     * 任务完成环节带参数，指定多个候选人
     *
     * 25ee6a00-4b3e-11ec-881e-acde48001122
     */
    @Test
    public void initProcessInstanceWithCandidateArgs() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("houxuanren", "wukong,tangseng");
        taskService.complete("25ee6a00-4b3e-11ec-881e-acde48001122", variables);
        System.out.println("完成任务");
    }

    /**
     * 直接指定流程变量（全局）
     */
    @Test
    public void otherArgs() {
        runtimeService.setVariable("25ee6a00-4b3e-11ec-881e-acde48001122", "pay", "101");
//        runtimeService.setVariables();
//        taskService.setVariable();
//        taskService.setVariables();
    }

    /**
     * 局部变量
     */
    @Test
    public void otherLocalArgs() {
        runtimeService.setVariableLocal("25ee6a00-4b3e-11ec-881e-acde48001122", "pay", "101");
//        runtimeService.setVariablesLocal();
//        taskService.setVariableLocal();
//        taskService.setVariablesLocal();
    }
}
