package com.deo.activitimook;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/20 上午3:18
 * @since TODO
 */
@SpringBootTest
public class Part2_ProcessDefinition {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 查询流程定义
     */
    @Test
    public void getDefinitions() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()
                .list();

        list.forEach(pd -> {
            System.out.println("getName:" + pd.getName());
            System.out.println("getKey:" + pd.getKey());
            System.out.println("getResourceName:" + pd.getResourceName());
            System.out.println("getDeploymentId:" + pd.getDeploymentId());
            System.out.println("getVersion:" + pd.getVersion());
            System.out.println("---------------");
        });
    }

    /**
     * 删除流程定义
     * ab312797-4858-11ec-a6da-acde48001122
     */
    @Test
    public void delDefinition() {

        String pdID = "ab312797-4858-11ec-a6da-acde48001122";
        repositoryService.deleteDeployment(pdID, true);// 如果为true则删除当前流程定义下的所有实例。生产环境中一般设置为false，用于追责
        System.out.println("删除流程定义成功！");
    }



}
