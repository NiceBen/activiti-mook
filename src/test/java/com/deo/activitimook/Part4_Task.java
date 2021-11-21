package com.deo.activitimook;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/22 上午12:22
 * @since TODO
 */
@SpringBootTest
public class Part4_Task {

    @Autowired
    private TaskService taskService;

    /**
     * 任务查询（所有:管理员）
     */
    @Test
    public void getTasks() {
        List<Task> list = taskService.createTaskQuery().list();
        for (Task tk : list) {
            System.out.println("------任务分隔符-----");
            System.out.println("getId:" + tk.getId());
            System.out.println("getName:" + tk.getName());
            System.out.println("getAssignee:" + tk.getAssignee());
        }
    }

    /**
     * 任务查询（我的代办任务）
     */
    @Test
    public void getTasksByAssignee() {
        String assignee = "bajie";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assignee).list();
        for (Task tk : list) {
            System.out.println("------任务分隔符-----");
            System.out.println("getId:" + tk.getId());
            System.out.println("getName:" + tk.getName());
            System.out.println("getAssignee:" + tk.getAssignee());
        }
    }

    /**
     * 执行任务
     *
     * Assignee
     */
    @Test
    public void completeTask() {
        String taskId = "7e9bfc95-4ae9-11ec-9bcf-acde48001122";
        taskService.complete(taskId);
        System.out.println("完成任务！");
    }

    /**
     * 拾取任务
     *
     * Candidate
     */
    @Test
    public void claimTask() {
        String candidateUser = "bajie";
        // 在Activiti7 之前可以通过taskCandidateUser()方法直接查询，但是7引入了Security，所以不能了
//        List<Task> list = taskService.createTaskQuery()
//                .taskCandidateUser(candidateUser)
//                .list();


        // 这里测试直接通过 TaskId，进行查询
        String taskId = "0ea46ede-4aeb-11ec-969d-acde48001122";
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        taskService.claim(taskId, candidateUser);
    }

    /**
     * 归还与交办任务
     */
    @Test
    public void setTaskAssignee() {
        String taskId = "0ea46ede-4aeb-11ec-969d-acde48001122";
        taskService.setAssignee(taskId, "null");    // 归还候选任务
        taskService.setAssignee(taskId, "wukong");    // 交办
    }

}
