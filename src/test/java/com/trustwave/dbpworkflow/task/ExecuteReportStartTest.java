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

/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.engine.test.Deployment;
import org.flowable.engine.test.FlowableRule;
import org.flowable.task.api.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:org/flowable/spring/test/junit4/springTypicalUsageTest-context.xml")
public class ExecuteReportStartTest {

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    @Rule
    public FlowableRule flowableSpringRule;

    @Autowired
    protected RepositoryService repositoryService;

    @Before
    public void startup() {
    }

    @After
    public void closeProcessEngine() {
        // Required, since all the other tests seem to do a specific drop on the end
        processEngine.close();
    }

    @Test
    @Deployment(resources = {"processes/auditWorkflowDemo.bpmn20.xml"})
    public void testingDeployment_single() {
        // Verify that we understand how workflows are referenced for testing.
        // Question: can we get a single workflow referenced correctly?
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        assertEquals("demo", processDefinition.getId() );
    }

    @Test
    @Deployment(resources = {"processes/auditWorkflowDemo.bpmn20.xml", "processes/demoworkflow.bpmn20.xml"})
    public void testingDeployment_double() {
        // Verify that we understand how workflows are referenced for testing.
        // Question: can we specify multiple workflows and reference them?
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        assertEquals(2, processDefinitions.size() );

        Set<String> names = processDefinitions.stream().map(ProcessDefinition::getId).collect(Collectors.toSet());
        assertTrue(names.contains("demo"));
        assertTrue(names.contains("demoDec13_2023_1"));
    }

    @Test
    @Deployment(resources = {"processes/auditWorkflowDemo.bpmn20.xml"})
    public void testingDeployment_() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

//        runtimeService.c

        ProcessInstanceBuilder demo = runtimeService.createProcessInstanceBuilder().processDefinitionKey("demo");
        demo.startEventId("executeReportStart").start();
//        runtimeService.startProcessInstanceByKey("demo");
        long count = taskService.createTaskQuery().count();

        List<Task> tasks = taskService.createTaskQuery().list();
        assertNotNull(tasks);
        assertTrue(tasks.size() > 0);
        Task task = tasks.get(0);
        assertThat(task.getName()).isEqualTo("My Task");

        // ACT-1186: ActivitiRule services not initialized when using
        // SpringJUnit4ClassRunner together with @ContextConfiguration
        assertThat(flowableSpringRule.getRuntimeService()).isNotNull();

        taskService.complete(task.getId());
        assertThat(runtimeService.createProcessInstanceQuery().count()).isZero();
    }

}
