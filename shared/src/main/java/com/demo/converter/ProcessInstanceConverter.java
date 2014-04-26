package com.demo.converter;

import com.demo.core.ActivitiMessage;
import java.util.UUID;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;

@Service("processInstanceConverter")
public class ProcessInstanceConverter {

    public ActivitiMessage toActivitiMessage(ProcessInstance processInstance) {

        Validate.notNull(processInstance, "Process instance must not be null");

        ActivitiMessage activitiMessage = new ActivitiMessage();
        activitiMessage.setId(UUID.randomUUID().toString());
        activitiMessage.setActivityId(processInstance.getActivityId());
        activitiMessage.setBusinessKey(processInstance.getBusinessKey());
        activitiMessage.setEnded(processInstance.isEnded());
        activitiMessage.setExecutionId(processInstance.getId());
        activitiMessage.setParentId(processInstance.getParentId());
        activitiMessage.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        activitiMessage.setProcessInstanceId(processInstance.getProcessInstanceId());
        activitiMessage.setProcessVariables(processInstance.getProcessVariables());
        activitiMessage.setSuspended(processInstance.isSuspended());
        activitiMessage.setTenantId(processInstance.getTenantId());

        return activitiMessage;
    }
}
