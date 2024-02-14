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

import org.flowable.engine.parse.BpmnParseHandler;

import com.trustwave.dbpworkflow.util.handlers.BoundaryEventParseHandler;
import com.trustwave.dbpworkflow.util.handlers.CallActionParseHandler;
import com.trustwave.dbpworkflow.util.handlers.EndEventParseHandler;
import com.trustwave.dbpworkflow.util.handlers.ExclusiveGatewayParseHandler;
import com.trustwave.dbpworkflow.util.handlers.ParallelGatewayParseHandler;
import com.trustwave.dbpworkflow.util.handlers.ProcessParseHandler;
import com.trustwave.dbpworkflow.util.handlers.SequenceFlowParseHandler;
import com.trustwave.dbpworkflow.util.handlers.ServiceTaskParseHandler;
import com.trustwave.dbpworkflow.util.handlers.StartEventParseHandler;
import com.trustwave.dbpworkflow.util.handlers.SubProcessParseHandler;

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
public class ParseHandlerRegistry {
    static List<BpmnParseHandler> handlers = new ArrayList<>();

    static {
        handlers.add(new BoundaryEventParseHandler());
        handlers.add(new CallActionParseHandler());
        handlers.add(new EndEventParseHandler());
        handlers.add(new ExclusiveGatewayParseHandler());
        handlers.add(new ParallelGatewayParseHandler());
        handlers.add(new ProcessParseHandler());
        handlers.add(new SequenceFlowParseHandler());
        handlers.add(new ServiceTaskParseHandler());
        handlers.add(new StartEventParseHandler());
        handlers.add(new SubProcessParseHandler());
    }

    public static void register(BpmnParseHandler handler) {
        handlers.add(handler);
    }

    public static List<BpmnParseHandler> handlers() { return handlers; }
}
