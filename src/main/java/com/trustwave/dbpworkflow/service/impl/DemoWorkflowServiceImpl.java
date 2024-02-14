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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
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
@Service
public class DemoWorkflowServiceImpl implements DemoWorkflowService {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;


    @Override
    public String getHistory(String processInstanceId) {
        StringBuilder htmlOutput = new StringBuilder();

        // Retrieve historic process instance
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        printprocessInstanceDetails(htmlOutput, historicProcessInstance);

        // Retrieve historic activity instances
        List<HistoricActivityInstance> activityInstances = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        if (!activityInstances.isEmpty()) {
            startActivityInstancesTable(htmlOutput);
            printActivityInstances(processInstanceId, activityInstances, htmlOutput, "");
            finishTable(htmlOutput);
        }

        // Retrieve historic task instances
        List<HistoricTaskInstance> taskInstances = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        if (!taskInstances.isEmpty()) {
            htmlOutput.append("<h2>Historic Task Instances</h2>");
            htmlOutput.append("<ul>");
            for (HistoricTaskInstance taskInstance : taskInstances) {
                htmlOutput.append("<li>Task ID: " + taskInstance.getId() + ", Name: " + taskInstance.getName() + ", Start Time: " + taskInstance.getStartTime() + ", End Time: " + taskInstance.getEndTime() + "</li>");
            }
            htmlOutput.append("</ul>");
        }

        // Retrieve historic variable instances
        List<HistoricVariableInstance> variableInstances = historyService
                .createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();

        if (!variableInstances.isEmpty()) {
            htmlOutput.append("<h2>Historic Variable Instances</h2>");
            htmlOutput.append("<table border='1' style='border-collapse: collapse;'>");
            htmlOutput.append("<tr>" +
                    "<th style='padding: 5px;'>Name</th>" +
                    "<th style='padding: 5px;'>Value</th>" +
                    "</tr>"
            );
            for (HistoricVariableInstance variableInstance : variableInstances) {
                htmlOutput.append("<tr>");
                htmlOutput.append("<td style='padding: 5px;'>" + variableInstance.getVariableName() + "</td>");
                htmlOutput.append("<td style='padding: 5px;'>" + variableInstance.getValue() + "</td>");
                htmlOutput.append("</tr>");
            }
        }
        htmlOutput.append("</table>");

        return htmlOutput.toString();
    }

    private void printActivityInstances(String processInstanceId, List<HistoricActivityInstance> activityInstances,
            StringBuilder htmlOutput,
            String prefix) {
        if (!activityInstances.isEmpty()) {
            for (HistoricActivityInstance activityInstance : activityInstances) {
                printActivityInstanceRow(processInstanceId, activityInstance, htmlOutput, prefix);

                String calledProcessInstanceId = activityInstance.getCalledProcessInstanceId();
                if (calledProcessInstanceId != null) {
                    // Retrieve any called historic activity instances
                    List<HistoricActivityInstance> calledActivityInstances = historyService
                            .createHistoricActivityInstanceQuery()
                            .processInstanceId(calledProcessInstanceId)
                            .list();

                    printActivityInstances(processInstanceId, calledActivityInstances, htmlOutput, prefix + "&nbsp;&nbsp;&nbsp;");
                }
            }
        }
    }

    private void printActivityInstanceRow(String processInstanceId, HistoricActivityInstance activityInstance,
            StringBuilder htmlOutput,
            String prefix) {
        htmlOutput.append("<tr>");
        String activityName = activityInstance.getActivityName();
        String activityDisplay = activityName == null ? activityInstance.getActivityId() : activityName;

        activityDisplay = switch(activityInstance.getActivityType()) {
            case "startEvent", "serviceTask", "endEvent", "receiveTask" -> "<strong>"+activityDisplay+" </strong>";
            case "callActivity" -> "<em><strong>"+activityDisplay+"</strong></em> (call-out)";
            case "sequenceFlow" -> activityDisplay +" flows to " + getTargetActivityId(processInstanceId, activityInstance.getActivityId() );
            default -> activityDisplay;
        };

        htmlOutput.append("<td style='padding: 5px;'>" + prefix + activityDisplay + "</td>");

        // Calculate and display the duration in milliseconds
        long duration = activityInstance.getDurationInMillis();

        htmlOutput.append("<td style='padding: 5px;'>" + duration + "</td>");
        htmlOutput.append("<td style='padding: 5px;'>" + activityInstance.getActivityType() + "</td>");
        htmlOutput.append("<td style='padding: 5px;'>" + activityInstance.getActivityId() + "</td>");

        htmlOutput.append("<td style='padding: 5px;'>" + activityInstance.getStartTime() + "</td>");
        htmlOutput.append("<td style='padding: 5px;'>" + activityInstance.getEndTime() + "</td>");

        htmlOutput.append("</tr>");
    }

    private void finishTable(StringBuilder htmlOutput) {
        htmlOutput.append("</table>");
    }

    private void startActivityInstancesTable(StringBuilder htmlOutput) {
        htmlOutput.append("<h2>Historic Activity Instances</h2>");
        htmlOutput.append("<table border='1' style='border-collapse: collapse;'>");
        htmlOutput.append("<tr>" +
                "<th style='padding: 5px;'>Activity Name</th>" +
                "<th style='padding: 5px;'>Duration (ms)</th>" +
                "<th style='padding: 5px;'>Activity Type</th>" +
                "<th style='padding: 5px;'>Id</th>" +
                "<th style='padding: 5px;'>Start Time</th>" +
                "<th style='padding: 5px;'>End Time</th>" +
                "</tr>"
        );
    }

    private void printprocessInstanceDetails(StringBuilder htmlOutput, HistoricProcessInstance historicProcessInstance) {
        if (historicProcessInstance != null) {
            htmlOutput.append("<h2>Process Instance Details</h2>");
            htmlOutput.append("<ul>");
            htmlOutput.append("<li>Process Instance ID: " + historicProcessInstance.getId());
            htmlOutput.append("<li>Process Business Key: " + historicProcessInstance.getBusinessKey());
            htmlOutput.append("<li>Start Time: " + historicProcessInstance.getStartTime());
            htmlOutput.append("<li>End Time: " + historicProcessInstance.getEndTime());
            htmlOutput.append("</ul>");
        }
    }

    @Override
    public void startProcess(Scan scan) {
        throw new NotImplementedException("not yet implemented");
//        runtimeService.startProcessInstanceByKey("scan");
    }

    @Override
    public List<Scan> getScans() {
        throw new NotImplementedException("not yet implemented");
    }

    public static class HtmlOutput {
        private StringBuilder sb = new StringBuilder();

        public HtmlOutput table() {
            sb.append("<table>");
            return this;
        }

        public HtmlOutput endtable() {
            sb.append("</table>");
            return this;
        }

        public HtmlOutput tr() {
            sb.append("<tr>");
            return this;
        }

        public HtmlOutput th(String... hdr) {
            Arrays.stream(hdr).forEach(s -> sb.append("<th style='padding: 5px;'>"+s+"</th>"));
            return this;
        }

        public HtmlOutput td(String s) {
            sb.append("<td style='padding: 5px;'>" + s + "</td>");
            return this;
        }
    }

    private String getTargetActivityId(String processInstanceId, String sourceActivityId) {
        // Query for outgoing sequence flows based on the process instance ID and source activity ID
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .activityId(sourceActivityId)
                .orderByHistoricActivityInstanceEndTime()
                .desc();

        List<HistoricActivityInstance> outgoingFlows = query.list();

        if (!outgoingFlows.isEmpty()) {
            // Assuming the first entry is the latest completed instance of the source activity
            // Retrieve the target activity ID from the sequence flow information
            return outgoingFlows.get(0).getActivityName();
        }

        // Default to null if no information is found
        return "n/a";
    }
}
