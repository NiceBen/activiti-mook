package com.deo.activitimook.listener;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * TODO
 *
 * @author SL Zhou
 * @date 2021-12-08
 * @since TODO
 */
public class ErrorServiceTaskListener implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        throw new BpmnError("Error_3hnv5od");
    }
}
