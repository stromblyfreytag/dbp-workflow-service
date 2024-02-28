/*
 * Copyright (c) 2023 Trustwave Holdings, Inc.
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
package com.trustwave.dbpworkflow.check;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import com.trustwave.dbpworkflow.domain.Asset;
import com.trustwave.dbpworkflow.task.BaseAction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * -- TODO add description here
 *
 * <pre>
 * Copyright (c) 2023 Trustwave Holdings, Inc.
 * All rights reserved.
 * </pre>
 *
 * @author sfreytag
 */
@NoArgsConstructor
@Getter
@Setter
public class PolicyListToCheckSetsAction extends BaseAction implements JavaDelegate {
    public void execute(DelegateExecution execution) {
        super.execute(execution);
        final Asset asset = (Asset)execution.getVariable("asset");
        asset.setPassedPrecheck(true);
        execution.setVariable("asset", asset);
        execution.setVariable("failed", Boolean.FALSE);
        System.out.println("PolicyListToCheckSetsAction Action. Task="+task.getExpressionText());
    }

    @Override
    protected String getActionName() {
        return getClass().getSimpleName();
    }
}
