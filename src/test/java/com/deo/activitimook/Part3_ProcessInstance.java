package com.deo.activitimook;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/21 下午11:52
 * @since TODO
 */
@SpringBootTest
public class Part3_ProcessInstance {

    @Autowired
    private RuntimeService runtimeService;


    /**
     * 初始化流程实例
     */
    @Test
    public void initProcessInstance() {
        /*
         1.获取页面表单填表的内容，请假时间，请假事由，String fromData
         2.fromData 写入业务表，返回业务表主键ID==businessKey
         3.把业务数据与Activiti7流程数据关联
         */

        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myProcess_claim", "bKey002");

        System.out.println("流程实例ID:" + processInstance.getProcessInstanceId());
    }

    /**
     * 获取流程实例列表
     */
    @Test
    public void getProcessInstances() {
        List<ProcessInstance> list = runtimeService.
                createProcessInstanceQuery().list();
        for (ProcessInstance pi : list) {
            System.out.println("-----流程实例-----");
            System.out.println("getProcessInstanceId:" + pi.getProcessInstanceId());
            System.out.println("getProcessDefinitionId:" + pi.getProcessDefinitionId());
            System.out.println("isEnded:" + pi.isEnded());
            System.out.println("isSuspended:" + pi.isSuspended());
        }
    }

    /**
     * 暂停与激活流程实例
     * 33d4684e-4ae4-11ec-aff6-acde48001122
     */
    @Test
    public void activateProcessInstance() {
        String psId = "33d4684e-4ae4-11ec-aff6-acde48001122";
//        runtimeService.suspendProcessInstanceById(psId);
//        System.out.println("挂起流程实例!");

        runtimeService.activateProcessInstanceById(psId);
        System.out.println("激活流程实例！");
    }

    /**
     * 删除流程实例
     */
    @Test
    public void delProcessInstance() {
        String psId = "33d4684e-4ae4-11ec-aff6-acde48001122";
        runtimeService.deleteProcessInstance(psId, "删除测试");
        System.out.println("删除流程实例！");
        // 对于多次删除，实际项目中，应该允许，并且不会报错。但是activiti删除已删除的会报错，实际项目中应该通过try-catch捕获异常
    }
}
