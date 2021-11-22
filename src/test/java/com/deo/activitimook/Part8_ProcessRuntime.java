package com.deo.activitimook;

import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/22 下午5:29
 * @since TODO
 */
@SpringBootTest
public class Part8_ProcessRuntime {

    /*
    1.如果你的项目没有使用 Spring Security 安全框架，那么直接使用之前各种 Service API 进行操作即可；
    2.如果你的项目使用了 Spring Security 安全框架，可以使用Activiti7新提供的API进行开发，即ProcessRuntime
     */

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 获取流程实例
     */
    @Test
    public void getProcessInstance() {
        // 先登录
        securityUtil.logInAs("bajie");
        Page<ProcessInstance> processInstancePage = processRuntime
                .processInstances(Pageable.of(0, 100));
        System.out.println("流程实例数量：" + processInstancePage.getTotalItems());
        List<ProcessInstance> list = processInstancePage.getContent();
        for (ProcessInstance pi : list) {
            System.out.println("----------分隔符----------");
            System.out.println("getId：" + pi.getId());
            System.out.println("getName：" + pi.getName());
            System.out.println("getStartDate：" + pi.getStartDate());
            System.out.println("getStatus：" + pi.getStatus());
            System.out.println("getProcessDefinitionId：" + pi.getProcessDefinitionId());
            System.out.println("getProcessDefinitionKey：" + pi.getProcessDefinitionKey());
        }
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance() {
        // 先登录
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime
                .start(ProcessPayloadBuilder.start()
                                .withProcessDefinitionKey("myProcess_ProcessRuntime")
                                .withName("第一个流程实例的名称")
//                                .withVariable("", "")
                                .withBusinessKey("自定义bKey")
                                .build()
                );
        System.out.println("通过ProcessRuntime，启动流程实例成功！");
    }

    /**
     * 删除流程实例
     */
    @Test
    public void delProcessInstance() {
        // 先登录
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime
                .delete(ProcessPayloadBuilder.delete()
                        .withProcessInstanceId("825d20ad-4b79-11ec-8e1b-acde48001122")
                                .build()
                );
        System.out.println("通过ProcessRuntime，删除流程实例成功！");
    }

    /**
     * 挂起流程实例
     */
    @Test
    public void suspendProcessInstance() {
        // 先登录
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime
                .suspend(ProcessPayloadBuilder.suspend()
                        .withProcessInstanceId("825d20ad-4b79-11ec-8e1b-acde48001122")
                        .build()
                );
        System.out.println("通过ProcessRuntime，挂机流程实例成功！");
    }

    /**
     * 激活流程实例
     */
    @Test
    public void resumeProcessInstance() {
        // 先登录
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime
                .resume(ProcessPayloadBuilder.resume()
                        .withProcessInstanceId("825d20ad-4b79-11ec-8e1b-acde48001122")
                        .build()
                );
        System.out.println("通过ProcessRuntime，激活流程实例成功！");
    }

    /**
     * 流程实例参数
     */
    @Test
    public void getVariables() {
        // 先登录
        securityUtil.logInAs("bajie");
        List<VariableInstance> list = processRuntime.variables(
                ProcessPayloadBuilder.variables()
                        .withProcessInstanceId("825d20ad-4b79-11ec-8e1b-acde48001122")
                        .build()
        );

        for (VariableInstance vi : list) {
            System.out.println("----分隔符----");
            System.out.println("getName:" + vi.getName());
            System.out.println("getValue:" + vi.getValue());
            System.out.println("getTaskId:" + vi.getTaskId());
            System.out.println("getProcessInstanceId:" + vi.getProcessInstanceId());
        }

        System.out.println("通过ProcessRuntime，查询流程参数成功！");
    }

}
