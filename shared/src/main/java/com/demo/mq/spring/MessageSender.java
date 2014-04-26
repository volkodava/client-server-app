package com.demo.mq.spring;

import com.demo.converter.JsonConverter;
import com.demo.core.ActivitiMessage;
import java.io.IOException;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.Assert;

public class MessageSender implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private final JmsTemplate jmsTemplate;
    private JsonConverter messageConverter;

    /**
     * Inject a JMS Template
     *
     * @param jmsTemplate
     */
    public MessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void simpleSend(final ActivitiMessage message) {

        Validate.notNull(message, "Message must not be null");

        try {
            String textMessage = messageConverter.writeValueAsString(message);
            jmsTemplate.convertAndSend(textMessage);
        } catch (IOException e) {
            logger.error("Can't send the message: " + message, e);
        }
    }

    public void setMessageConverter(JsonConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(jmsTemplate, "JMS template must not be null");
        Assert.notNull(messageConverter, "Message converter must not be null");
    }
}
