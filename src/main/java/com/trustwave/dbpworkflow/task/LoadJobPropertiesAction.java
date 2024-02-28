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
import java.util.HashMap;
import java.util.List;

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
public class LoadJobPropertiesAction extends BaseAction implements JavaDelegate {
    public void execute(DelegateExecution execution) {
        super.execute(execution);
        execution.setVariable("syncExceptions", Boolean.TRUE);
        execution.setVariable("failed", Boolean.FALSE);
        execution.setVariable("bestEffort", Boolean.FALSE);
        execution.setVariable("collectJobDataAfterWarehousing", Boolean.TRUE);
        execution.setVariable("successCount", 0L);
        execution.setVariable("reportCount", 0L);

        int assetCount = 4;
        ArrayList<Asset> assets = new ArrayList<>();
        ArrayList<String> jobKeys = new ArrayList<>();
        ArrayList<String> reportConfigurations = new ArrayList<>();

        for (int i=1; i < assetCount; i++) {
            Asset asset = new Asset(false,"asset"+i);
            assets.add(asset);
        }
        execution.setVariable("assets", assets);
        execution.setVariable("jobKeys", jobKeys);
        execution.setVariable("reportConfigurations", reportConfigurations);

        System.out.println("Action: "+getClass().getSimpleName());
    }

    @Override
    protected String getActionName() {
        return this.getClass().getSimpleName();
    }
}
