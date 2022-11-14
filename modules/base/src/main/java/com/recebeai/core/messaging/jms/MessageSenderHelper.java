package tech.jannotti.billing.core.messaging.jms;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;

@Component
public class MessageSenderHelper {

    @Value("${core.notification.deliveryDelay}")
    private Integer deliveryDelay;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(String queue, Object message) {
        send(queue, message, deliveryDelay);
    }

    public void send(String queue, Object message, int delay) {

        MessageProcessor messageProcessor = new MessageProcessor(delay);
        jmsTemplate.convertAndSend(queue, message, messageProcessor);
    }

    public static class MessageProcessor implements MessagePostProcessor {

        private Integer delay;

        public MessageProcessor(int delay) {
            this.delay = delay;
        }

        public Message postProcessMessage(Message message) throws JMSException {

            long deliverySchedule = DateTimeHelper.getNowLongDateTime() + delay;
            message.setLongProperty("_AMQ_SCHED_DELIVERY", deliverySchedule);

            return message;
        }
    }

}
