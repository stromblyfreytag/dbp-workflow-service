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

import org.flowable.bpmn.model.FlowableListener;
import org.flowable.bpmn.model.ImplementationType;
import org.flowable.engine.delegate.ExecutionListener;

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
public class FlowableListenerBuilder {
    private String implementationType = ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION;
    private String implementation;
    private String eventType;

    public FlowableListenerBuilder implementation(String implementation) {
        this.implementation = implementation;
        return this;
    }

    public FlowableListenerBuilder eventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public FlowableListenerBuilder implementationType(String implementationType) {
        this.implementationType = implementationType;
        return this;
    }

    public FlowableListener build() {
        FlowableListener flowableListener = new FlowableListener();
        flowableListener.setImplementationType(implementationType);
        flowableListener.setImplementation(implementation);
        flowableListener.setEvent(eventType);
        return flowableListener;
    }
}
