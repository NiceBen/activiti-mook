package com.deo.activitimook.pojo;

import java.io.Serializable;

/**
 * TODO
 *
 * @author NiceBen
 * @date 2021/11/22 上午10:34
 * @since TODO
 */
public class UEL_POJO implements Serializable {
    private static final long serialVersionUID = 6684040209563045371L;

    private String zhixingren;

    private String pay;

    public String getZhixingren() {
        return zhixingren;
    }

    public void setZhixingren(String zhixingren) {
        this.zhixingren = zhixingren;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
