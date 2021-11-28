package com.deo.activitimook.controller;

import com.deo.activitimook.SecurityUtil;
import com.deo.activitimook.util.AjaxResponse;
import com.deo.activitimook.util.GlobalConfig;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.CompleteTaskPayloadBuilder;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.model.payloads.CompleteTaskPayload;
import org.activiti.api.task.runtime.TaskRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/28 下午11:29
 * @since TODO
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    // 获取我的待办
    @GetMapping(value = "/getTasks")
    public AjaxResponse getTasks() {
        try {
            if (GlobalConfig.IS_TEST) {
                securityUtil.logInAs("bajie");
            }

            Page<Task> page = taskRuntime.tasks(Pageable.of(0, 100));
            List<HashMap<String, Object>> listMap = new ArrayList<>();

            List<Task> list = page.getContent();
            for (Task tk : list) {
                HashMap<String, Object> map = new HashMap<>();

                map.put("id", tk.getId());
                map.put("name", tk.getName());
                map.put("status", tk.getStatus());
                map.put("createDate", tk.getCreatedDate());
                map.put("assignee", tk.getAssignee()==null?"待拾取任务":tk.getAssignee());

                // 获取流程实例的名称
                ProcessInstance pi = processRuntime.processInstance(tk.getProcessInstanceId());
                map.put("instanceName", pi.getName());

                listMap.add(map);
            }

            return AjaxResponse.success(listMap);
        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("获取我的待办任务失败", e.toString());
        }
    }

    // 完成任务
    @GetMapping(value = "/completeTask")
    public AjaxResponse completeTask(@RequestParam("taskID") String taskID) {
        try {
            if (GlobalConfig.IS_TEST) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskID);
            // 如果为候选人，先拾取，再完成；否则直接完成
            if (task.getAssignee() == null) {
                taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
            }
            taskRuntime.complete(TaskPayloadBuilder.complete()
                    .withTaskId(task.getId())
//                    .withVariable("num", "2") // 完成任务是会添加参数
                    .build());

            return AjaxResponse.success();
        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("完成任务：" + taskID + "失败");
        }
    }

    // 渲染动态表单

    // 保存动态表单



}
