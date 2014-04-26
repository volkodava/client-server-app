package com.demo;

import com.demo.core.ActivitiMessage;
import com.demo.core.ActivitiMessageEvent;
import com.demo.core.ActivitiMessageListener;
import com.demo.core.Command;
import com.demo.mq.spring.MessageReceiver;
import com.demo.util.TimedCallUtil;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Run {

    private static final Logger LOGGER = LoggerFactory.getLogger(Run.class);
    private static final long MAX_TIME_TO_WAIT = 20;
    private static final TimeUnit MAX_TIME_TO_WAIT_UNIT = TimeUnit.SECONDS;
    private static final long INTERVAL = 5;
    private static final TimeUnit INTERVAL_UNIT = TimeUnit.SECONDS;
    private static final String SIGNAL_EVENT_SUBSCRIPTION_NAME = "alert";

    public static void main(String[] args) {

        Run run = new Run();
        LOGGER.info("Launch application");
        run.launch();
    }

    private void launch() {

        MessageReceiver receiver = AppFactory.getInstance().getBean(MessageReceiver.class);
        receiver.addActivitiMessageListener(new ActivitiMessageListener() {

            @Override
            public void messageReceived(ActivitiMessageEvent event) {

                LOGGER.info("Message received: {}", event);

                final ActivitiMessage activitiMessage = event.getActivitiMessage();

                LOGGER.info("Find the activiti execution");
                Execution execution = findExecution(activitiMessage);

                LOGGER.info("Fire event to complete process instance");
                notifiesSignalEventReceived(execution);

                LOGGER.info("Wait for completion of process instance");
                HistoricProcessInstance completedHistoricProcessInstance = findHistoricProcessInstance(activitiMessage);

                LOGGER.info("Process instance successfully completed. Process instance id: {}", completedHistoricProcessInstance.getId());
            }
        });
    }

    private void notifiesSignalEventReceived(Execution execution) {

        final RuntimeService runtimeService = AppFactory.getInstance().getBean(RuntimeService.class);
        runtimeService.signalEventReceived(SIGNAL_EVENT_SUBSCRIPTION_NAME,
            execution.getId(),
            Collections.<String, Object>singletonMap("test", "test"));
    }

    private HistoricProcessInstance findHistoricProcessInstance(final ActivitiMessage activitiMessage) throws RuntimeException {

        final TimedCallUtil timedCallUtil = AppFactory.getInstance().getBean(TimedCallUtil.class);
        final HistoryService historyService = AppFactory.getInstance().getBean(HistoryService.class);

        Command<HistoricProcessInstance> waitForCompletionOfExecutionCommand = new Command<HistoricProcessInstance>() {

            @Override
            public HistoricProcessInstance execute() {
                HistoricProcessInstance result = historyService.createHistoricProcessInstanceQuery().
                    processInstanceId(activitiMessage.getProcessInstanceId()).
                    finished().
                    singleResult();

                if (result != null) {
                    return result;
                }

                return null;
            }
        };

        HistoricProcessInstance completedHistoricProcessInstance;
        try {
            completedHistoricProcessInstance = timedCallUtil.<HistoricProcessInstance>call(waitForCompletionOfExecutionCommand, MAX_TIME_TO_WAIT, MAX_TIME_TO_WAIT_UNIT, INTERVAL, INTERVAL_UNIT);
        } catch (InterruptedException e) {
            LOGGER.error("Execution of call to find an activiti process was interrupted", e);
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            LOGGER.error("Can't find an activiti process", e);
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            LOGGER.error("Can't find an activiti process. Timeout reached.", e);
            throw new RuntimeException(e);
        }
        return completedHistoricProcessInstance;
    }

    private Execution findExecution(final ActivitiMessage activitiMessage) throws RuntimeException {

        final TimedCallUtil timedCallUtil = AppFactory.getInstance().getBean(TimedCallUtil.class);
        final RuntimeService runtimeService = AppFactory.getInstance().getBean(RuntimeService.class);
        Command<Execution> findExecutionCommand = new Command<Execution>() {

            @Override
            public Execution execute() {
                Execution result = runtimeService.createExecutionQuery()
                    .processInstanceId(activitiMessage.getProcessInstanceId())
                    .signalEventSubscriptionName(SIGNAL_EVENT_SUBSCRIPTION_NAME)
                    .singleResult();

                return result;
            }
        };

        Execution execution;
        try {
            execution = timedCallUtil.<Execution>call(findExecutionCommand, MAX_TIME_TO_WAIT, MAX_TIME_TO_WAIT_UNIT, INTERVAL, INTERVAL_UNIT);
        } catch (InterruptedException e) {
            LOGGER.error("Execution of call to find an activiti process was interrupted", e);
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            LOGGER.error("Can't find an activiti process", e);
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            LOGGER.error("Can't find an activiti process. Timeout reached.", e);
            throw new RuntimeException(e);
        }
        return execution;
    }
}
