package com.demo.activiti.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("logService")
@Transactional
public class LogService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void info(final DelegateExecution execution, String message) throws Exception {

        Validate.notNull(execution, "Execution must not be null");
        Validate.notNull(message, "Message must not be null");

        logger.info("*** Variables:\n{}", execution.getVariables());
        logger.info("*** Local Variables:\n{}", execution.getVariablesLocal());
        logger.info("*** Message:\n{}", message);
    }
}
