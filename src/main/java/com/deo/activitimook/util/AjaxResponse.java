package com.deo.activitimook.util;

import static com.deo.activitimook.util.GlobalConfig.ResponseCode.ERROR;
import static com.deo.activitimook.util.GlobalConfig.ResponseCode.SUCCESS;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/28 下午12:47
 * @since TODO
 */
public class AjaxResponse {

    // 状态码
    private Integer status;
    // 执行信息
    private String msg;
    // 返回值
    private Object obj;

    // 这里将构造方法修改为私有的构造方法
    private AjaxResponse(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    // 该方法用于对外共用，所以本身的 构造方法，可以修改为私有的
    public static AjaxResponse AjaxData(Integer status, String msg, Object obj) {
        return new AjaxResponse(status, msg, obj);
    }

    public static AjaxResponse success() {
        return success(null);
    }

    public static AjaxResponse success(Object obj) {
        return new AjaxResponse(SUCCESS.getCode(), SUCCESS.getDesc(), obj);
    }

    public static AjaxResponse error(Object obj) {
        return new AjaxResponse(ERROR.getCode(), ERROR.getDesc(), obj);
    }

    public static AjaxResponse error(String msg, Object obj) {
        return new AjaxResponse(ERROR.getCode(), msg, obj);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
