package com.deo.activitimook.controller;

import com.deo.activitimook.SecurityUtil;
import com.deo.activitimook.pojo.UserInfoBean;
import com.deo.activitimook.util.AjaxResponse;
import com.deo.activitimook.util.GlobalConfig;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
 * @date 2021/11/28 下午10:47
 * @since TODO
 */
@RestController
@RequestMapping("/processInstance")
public class ProcessInstanceController {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private ProcessRuntime processRuntime;


    /**
     * Security 中添加 @AuthenticationPrincipal 注解之后，就可以使用 Security 框架中的登录用户信息
     * @param userInfoBean
     * @return
     */
    // 查询流程实例
    @GetMapping(value = "/getInstances")
    public AjaxResponse getInstances(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        try {
            if (GlobalConfig.IS_TEST) {
                securityUtil.logInAs("bajie");
            }

            Page<ProcessInstance> page = processRuntime.processInstances(
                    Pageable.of(0, 100));
            List<ProcessInstance> list = page.getContent();
            list.sort((y,x) -> x.getStartDate().toString().compareTo(y.getStartDate().toString()));

            List<HashMap<String, Object>> listMap = new ArrayList<>();
            for (ProcessInstance pi : list) {
                HashMap<String, Object> hashMap = new HashMap<>();

                hashMap.put("id", pi.getId());
                hashMap.put("name", pi.getName());
                hashMap.put("status", pi.getStatus());
                hashMap.put("processDefinitionId", pi.getProcessDefinitionId());
                hashMap.put("processDefinitionKey", pi.getProcessDefinitionKey());
                hashMap.put("startDate", pi.getStartDate());
                hashMap.put("processDefinitionVersion", pi.getProcessDefinitionVersion());
                hashMap.put("businessKey", pi.getBusinessKey());

                // 因为 pi 没有历史高亮需要的 deploymentId 和资源Name，所以需要再次查询
                ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(pi.getProcessDefinitionId())
                        .singleResult();

                hashMap.put("deploymentId", pd.getDeploymentId());
                hashMap.put("resourceName", pd.getResourceName());
                listMap.add(hashMap);
            }

            return AjaxResponse.success(listMap);

        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("获取流程实例失败", e.toString());
        }
    }

    // 启动流程实例
    @GetMapping(value = "/startProcess")
    public AjaxResponse startProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                     @RequestParam("instanceName") String instanceName) {
        try {
            if (GlobalConfig.IS_TEST) {
                securityUtil.logInAs("bajie");
            } else {
                // 获取当前登录人（第三种方式）（第一种默认不用获取）（第二种直接通过注解参数获取）
                securityUtil.logInAs(SecurityContextHolder.getContext().getAuthentication().getName());
            }


            processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionKey(processDefinitionKey)
                    .withName(instanceName)
                    .withBusinessKey("自定义BusinessKey")
//                    .withVariable("参数name", "参数值")
                    .build());

            return AjaxResponse.success();
        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("启动流程实例失败", e.toString());
        }
    }

    // 挂起流程实例
    @GetMapping(value = "/suspendInstance")
    public AjaxResponse suspendInstance(@RequestParam("instanceID") String instanceID) {
        try {
            if (GlobalConfig.IS_TEST) {
                securityUtil.logInAs("bajie");
            }

            ProcessInstance pi = processRuntime.suspend(ProcessPayloadBuilder
                    .suspend()
                    .withProcessInstanceId(instanceID)
                    .build());

            return AjaxResponse.success(pi.getName());
        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("暂停流程实例失败", e.toString());
        }
    }

    // 激活重启流程实例
    @GetMapping(value = "/resumeInstance")
    public AjaxResponse resumeInstance(@RequestParam("instanceID") String instanceID) {
        try {
            if (GlobalConfig.IS_TEST) {
                securityUtil.logInAs("bajie");
            }

            ProcessInstance pi = processRuntime.resume(ProcessPayloadBuilder
                    .resume()
                    .withProcessInstanceId(instanceID)
                    .build());

            return AjaxResponse.success(pi.getName());
        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("激活流程实例失败", e.toString());
        }
    }

    // 删除流程实例
    @GetMapping(value = "/deleteInstance")
    public AjaxResponse deleteInstance(@RequestParam("instanceID") String instanceID) {
        try {
            if (GlobalConfig.IS_TEST) {
                securityUtil.logInAs("bajie");
            }

            ProcessInstance pi = processRuntime.delete(ProcessPayloadBuilder
                    .delete()
                    .withProcessInstanceId(instanceID)
                    .build());

            return AjaxResponse.success(pi.getName());
        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("删除流程实例失败", e.toString());
        }
    }

    // 查询流程参数
    @GetMapping(value = "/variables")
    public AjaxResponse variables(@RequestParam("instanceID") String instanceID) {
        try {
            if (GlobalConfig.IS_TEST) {
                securityUtil.logInAs("bajie");
            }

            List<VariableInstance> variables = processRuntime.variables(ProcessPayloadBuilder
                    .variables()
                    .withProcessInstanceId(instanceID)
                    .build());

            return AjaxResponse.success(variables);
        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("删除流程实例失败", e.toString());
        }
    }

}
