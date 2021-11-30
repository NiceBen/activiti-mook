package com.deo.activitimook.controller;

import com.deo.activitimook.SecurityUtil;
import com.deo.activitimook.mapper.ActivitiMapper;
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
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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
    private RepositoryService repositoryService;

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private ActivitiMapper activitiMapper;

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
                    .withVariable("audit", "1") // 完成任务是会添加参数
                    .withVariable("isFirst", "1") // 完成任务是会添加参数
                    .build());

            return AjaxResponse.success();
        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("完成任务：" + taskID + "失败");
        }
    }

    // 渲染动态表单
    // 备注：每次在点击“完成任务前”，执行查询是否有“动态表单渲染”
    @GetMapping(value = "/formDataShow")
    public AjaxResponse formDataShow(@RequestParam("taskID") String taskID) {
        final String CUSTOM_SEPARATOR = "-_!";
        try {
            if (GlobalConfig.IS_TEST) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskID);

            UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                    .getFlowElement(task.getFormKey());

            // 这里进行判断，userTask 如果为null，则完成任务的点击按钮，不需要弹出表单
            if (userTask == null) {
                return AjaxResponse.success("无表单");
            }

            /*
                这里通过定义bpmn中Task的form属性，通过自定义分隔符，来实现传递各个类型的参数
             */
            List<FormProperty> formProperties = userTask.getFormProperties();
            List<HashMap<String, Object>> listMap = new ArrayList<>();
            for (FormProperty fp : formProperties) {
                String idStr = fp.getId();
                HashMap<String, Object> hashMap = new HashMap<>();
                String[] splitFP = idStr.split(CUSTOM_SEPARATOR, -1);
                hashMap.put("id", splitFP[0]);
                hashMap.put("controlType", splitFP[1]);
                hashMap.put("controlLabel", splitFP[2]);
                hashMap.put("controlDefValue", splitFP[3]);
                hashMap.put("controlParam", splitFP[4]);
                listMap.add(hashMap);
            }

            return AjaxResponse.success(listMap);
        } catch (Exception e) {
            return AjaxResponse.error("表单动态渲染失败", e.getMessage());
        }
    }

    /*
        后期需要针对 “formData” 参数进行优化，这里是通过一个将参数拼接成一个字符串，然后通过解析字符串，来进行操作
        示例：formData:控件id-_!控件值-_!是否参数!_!控件id-_!控件值-_!是否参数
        ①将字符传修改为 List<Map<String, String>> listMap （不推荐）
        ②按照字符List<Bo> listBo， Bo 中包含“控件id”，“控件值”，“是否参数”等属性
     */
    // 保存动态表单
    @PostMapping(value = "/formDataSave")
    public AjaxResponse formDataSave(@RequestParam("taskID") String taskID,
                                     @RequestParam("formData") String formData) {
        final String OUTER_CUSTOM_SEPARATOR = "!_!";
        final String INNER_CUSTOM_SEPARATOR = "-_!";
        try {
            if (GlobalConfig.IS_TEST) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskID);

            // 保存流程变量
            HashMap<String, Object> variable = new HashMap<>();
            // 是否有流程变量
            boolean hasVariables = false;

            // 前端传来的字符串拆分为控件组
            String[] formDataList = formData.split(OUTER_CUSTOM_SEPARATOR);
            List<HashMap<String, Object>> listMap = new ArrayList<>();
            String pdID = task.getProcessDefinitionId();
            String piId = task.getProcessInstanceId();
            String formKey = task.getFormKey();
            for (String controlItem : formDataList) {
                String[] formDataItem = controlItem.split(INNER_CUSTOM_SEPARATOR);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("PROC_DEF_ID_", pdID);
                hashMap.put("PROC_INST_ID_", piId);
                hashMap.put("FORM_KEY_", formKey);
                hashMap.put("Control_ID_", formDataItem[0]);
                hashMap.put("Control_VALUE_", formDataItem[1]);
//                hashMap.put("Control_PARAM_", formDataItem[2]);
                listMap.add(hashMap);

                // 这里需要进行判断，当前参数是否需要作为流程变量
                switch (formDataItem[2]) {
                    case "f":
                        System.out.println("控件值不作为参数！");
                        break;
                    case "s":
                        variable.put(formDataItem[0], formDataItem[1]);
                        hasVariables = true;
                        break;
                    case "t":
                        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        variable.put(formDataItem[0], timeFormat.parse(formDataItem[1]));
                        hasVariables = true;
                        break;
                    case "b":
                        variable.put(formDataItem[0], BooleanUtils.toBoolean(formDataItem[1]));
                        hasVariables = true;
                        break;
                    default:
                        System.out.println("控件ID：" + formDataItem[0] + "的参数" + formDataItem[2] + "不存在");
                }
            }

            // 1.完成任务
            if (hasVariables) {
                // 带有参数
                taskRuntime.complete(TaskPayloadBuilder.complete()
                        .withTaskId(taskID)
                        .withVariables(variable)
                        .build());
            } else {
                taskRuntime.complete(TaskPayloadBuilder.complete()
                        .withTaskId(taskID)
                        .build());
            }

            // 2.将前端传递过来的 form 表单数据，进行持久化
            int result = activitiMapper.insertFormData(listMap);

            return AjaxResponse.success("执行成功，保存" + result + "条数据");
        } catch (Exception e) {
            return AjaxResponse.error("表单动态渲染失败", e.getMessage());
        }
    }


//     自定义-获取下一个节点(暂不设置)


}
