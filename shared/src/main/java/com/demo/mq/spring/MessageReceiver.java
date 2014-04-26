package com.demo.mq.spring;

import com.demo.converter.JsonConverter;
import com.demo.core.ActivitiMessage;
import com.demo.core.ActivitiMessageEvent;
import com.demo.core.ActivitiMessageListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class MessageReceiver implements MessageListener, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private JsonConverter messageConverter;
    private List<ActivitiMessageListener> listeners = new CopyOnWriteArrayList<ActivitiMessageListener>();

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            TextMessage castedMessage = (TextMessage) message;
            try {
                String text = castedMessage.getText();
                logger.info("Received text {}", text);
                try {
                    ActivitiMessage activitiMessage = messageConverter.readValue(text, ActivitiMessage.class);
                    notifyActivitiMessageListeners(activitiMessage);
                } catch (IOException e) {
                    logger.error("Can't parse the message: " + text, e);
                }
            } catch (JMSException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    private void notifyActivitiMessageListeners(ActivitiMessage message) {
        for (ActivitiMessageListener listener : listeners) {
            listener.messageReceived(new ActivitiMessageEvent(message));
        }
    }

    public void addActivitiMessageListener(ActivitiMessageListener listener) {
        listeners.add(listener);

    }

    public void removeActivitiMessageListener(ActivitiMessageListener listener) {
        listeners.remove(listener);
    }

    public void setMessageConverter(JsonConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(messageConverter, "Message converter must not be null");
    }
}
