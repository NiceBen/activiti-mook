package com.deo.activitimook.controller;

import com.deo.activitimook.SecurityUtil;
import com.deo.activitimook.pojo.UserInfoBean;
import com.deo.activitimook.util.AjaxResponse;
import com.deo.activitimook.util.GlobalConfig;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/28 下午11:53
 * @since TODO
 */
@RestController
@RequestMapping("/activitiHistory")
public class ActivitiHistoryController {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private HistoryService historyService;

    // 仓储对象，用于流程读取
    @Autowired
    private RepositoryService repositoryService;


    // 用户历史任务
    @GetMapping(value = "/getInstancesByUserName")
    public AjaxResponse getInstancesByUserName(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().desc()
                    .taskAssignee(userInfoBean.getUsername())
                    .list();

            return AjaxResponse.success(historicTaskInstances);

        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("获取用户历史任务失败", e.toString());
        }
    }

    // 根据流程实例ID查询任务
    @GetMapping(value = "/getInstancesByPiID")
    public AjaxResponse getInstancesByPiID(@RequestParam("piID") String piID) {
        try {
            List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceEndTime().desc()
                    .processInstanceId(piID)
                    .list();

            return AjaxResponse.success(historicTaskInstances);

        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("获取用户历史任务失败", e.toString());
        }
    }

    // 高亮显示流程历史
    // 98bfd61d-526b-11ec-9a57-00ff2b3382c5
    // getHighLine
    @GetMapping(value = "/getHighLine")
    public AjaxResponse getHighLine(@RequestParam("instanceID") String instanceID, @AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(instanceID).singleResult();
            // 1.通过 RepositoryService 来获取 BpmnModel
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            Process process = bpmnModel.getProcesses().get(0);

            // 2.获取所有流程 FlowElement 的信息
            Collection<FlowElement> flowElements = process.getFlowElements();
            HashMap<String, String> map = new HashMap<>();
            for (FlowElement flowElement : flowElements) {
                // 判断是否为线条
                if (flowElement instanceof SequenceFlow) {
                    SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                    String ref = sequenceFlow.getSourceRef();
                    String targetRef = sequenceFlow.getTargetRef();
                    map.put(ref + targetRef, sequenceFlow.getId());
                }
            }

            // 3.获取流程实例 历史节点（全部）
            List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceID)
                    .list();
            // 各个历史节点   两两组合 key
            Set<String> keyList = new HashSet<>();
            for (HistoricActivityInstance i : list) {
                for (HistoricActivityInstance j : list) {
                    if (i != j) {
                        keyList.add(i.getActivityId() + j.getActivityId());
                    }
                }
            }

            // 高亮连线ID
            Set<String> highLine = new HashSet<>();
            keyList.forEach(s -> highLine.add(map.get(s)));

            // 4.获取已经完成的节点
            List<HistoricActivityInstance> listFinished = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceID)
                    .finished()
                    .list();
            // 已经完成的节点高亮
            Set<String> highPoint = new HashSet<>();
            listFinished.forEach(s -> highPoint.add(s.getActivityId()));

            // 5.获取待办的节点
            List<HistoricActivityInstance> listUnFinished = historyService.createHistoricActivityInstanceQuery()
                    .processInstanceId(instanceID)
                    .unfinished()
                    .list();
            // 待办的节点高亮
            Set<String> waitingToDO = new HashSet<>();
            listUnFinished.forEach(s -> waitingToDO.add(s.getActivityId()));

            // 6.当前用户完成的任务
            String AssigneeName = null;
            if (GlobalConfig.IS_TEST) {
                AssigneeName = "bajie";
            } else {
                AssigneeName = userInfoBean.getUsername();
            }
            List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                    .taskAssignee(AssigneeName)
                    .processInstanceId(instanceID)
                    .finished()
                    .list();
            // 我完成的节点
            Set<String> iDo = new HashSet<>();
            taskInstanceList.forEach(s -> iDo.add(s.getTaskDefinitionKey()));

            // 7.拼接返回值
            HashMap<String, Object> reMap = new HashMap<>();
            reMap.put("highLine", highLine);
            reMap.put("highPoint", highPoint);
            reMap.put("waitingToDO", waitingToDO);
            reMap.put("iDo", iDo);




            return AjaxResponse.success(reMap);
        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("获取高亮历史任务失败", e.toString());
        }
    }
}
