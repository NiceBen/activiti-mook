package com.deo.activitimook.controller;

import com.deo.activitimook.util.AjaxResponse;
import com.deo.activitimook.util.GlobalConfig;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipInputStream;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/28 下午1:04
 * @since TODO
 */
@RestController
@RequestMapping("/processDefinition")
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;

    // 添加流程定义-通过上传bpmn
    @PostMapping(value = "/uploadStreamAndDeployment")
    public AjaxResponse uploadStreamAndDeployment(@RequestParam("processFile") MultipartFile multipartFile
//            ,@RequestParam("deploymentName") String deploymentName
    ) {
        try {
            // 获取上传文件名
            String fileName = multipartFile.getOriginalFilename();
            // 获取文件扩展名
            String extension = FilenameUtils.getExtension(fileName);
            // 获取文件字节流对象
            InputStream fileInputStream = multipartFile.getInputStream();

            Deployment deployment = null;

            if (extension.equals("zip")) {
                // 上传的是zip文件
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment()
                        .addZipInputStream(zip)
//                        .name(deploymentName)
                        .deploy();
            } else {
                // 上传的是bpmn文件
                deployment = repositoryService.createDeployment()
                        .addInputStream(fileName, fileInputStream)
//                        .name(deploymentName)
                        .deploy();
            }

            return AjaxResponse.success(deployment.getId() + ";" + fileName);

        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("部署流程失败", e.toString());
        }
    }


    // 添加流程定义-通过在线提交bpmn的XML
    @PostMapping(value = "/addDeploymentByString")
    public AjaxResponse addDeploymentByString(@RequestParam("stringBPMN") String stringBPMN
//            ,@RequestParam("deploymentName") String deploymentName        // 真实的名称可以通过xml，进行定义
    ) {
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .addString("CreateWithBPMNJS.bpmn", stringBPMN)
                    .name("这里不需要传递名称")
                    .deploy();

            return AjaxResponse.success(deployment.getId());
        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("string 部署流程失败", e.toString());
        }
    }

    // 获取流程定义列表
    @GetMapping(value = "/getDefinitions")
    public AjaxResponse getDefinitions() {
        try {
            List<HashMap<String, Object>> listMap = new ArrayList<>();

            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
            for (ProcessDefinition pd : list) {
                HashMap<String, Object> hashMap = new HashMap<>();

                //System.out.println("流程定义ID："+pd.getId());
                hashMap.put("processDefinitionID", pd.getId());
                hashMap.put("name", pd.getName());
                hashMap.put("key", pd.getKey());
                hashMap.put("resourceName", pd.getResourceName());
                hashMap.put("deploymentID", pd.getDeploymentId());
                hashMap.put("version", pd.getVersion());
                listMap.add(hashMap);
            }

            return AjaxResponse.success(listMap);

        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("获取流程定义失败", e.toString());
        }
    }


    // 获取流程定义XML
    @GetMapping(value = "/getDefinitionXML")
    public void getDefinitionXML(HttpServletResponse response,
                                         @RequestParam("deploymentId") String deploymentId,
                                         @RequestParam("resourceName") String resourceName) {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取流程部署列表
    @GetMapping(value = "/getDeployments")
    public AjaxResponse getDeployments() {
        try {
            List<HashMap<String, Object>> listMap = new ArrayList<>();

            List<Deployment> list = repositoryService.createDeploymentQuery().list();

            for (Deployment dep : list) {
                HashMap<String, Object> hashMap = new HashMap<>();

                hashMap.put("id", dep.getId());
                hashMap.put("name", dep.getName());
                hashMap.put("deploymentTime", dep.getDeploymentTime());
                /*
                这里核心的 deploymentTime 数据在流程部署中，如果想要将流程定义与流程部署结合起来，做法有三种：
                1.在流程定义的for循环中，再去查询流程部署（不推荐）
                2.自己编写SQL语句，通过 join 将流程定义表和流程部署表进行关联（推荐）
                3.在同一个方法中，将流程定义list与流程部署list同步查询，进行关联（推荐）
                 */

                listMap.add(hashMap);
            }

            return AjaxResponse.success(listMap);

        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("获取流程部署列表失败", e.toString());
        }
    }

    // 删除流程定义
    @GetMapping(value = "/delDefinition")
    public AjaxResponse delDefinition(@RequestParam("depID") String depID) {
        try {
            repositoryService.deleteDeployment(depID, true);

            return AjaxResponse.success();
        } catch (Exception e) {
            // 这里具体的错误信息返回给客户，前端展示msg，但是接口同步展示真正错误原因
            return AjaxResponse.error("删除流程定义失败", e.toString());
        }
    }

    // 上传文件(所有文件，包含BPMN)
    @PostMapping(value = "/upload")
    public AjaxResponse upload(HttpServletRequest request,
                               @RequestParam("processFile") MultipartFile multipartFile) {
        try {
            if (multipartFile.isEmpty()) {
                System.out.println("文件为空");
            }
            String fileName = multipartFile.getOriginalFilename();//文件名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));//后缀名
            String filePath = GlobalConfig.WINDOWS_BPMN_PATH_MAPPING;
            // 修改路径格式
            filePath = filePath.replace("\\", "/");
            filePath = filePath.replace("file:", "");


            String prefixName = fileName.substring(0, fileName.lastIndexOf("."));   // 前缀名

            fileName = prefixName + "-" + UUID.randomUUID() + suffixName;// 新的文件名
            File file = new File(filePath + fileName);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            try {
                multipartFile.transferTo(file);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            return AjaxResponse.success(fileName);
        } catch (Exception e) {
            return AjaxResponse.error("上传文件失败", e.toString());
        }



    }



}
