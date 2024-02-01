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
package com.trustwave.dbpworkflow.task;

import java.util.ArrayList;
import java.util.List;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;

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
abstract public class BaseAction {
    protected Expression task;
    protected List<Expression> valuePresent = new ArrayList<>();

    public void execute(DelegateExecution execution) {
        System.out.println("Action: "+getActionName());
        if (task != null) {
            System.out.println("   task: " + task.getExpressionText());
        }
        valuePresent.forEach(v ->
                System.out.println("   valuePresent: "+v.getExpressionText())
        );
    }

    public void setTask(Expression task) {
        this.task = task;
    }

    public void setValuePresent(Expression valuePresent) {
        this.valuePresent.add(valuePresent);
    }

    abstract protected String getActionName() ;
}
