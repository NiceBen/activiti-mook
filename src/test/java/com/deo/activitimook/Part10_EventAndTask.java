package com.deo.activitimook;

import org.activiti.api.process.model.builders.StartMessagePayloadBuilder;
import org.activiti.api.process.model.payloads.StartMessagePayload;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * TODO
 *
 * @author SL Zhou
 * @date 2021-12-08
 * @since TODO
 */
@SpringBootTest
public class Part10_EventAndTask {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ProcessRuntime processRuntime;

    @Test
    public void signalStart() {
        runtimeService.signalEventReceived("Signal_0mrh702");
    }

    @Test
    public void msgBack() {
        Execution exec = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("Message_2ptchqd")
                .processInstanceId("f6d2ccf0-57e5-11ec-93d5-00ff2b3382c5")
                .singleResult();

        runtimeService.messageEventReceived("Message_2ptchqd", exec.getId());

        // 在开始节点启动流程实例，当前 M4 版本会报错。

//        runtimeService.startProcessInstanceByMessage("Message_2ptchqd");

//        processRuntime.start(StartMessagePayloadBuilder
//                .start("Message_2ptchqd")
//                .build());

    }

}
