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

    public static final Boolean IS_TEST = true;

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
