package com.deo.activitimook;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/22 上午1:01
 * @since TODO
 */
@SpringBootTest
public class Part5_HistoricTaskInstance {

    @Autowired
    private HistoryService historyService;

    /**
     * 根据用户名查询历史记录
     */
    @Test
    public void HistoricTaskInstanceByUser() {
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .taskAssignee("bajie")
                .orderByHistoricTaskInstanceEndTime().asc()
                .list();
        for (HistoricTaskInstance hi : list) {
            System.out.println("----分隔符---");
            System.out.println("getId:" + hi.getId());
            System.out.println("getProcessInstanceId:" + hi.getProcessInstanceId());
            System.out.println("getName:" + hi.getName());
        }
    }

    /**
     * 根据流程实例ID查询历史
     * 0ea0c55a-4aeb-11ec-969d-acde48001122
     */
    @Test
    public void HistoricTaskInstanceByPiID() {
        List<HistoricTaskInstance> list = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId("0ea0c55a-4aeb-11ec-969d-acde48001122")
                .orderByHistoricTaskInstanceEndTime().asc()
                .list();
        for (HistoricTaskInstance hi : list) {
            System.out.println("----分隔符---");
            System.out.println("getId:" + hi.getId());
            System.out.println("getProcessInstanceId:" + hi.getProcessInstanceId());
            System.out.println("getName:" + hi.getName());
        }
    }
}
