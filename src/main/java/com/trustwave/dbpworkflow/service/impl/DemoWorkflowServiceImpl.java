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
package com.trustwave.dbpworkflow.service.impl;

import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.flowable.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trustwave.dbpworkflow.domain.Scan;
import com.trustwave.dbpworkflow.service.DemoWorkflowService;

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
//@Service
public class DemoWorkflowServiceImpl implements DemoWorkflowService {
//    @Autowired
    private RuntimeService runtimeService;

    @Override
    public void startProcess(Scan scan) {
//        runtimeService.startProcessInstanceByKey("scan");
    }

    @Override
    public List<Scan> getScans() {
        throw new NotImplementedException("not yet implemented");
    }
}
