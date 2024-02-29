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
package com.trustwave.dbpworkflow.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
public class DemoController {
    @Autowired
    private DemoWorkflowService service;

    @Autowired
    private RuntimeService runtimeService;

    @RequestMapping(value = "/demo", method = RequestMethod.GET)
    public String getDemo(@RequestParam(name = "syncExceptions", defaultValue = "false") boolean syncExceptions,
            @RequestParam(name = "storeResults", defaultValue = "false") boolean storeResults) {

        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("syncExceptions", syncExceptions);
        processVariables.put("storeResults", storeResults);
        processVariables.put("taskFailed", Boolean.FALSE);
        String businessId = UUID.randomUUID().toString();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("demo", businessId, processVariables);


        // the wait is an artificial task so we can make queries of the instance, if desired
        // put any queries here


        Execution execution = runtimeService.createExecutionQuery().processInstanceId(pi.getId()).activityId("waitTask").singleResult();
        runtimeService.trigger(execution.getId());
        String historyUrl = "http://localhost:8080/history/" + pi.getId();
        return "Process instance has run. Process ID:"+pi.getId()+". <br><a href='" + historyUrl + "' target='_blank'>Click here for history</a>";
    }

    @GetMapping(value = "/history/{processInstanceId}")
    public String getHistory(@PathVariable String processInstanceId) {
        return service.getHistory(processInstanceId);
    }

    @GetMapping(value = "/retry/{processInstanceId}/{executionId}")
    public String sendRetry( @PathVariable String processInstanceId, @PathVariable String executionId ) {
        return service.sendRetry(processInstanceId, executionId);
    }

    @GetMapping(value = "/cancel/{processInstanceId}/{executionId}")
    public String sendCancel( @PathVariable String processInstanceId, @PathVariable String executionId ) {
        return service.sendCancel(processInstanceId, executionId);
    }

    @RequestMapping(value = "/scan", method = RequestMethod.GET)
    public String getScan(@RequestParam(name = "syncExceptions", defaultValue = "false") boolean syncExceptions,
            @RequestParam(name = "assetToFail", defaultValue = "") String assetToFail) {

        return service.getScans(syncExceptions, assetToFail);
    }
}
