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

import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.util.ProcessDefinitionUtil;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;

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
public class RecordedExecutionListener implements ExecutionListener {
    private static List<RecordedEvent> recordedEvents = new ArrayList<>();

    private Expression parameter;

    @Override
    public void notify(DelegateExecution execution) {
        ExecutionEntity executionCasted = ((ExecutionEntity) execution);

        org.flowable.bpmn.model.Process process = ProcessDefinitionUtil.getProcess(execution.getProcessDefinitionId());
        String activityId = execution.getCurrentActivityId();
        FlowElement currentFlowElement = process.getFlowElement(activityId, true);
        String inflow = "";
        if ( currentFlowElement instanceof ServiceTask) {
            ServiceTask task = (ServiceTask)currentFlowElement;
            List<SequenceFlow> incomingFlows = task.getIncomingFlows();
            if (!incomingFlows.isEmpty()) {
                inflow = incomingFlows.get(0).getSourceRef();
            }
        }

        recordedEvents.add(new RecordedEvent(
                executionCasted.getActivityId(),
                (currentFlowElement != null) ? currentFlowElement.getName() : null,
                execution.getEventName(),
                inflow));
    }

    public static void clear() {
        recordedEvents.clear();
    }

    public static List<RecordedEvent> getRecordedEvents() {
        return recordedEvents;
    }

    public static class RecordedEvent {
        private final String activityId;
        private final String eventName;
        private final String activityName;
        private final String incomingFlow;

        public RecordedEvent(String activityId, String activityName, String eventName, String incomingFlow) {
            this.activityId = activityId;
            this.activityName = activityName;
            this.incomingFlow = incomingFlow;
            this.eventName = eventName;
        }

        public String getActivityId() {
            return activityId;
        }

        public String getEventName() {
            return eventName;
        }

        public String getActivityName() {
            return activityName;
        }

        public String getIncomingFlow() {
            return incomingFlow;
        }

    }
}