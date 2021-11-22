package com.deo.activitimook;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/18 下午6:10
 * @since TODO
 */
@SpringBootTest
public class Part1_Deployment {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 通过 BPMN 部署流程
     */
    @Test
    public void initDeploymentBPMN() {

        String fileName = "BPMN/Part8_ProcessRuntime.bpmn";
//        String pngName = "BPMN/Part1_Deployment.png";
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(fileName)
//                .addClasspathResource(pngName)  // 图片
                .name("流程部署测试-ProcessRuntime")
                .deploy();
        System.out.println("部署是否成功：" + deployment.getName());
    }

    /**
     * 通过 ZIP 部署流程
     */
    @Test
    public void initDeploymentZIP() {
        String zipName = "BPMN/Part1_DeploymentV2.zip";
        InputStream fis = this.getClass().getClassLoader()
                .getResourceAsStream(zipName);
        ZipInputStream zip = new ZipInputStream(fis);

        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zip)
                .name("流程部署测试zip")
                .deploy();
        System.out.println("部署是否成功：" + deployment.getName());
    }

    /**
     * 查询流程部署
     */
    @Test
    public void testGetDeployments() {
        List<Deployment> list = repositoryService.createDeploymentQuery()
                .list();

        list.forEach(dep -> {
            System.out.println("ID:" + dep.getId());
            System.out.println("Name:" + dep.getName());
            System.out.println("DeploymentTime:" + dep.getDeploymentTime());
            System.out.println("Key:" + dep.getKey());
            System.out.println("---------");
        });
    }



}
