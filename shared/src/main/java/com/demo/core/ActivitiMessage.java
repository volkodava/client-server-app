package com.demo.core;

import java.util.Map;

public class ActivitiMessage {

    private String id;

    private String processDefinitionId;

    private String businessKey;

    private boolean suspended;

    private Map<String, Object> processVariables;

    private String tenantId;

    private boolean ended;

    private String activityId;

    private String processInstanceId;

    private String parentId;

    private String executionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public Map<String, Object> getProcessVariables() {
        return processVariables;
    }

    public void setProcessVariables(Map<String, Object> processVariables) {
        this.processVariables = processVariables;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    @Override
    public String toString() {
        return "id=" + id + ", processDefinitionId=" + processDefinitionId + ", businessKey=" + businessKey + ", suspended=" + suspended + ", processVariables=" + processVariables + ", tenantId=" + tenantId + ", ended=" + ended + ", activityId=" + activityId + ", processInstanceId=" + processInstanceId + ", parentId=" + parentId + ", executionId=" + executionId;
    }
}
