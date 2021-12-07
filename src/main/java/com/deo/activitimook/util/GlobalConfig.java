package com.deo.activitimook.util;

/**
 * TODO
 * 1.这里编写了返回值编码类
 * 2.定义是否为测试
 *
 * @author NiceBen
 * @date 2021/11/28 下午12:41
 * @since TODO
 */
public class GlobalConfig {

    public static final Boolean IS_TEST = false;

    // Windows 发布路径
    public static final String WINDOWS_BPMN_PATH_MAPPING = "file:D:\\IntelliJIDEA\\workspace\\others\\activiti-mook-12\\src\\main\\resources\\resources\\bpmn\\";

    // Linux 发布路径
//    public static final String LINUX_BPMN_PATH_MAPPING = "file:/root/activiti/bpmn";

    public enum ResponseCode {
        SUCCESS(0, "成功"),
        ERROR(1, "错误");

        private final int code;
        private final String desc;

        ResponseCode(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
