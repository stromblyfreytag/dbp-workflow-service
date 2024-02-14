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
package com.trustwave.dbpworkflow.util.handlers;

import static org.flowable.engine.delegate.BaseExecutionListener.EVENTNAME_END;
import static org.flowable.engine.delegate.BaseExecutionListener.EVENTNAME_START;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowable.bpmn.model.BaseElement;
import org.flowable.bpmn.model.ExtensionAttribute;
import org.flowable.bpmn.model.ExtensionElement;
import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.bpmn.model.Task;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.handler.AbstractBpmnParseHandler;

import com.trustwave.dbpworkflow.util.FlowableListenerBuilder;

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
public class ServiceTaskParseHandler extends AbstractBpmnParseHandler<ServiceTask> {
    public static final String PROGRESS_LISTENER = "${progressListener}";

    @Override
    protected Class<? extends BaseElement> getHandledType() {
        return ServiceTask.class;
    }

    @Override
    protected void executeParse(BpmnParse bpmnParse, ServiceTask element) {
        Map<String, List<ExtensionAttribute>> attributes = element.getAttributes();
        Map<String, List<ExtensionElement>> extensionElements = element.getExtensionElements();

        List<FlowableListener> executionListeners = element.getExecutionListeners();
        if (executionListeners == null) {
            executionListeners = new ArrayList<>();
        }

        executionListeners.add( new FlowableListenerBuilder()
                .eventType(EVENTNAME_START)
                .implementation(PROGRESS_LISTENER)
                .build());

        executionListeners.add( new FlowableListenerBuilder()
                .eventType(EVENTNAME_END)
                .implementation(PROGRESS_LISTENER)
                .build());

        element.setExecutionListeners(executionListeners);
    }

}
