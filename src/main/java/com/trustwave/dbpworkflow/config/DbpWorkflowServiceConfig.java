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
package com.trustwave.dbpworkflow.config;

import java.util.ArrayList;
import java.util.List;

import org.flowable.engine.parse.BpmnParseHandler;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.trustwave.dbpworkflow.util.ParseHandlerRegistry;

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
@Configuration
public class DbpWorkflowServiceConfig {

	// This adds custom parsers to the process definition:
	// In these particular cases, we are added execution listeners
		@Bean
		public EngineConfigurationConfigurer<SpringProcessEngineConfiguration> customBpmnParseHandlers() {
			return processEngineConfiguration -> {
				List<BpmnParseHandler> parseHandlers = processEngineConfiguration.getPostBpmnParseHandlers();
				if (parseHandlers == null) {
					parseHandlers = new ArrayList<>();
				}

				parseHandlers.addAll(ParseHandlerRegistry.handlers());

				processEngineConfiguration.setPostBpmnParseHandlers(parseHandlers);
			};
		}
}
