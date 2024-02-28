/*
 * Copyright (c) 2024 Trustwave Holdings, Inc.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information
 * of Trustwave Holdings, Inc.  Use of this software is governed by
 * the terms and conditions of the license statement and limited
 * warranty furnished with the software.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD TRUSTWAVE HOLDINGS INC.,
 * ITS RELATED COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST
 * ANY CLAIMS OR LIABILITIES ARISING OUT OF OR RESULTING FROM THE USE,
 * MODIFICATION, OR DISTRIBUTION OF PROGRAMS OR FILES CREATED FROM,
 * BASED ON, AND/OR DERIVED FROM THIS SOURCE CODE FILE.
 */
package com.trustwave.dbpworkflow.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * -- TODO add description here
 *
 * <pre>
 * Copyright (c) 2024 Trustwave Holdings, Inc.
 * All rights reserved.
 * </pre>
 *
 * @author sfreytag
 */
@Service("progressListener")
public class ProgressExecutionListener implements ExecutionListener {
    public static final String INDENT_STR = "   ";
    @Autowired RuntimeService runtimeService;

    @Override
    public void notify(DelegateExecution execution) {
        ExecutionEntity executionCasted = ((ExecutionEntity) execution);

        String elementId = "<no element id>";
        String description = "";
        String classType = "";
        String eventType = execution.getEventName();
        String loopCounter = "";

        if (execution.isProcessInstanceType()) {
            elementId = "processInstance";
            classType = "Process";
            elementId = ((ExecutionEntity) execution).getProcessDefinitionId();
        }
        else {
            org.flowable.bpmn.model.Process process = ProcessDefinitionUtil.getProcess(execution.getProcessDefinitionId());
            String activityId = execution.getCurrentActivityId();
            FlowElement currentFlowElement = process.getFlowElement(activityId, true);

            if (currentFlowElement != null) {
                classType = currentFlowElement.getClass().getSimpleName();
                elementId = currentFlowElement.getId();
                switch (eventType) {
                    case ExecutionListener.EVENTNAME_TAKE -> {
                        description = currentFlowElement.toString();
                    }
                    case ExecutionListener.EVENTNAME_START -> {
                        //System.out.println("START: " + elementId);
                    }
                    case ExecutionListener.EVENTNAME_END -> {
                        //System.out.println("END:   " + elementId);
                    }
                    default -> {
                        /// System.out.println(execution.getEventName() + ": " + elementId);
                    }
                }

                if (execution.hasVariable("loopCounter")) {
                    loopCounter = "["+ (Integer) execution.getVariable("loopCounter") + "]";
                    // description = description + " loopCounter="+loopCounter;
                }

            }
        }

        String indent = "";
        int count = getProcessDepth(executionCasted);
        for (int i=0; i < count; i++) {
            indent = indent + INDENT_STR + INDENT_STR;
        }

        switch(classType) {
            case "Process" -> {
                ExecutionEntity parentProcess = executionCasted.getSuperExecution();

                if (parentProcess  != null) {
                    description = description + " called from [" + parentProcess.getActivityId() + "]";
                }
            }
            default -> {
                ExecutionEntity parent = ((ExecutionEntity) execution).getParent();

                while(parent != null) {
                    indent = indent + INDENT_STR;
                    parent = parent.getParent();
                }
            }
        }


        System.out.println(String.format("%s--> %s%-6s %-12s id:[%s] %s", indent, loopCounter, (eventType+":").toUpperCase(), classType, elementId, description ));
    }

    private ExecutionEntity findProcessEntity(ExecutionEntity execution) {
        if (execution.isProcessInstanceType()) {
            return execution;
        }
        while(execution != null && !execution.isProcessInstanceType()) {
            execution = execution.getParent();
        }
        return execution;
    }

    private int getProcessDepth(ExecutionEntity execution) {
        execution = findProcessEntity(execution);
        int count = 0;

        // count the parent processes, if any
        if (execution != null) {
            execution = execution.getSuperExecution();

            while(execution != null) {
                count++;
                execution = execution.getSuperExecution();
            }
        }

        return count;
    }
}