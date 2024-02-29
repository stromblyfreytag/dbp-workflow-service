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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricDetail;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.impl.persistence.entity.HistoricDetailVariableInstanceUpdateEntityImpl;
import org.flowable.engine.runtime.ActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trustwave.dbpworkflow.domain.Asset;
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

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    @Override
    public String sendRetry(String processInstanceId, String executionId) {
        final Boolean userResponseToRetry = Boolean.TRUE;
        final String successResponse = "<html><body>Successful RETRY message to "+executionId+"</body></html>";

        return sendMessageEventReceived(processInstanceId, executionId, userResponseToRetry, successResponse);
    }

    @Override
    public String sendCancel(String processInstanceId, String executionId) {
        final Boolean userResponseToRetry = Boolean.FALSE;
        final String successResponse = "<html><body>Successful CANCEL message to "+executionId+"</body></html>";

        return sendMessageEventReceived(processInstanceId, executionId, userResponseToRetry, successResponse);
    }

    @Override
    public String getScans(boolean syncExceptions, String assetToFail) {
        String businessId = UUID.randomUUID().toString();
        Map<String, Object> processVariables = getVariableMap(syncExceptions, assetToFail);

//        ProcessInstance pi = runtimeService.startProcessInstanceByKey("auditAssetsWorkflow", businessId, processVariables);
//        ProcessInstance pi = runtimeService.startProcessInstanceByKey("simpleSubProcess", businessId, processVariables);
//        ProcessInstance pi = runtimeService.startProcessInstanceByKey("scanAssetsSubProcessWorkflow", businessId, processVariables);
//        ProcessInstance pi = runtimeService.startProcessInstanceByKey("simpleCallOut", businessId, processVariables);
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("dbpAssetMainWorkflow", businessId, processVariables);

        return getProcessInstanceStatus(pi);
    }

    private String sendMessageEventReceived(String processInstanceId, String executionId, Boolean userResponseToRetry,
            String successResponse) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (processInstance == null) {
            return "<html><body>Process instance is no longer active</body></html>";
        }

        List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);

        if (activeActivityIds.isEmpty()) {
            return "<html><body>No active message found</body></html>";
        }

        String messageName = "eventResponseReceivedEvent";
        Execution execution = runtimeService
                .createExecutionQuery()
                .messageEventSubscriptionName(messageName)
                .executionId(executionId)
                .singleResult();

        if (execution == null) {
            return "<html><body>No such executionId: "+ executionId +"</body></html>";
        }
        else {
            runtimeService.setVariable(executionId, "retry", userResponseToRetry);
            runtimeService.messageEventReceived(messageName, executionId);
        }

        return successResponse;
    }

    @Override
    public String getHistory(String processInstanceId) {
        StringBuilder htmlOutput = new StringBuilder();

        // Retrieve historic process instance
        HistoricProcessInstance historicProcessInstance = historyService
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        printProcessStatus(htmlOutput, processInstanceId, historicProcessInstance);

        printProcessInstanceDetails(htmlOutput, historicProcessInstance);

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
            StringBuilder htmlOutput, String prefix) {

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
            case "sequenceFlow" -> activityDisplay;
            default -> activityDisplay;
        };

        htmlOutput.append("<td style='padding: 5px;'>" + prefix + activityDisplay + "</td>");

        // Create a HistoricDetailQuery
        // This will only exist if the HistoryLevel is 'FULL'
        List<HistoricDetail> list = historyService.createHistoricDetailQuery()
                .activityInstanceId(activityInstance.getId())
                .variableUpdates()
                .list();

        // Retrieve the list of HistoricDetail (variable updates)

        // Calculate and display the duration in milliseconds
        Long duration = activityInstance.getDurationInMillis();

        htmlOutput.append("<td style='padding: 5px;'>" + duration + "</td>");
        htmlOutput.append("<td style='padding: 5px;'>" + activityInstance.getActivityType() + "</td>");
        htmlOutput.append("<td style='padding: 5px;'>" + activityInstance.getActivityId() + "</td>");

        final Date startTime = activityInstance.getStartTime();
        final String formattedStartDate = startTime == null ? "--:--" : dateFormat.format(startTime);
        final Date endTime = activityInstance.getEndTime();
        final String formattedEndDate = endTime == null ? "--:--" : dateFormat.format(endTime);

        htmlOutput.append("<td style='padding: 5px;'>" + formattedStartDate + "</td>");
        htmlOutput.append("<td style='padding: 5px;'>" + formattedEndDate + "</td>");
        htmlOutput.append("<td style='padding: 5px;'>");
        htmlOutput.append(list.stream()
                .map(d -> (HistoricDetailVariableInstanceUpdateEntityImpl) d)
                .map(this::fromTo)
                .collect(Collectors.joining(", ")));
        htmlOutput.append("</td>");

        htmlOutput.append("</tr>");
    }

    private String fromTo(HistoricDetailVariableInstanceUpdateEntityImpl m) {
        String name = m.getName();
        String from = ""+m.getCachedValue();
        String to = ""+m.getValue();

        return String.format("<strong>%s</strong>: from[%s] to [%s]", name, from, to);
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
                "<th style='padding: 5px;'>Variables Changed</th>" +
                "</tr>"
        );
    }

    private void printProcessInstanceDetails(StringBuilder htmlOutput, HistoricProcessInstance historicProcessInstance) {
        if (historicProcessInstance != null) {
            htmlOutput.append("<h2>Process Instance Details</h2>");
            htmlOutput.append("<ul>");
            htmlOutput.append("<li>Process Instance ID: " + historicProcessInstance.getId());
            htmlOutput.append("<li>Process Definition Name: " + historicProcessInstance.getProcessDefinitionName());
            htmlOutput.append("<li>Process Business Key: " + historicProcessInstance.getBusinessKey());
            htmlOutput.append("<li>Start Time: " + historicProcessInstance.getStartTime());
            htmlOutput.append("<li>End Time: " + historicProcessInstance.getEndTime());
            htmlOutput.append("<li>Duration (ms): " + historicProcessInstance.getDurationInMillis());
            htmlOutput.append("</ul>");
        }
    }

    private void printProcessStatus(StringBuilder htmlOutput, String processInstanceId,
            HistoricProcessInstance historicProcessInstance) {
        final ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        htmlOutput.append("<h2>Process Instance Status</h2>");
        htmlOutput.append("<strong>");

        String status = "unknown";
        if (processInstance != null) {
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(processInstanceId);
            htmlOutput.append("<h3>Active Tasks</h3>");
            htmlOutput.append("<ul>");
            activeActivityIds.forEach( a -> {
                final ActivityInstance activityInstance = runtimeService.createActivityInstanceQuery()
                        .activityId(a)
                        .unfinished()
                        .singleResult();
                final String activityName = activityInstance.getActivityName();
                htmlOutput.append("<li>");
                htmlOutput.append(activityName);
                htmlOutput.append("</li>");
            });
            htmlOutput.append("</ul>");
            if (processInstance.isEnded()) {
                status = "ENDED";
            }
            else if (processInstance.isSuspended()) {
                status = "SUSPENDED";
            }
            else {
                status = "ACTIVE";
            }
        }
        else {
            if (historicProcessInstance == null) {
                status = "NOT FOUND";
            }
            else {
                status = "COMPLETED AND FLUSHED";
            }
        }
        htmlOutput.append(status);
        htmlOutput.append("</strong>");
        htmlOutput.append("<br>");
    }

    private String getProcessInstanceStatus(ProcessInstance pi) {
        String historyUrl = "http://localhost:8080/history/" + pi.getId();
        String defName = pi.getProcessDefinitionName();
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        sb.append("Process instance has completed.");
        sb.append("<ul><li>");
        sb.append("Process Definition Name:");
        sb.append(defName);
        sb.append("</li><li>Process ID:");
        sb.append(pi.getId());
        sb.append("</li></ul>");
        sb.append("<br>");
        sb.append("Here are your possible actions:<br>");
        sb.append("<ul><li>");
        sb.append("<a href='");
        sb.append(historyUrl);
        sb.append("' target='_blank'>Click here for history</a>");

        if (!pi.isEnded()) {
            String messageName = "eventResponseReceivedEvent";
            final List<Execution> list = runtimeService.createExecutionQuery().messageEventSubscriptionName(messageName).list();

            if (!list.isEmpty()) {
                list.forEach(m -> {
                    String msg = String.format("http://localhost:8080/retry/%s/%s", pi.getId(), m.getId() );
                    sb.append("</li><li>");
                    sb.append("<a href='");
                    sb.append(msg);
                    sb.append("' target='_blank'>Click here for RETRY of execution id: ");
                    sb.append(m.getId());
                    sb.append("</a>");

                    msg = String.format("http://localhost:8080/cancel/%s/%s", pi.getId(), m.getId() );
                    sb.append("</li><li>");
                    sb.append("<a href='");
                    sb.append(msg);
                    sb.append("' target='_blank'>Click here for CANCEL of execution id: ");
                    sb.append(m.getId());
                    sb.append("</a>");
                });
            }
        }

        sb.append("</li></ul>");
        sb.append("</body></html>");
        return sb.toString();
    }

    private Map<String, Object> getVariableMap(boolean syncExceptions, String assetToFail) {
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("syncExceptions", syncExceptions);
        processVariables.put("assetToFail", assetToFail);
        processVariables.put("failure", Boolean.FALSE);
        processVariables.put("bestEffort", Boolean.FALSE);

        List<Asset> assets = new ArrayList<>();
        assets.add(new Asset(false,"asset1"));
        assets.add(new Asset(false,"asset2"));
        processVariables.put("assets", assets);

        List<String> reportConfigurations = new ArrayList<>();
        reportConfigurations.add("report1");
        reportConfigurations.add("report2");
        processVariables.put("reportConfigurations", reportConfigurations);
        return processVariables;
    }
}
