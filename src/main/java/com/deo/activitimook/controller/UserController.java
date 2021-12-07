package com.deo.activitimook.controller;

import com.deo.activitimook.mapper.ActivitiMapper;
import com.deo.activitimook.util.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * TODO
 *
 * @date 2021-12-01
 * @since TODO
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ActivitiMapper activitiMapper;

    /*
        -- 查询流程定义数
        SELECT COUNT(ID_) FROM `act_re_procdef`;

        -- 查询流程实例数
        SELECT COUNT(DISTINCT(PROC_INST_ID_)) FROM act_ru_execution;

        -- 查询每个流程定义下的实例数
        SELECT rp.NAME_, COUNT(DISTINCT(PROC_INST_ID_)) AS COUNT_ FROM act_ru_execution AS ex RIGHT JOIN act_re_procdef rp ON ex.PROC_DEF_ID_ = rp.ID_ WHERE rp.NAME_ IS NOT NULL GROUP BY rp.NAME_
     */
    // 获取用户列表
    @GetMapping("/getUsers")
    public AjaxResponse getUsers() {
        try {
            List<HashMap<String, Object>> userList = activitiMapper.selectUser();
            return AjaxResponse.error(userList);
        } catch (Exception e) {
            return AjaxResponse.error("获取用户列表失败", e.toString());
        }
    }
}
