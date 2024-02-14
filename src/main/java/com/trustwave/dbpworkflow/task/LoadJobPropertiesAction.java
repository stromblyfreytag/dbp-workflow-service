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
package com.trustwave.dbpworkflow.task;

import java.util.ArrayList;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

import com.trustwave.dbpworkflow.domain.Asset;

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
public class LoadJobPropertiesAction implements JavaDelegate {
    public void execute(DelegateExecution execution) {
        execution.setVariable("syncExceptions", Boolean.TRUE);
        execution.setVariable("failed", Boolean.FALSE);
        execution.setVariable("bestEffort", Boolean.FALSE);
        execution.setVariable("collectJobDataAfterWarehousing", Boolean.TRUE);
        execution.setVariable("successCount", 0L);
        execution.setVariable("reportCount", 0L);
        ArrayList<Asset> value = new ArrayList<>();
        for (int i=1; i < 4; i++) {
            Asset asset = new Asset("asset"+i);
        }
        execution.setVariable("assets", value);
        System.out.println("Action: "+getClass().getSimpleName());
    }
}
