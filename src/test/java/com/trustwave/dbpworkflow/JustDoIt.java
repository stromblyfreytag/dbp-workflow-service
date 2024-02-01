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
package com.trustwave.dbpworkflow;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.flowable.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;

import com.trustwave.dbpworkflow.asset.ApplyValidatorsAction;
import com.trustwave.dbpworkflow.task.AuditAction;
import com.trustwave.dbpworkflow.task.BaseAction;
import org.flowable.common.engine.api.delegate.Expression;

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
public class JustDoIt {
    @Test
    public void testBaseActionTask() {
        BaseAction baseAction = new BaseAction() {

            @Override
            protected String getActionName() {
                return "testing name -- ignore";
            }
        };
        Expression mockExpression = mock(Expression.class);
        when(mockExpression.getExpressionText()).thenReturn("baseTask");
        baseAction.setTask(mockExpression);
        DelegateExecution mockExecution = mock(DelegateExecution.class);
        baseAction.execute(mockExecution);

        verify(mockExpression).getExpressionText();
    }

    @Test
    public void testApplyValidatorsAction() {
        ApplyValidatorsAction childAction = new ApplyValidatorsAction();
        for (int i = 0; i < 4; i++) {
            Expression mock = mock(Expression.class);
            when(mock.getExpressionText()).thenReturn("string"+i);
            childAction.setValuePresent(mock);
        }

        Expression mockTask = mock(Expression.class);
        when(mockTask.getExpressionText()).thenReturn("applyValidatorsTask");
        childAction.setTask(mockTask);
        DelegateExecution mockExecution = mock(DelegateExecution.class);
        childAction.execute(mockExecution);

        verify(mockTask).getExpressionText();
    }

    @Test
    public void testAuditAction() {
        AuditAction childAction = new AuditAction();

        DelegateExecution mockExecution = mock(DelegateExecution.class);
        childAction.execute(mockExecution);
    }
}
