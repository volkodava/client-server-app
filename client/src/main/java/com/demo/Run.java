package com.demo;

import com.demo.converter.ProcessInstanceConverter;
import com.demo.core.ActivitiMessage;
import com.demo.mq.spring.MessageSender;
import java.util.Collections;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Run {

    private static final Logger LOGGER = LoggerFactory.getLogger(Run.class);

    public static void main(String[] args) {

        Run run = new Run();
        ActivitiMessage message = run.startActivitiProcess();
        LOGGER.info("Message to send: {}", message);
        run.sendMessage(message);
        LOGGER.info("Message has been sent");

        System.exit(0);
    }

    public void sendMessage(ActivitiMessage activitiMessage) {

        MessageSender messageSender = AppFactory.getInstance().getBean(MessageSender.class);
        messageSender.simpleSend(activitiMessage);
    }

    public ActivitiMessage startActivitiProcess() {

        RuntimeService runtimeService = AppFactory.getInstance().getBean(RuntimeService.class);
        ProcessInstanceConverter processInstanceConverter = AppFactory.getInstance().getBean(ProcessInstanceConverter.class);

        ProcessInstance processInstance = runtimeService
            .startProcessInstanceByKey("signalProcess", Collections.<String, Object>singletonMap("init", "init"));

        ActivitiMessage activitiMessage = processInstanceConverter.toActivitiMessage(processInstance);

        return activitiMessage;
    }
}
