package com.deo.activitimook.controller;

import com.deo.activitimook.SecurityUtil;
import com.deo.activitimook.pojo.UserInfoBean;
import com.deo.activitimook.util.AjaxResponse;
import com.deo.activitimook.util.GlobalConfig;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
