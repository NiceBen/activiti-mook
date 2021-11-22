package com.deo.activitimook;

import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/22 下午6:00
 * @since TODO
 */
@SpringBootTest
public class Part9_TaskRuntime {

    @Autowired
    private TaskRuntime taskRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 获取当前登录用户任务
     */
    @Test
    public void getTasks() {
        /*
            1.这行登录代码，实际上如果使用了 Spring Security 框架之后，在登录用户成功之后，就自动可以了。
            2.每次调用 ProcessRuntime 或者 TaskRuntime 时，都会自动获取当前登录用户。
         */
        securityUtil.logInAs("wukong");
        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 100));
        List<Task> list = tasks.getContent();
        for (Task tk : list) {
            System.out.println("------------");
            System.out.println("getId:" + tk.getId());
            System.out.println("getName:" + tk.getName());
            System.out.println(":getStatus" + tk.getStatus());
            System.out.println("getCreatedDate:" + tk.getCreatedDate());

            // 判断当前任务是执行人，还是候选人
            if (tk.getAssignee() == null) {
                // 候选人为当前登录用户，null的时候需要前端拾取
                System.out.println("Assignee:带拾取任务");
            } else {
                System.out.println("Assignee:" + tk.getAssignee());
            }
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        securityUtil.logInAs("wukong");
        Task task = taskRuntime.task("b7c5ed52-4b3e-11ec-b993-acde48001122");
        if (task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim()
                    .withTaskId(task.getId())
                    .build());
        }
        taskRuntime.complete(TaskPayloadBuilder.complete()
                .withTaskId(task.getId()).build());
        System.out.println("任务执行完成");
    }
}
