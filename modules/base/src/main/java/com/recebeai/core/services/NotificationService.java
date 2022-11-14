package tech.jannotti.billing.core.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.http.client.HttpClientHelper;
import tech.jannotti.billing.core.commons.http.client.HttpClientHelper.HttpResponse;
import tech.jannotti.billing.core.commons.http.client.exception.HttpClientRequestException;
import tech.jannotti.billing.core.commons.log.LogFactory;
import tech.jannotti.billing.core.commons.log.LogManager;
import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.messaging.jms.MessageSenderHelper;
import tech.jannotti.billing.core.messaging.jms.MessagingConstants;
import tech.jannotti.billing.core.persistence.enums.NotificationStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseInvoice;
import tech.jannotti.billing.core.persistence.model.base.notification.BaseInvoiceNotification;
import tech.jannotti.billing.core.persistence.model.base.notification.BaseNotification;
import tech.jannotti.billing.core.persistence.model.base.notification.BaseNotificationType;
import tech.jannotti.billing.core.persistence.model.base.notification.BaseNotifyRequest;
import tech.jannotti.billing.core.persistence.model.base.notification.BaseNotifyResponse;
import tech.jannotti.billing.core.persistence.model.base.notification.BasePaymentNotification;
import tech.jannotti.billing.core.persistence.model.base.payment.BasePayment;
import tech.jannotti.billing.core.persistence.repository.base.notification.NotificationRepository;
import tech.jannotti.billing.core.persistence.repository.base.notification.NotificationTypeRepository;
import tech.jannotti.billing.core.persistence.repository.base.notification.NotifyRequestRepository;
import tech.jannotti.billing.core.persistence.repository.base.notification.NotifyResponseRepository;
import tech.jannotti.billing.core.services.dto.notification.AbstractNotificationDTO;
import tech.jannotti.billing.core.services.dto.parser.NotificationJSONParser;
import tech.jannotti.billing.core.services.exception.ServiceException;

@Service
public class NotificationService extends AbstractService {

    private static final LogManager logManager = LogFactory.getManager(NotificationService.class);

    @Value("${core.notification.requestTimeout}")
    private Integer requestTimeout;

    @Value("${core.notification.retryLimit}")
    private Integer retryLimit;

    @Value("${core.notification.retryDelay}")
    private Integer retryDelay;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationTypeRepository notificationTypeRepository;

    @Autowired
    private NotifyRequestRepository notifyRequestRepository;

    @Autowired
    private NotifyResponseRepository notifyResponseRepository;

    @Autowired
    private MessageSenderHelper messageSenderHelper;

    @Autowired
    private NotificationJSONParser notificationJSONParser;

    @Autowired
    private HttpClientHelper httpClientHelper;

    public void dispatch(BaseInvoice invoice, String notificationTypeCode, AbstractNotificationDTO notificationDTO) {

        BaseInvoiceNotification notification = new BaseInvoiceNotification();
        notification.setInvoice(invoice);

        doDispatch(notification, notificationTypeCode, notificationDTO, invoice.getCallbackUrl());
    }

    public void dispatch(BasePayment payment, String notificationTypeCode, AbstractNotificationDTO notificationDTO) {

        BasePaymentNotification notification = new BasePaymentNotification();
        notification.setPayment(payment);

        doDispatch(notification, notificationTypeCode, notificationDTO, payment.getInvoice().getCallbackUrl());
    }

    private void doDispatch(BaseNotification notification, String notificationTypeCode, AbstractNotificationDTO notificationDTO,
        String destinationUrl) {

        BaseNotificationType notificationType = notificationTypeRepository.getByCode(notificationTypeCode);
        String messageContent = notificationJSONParser.parseToJSON(notificationDTO);
        LocalDateTime creationDate = DateTimeHelper.getNowDateTime();

        notification.setNotificationType(notificationType);
        notification.setDestinationUrl(destinationUrl);
        notification.setMessageContent(messageContent);
        notification.setStatus(NotificationStatusEnum.PENDING);
        notification.setCreationDate(creationDate);

        notificationRepository.save(notification);

        // Envia pra fila de notificacoes
        messageSenderHelper.send(MessagingConstants.NOTIFICATIONS_QUEUE, notification.getId());
    }

    @Transactional
    public void resendUndelivered() {

        List<BaseNotification> notifications = notificationRepository.findByStatus(NotificationStatusEnum.UNDELIVERED);
        if (CollectionUtils.isEmpty(notifications)) {
            logManager.logINFO("Nao existem notificacoes nao entregues para reeenviar");
            return;
        }

        logManager.logINFO("Reenviando notificacoes nao entregues");

        for (BaseNotification notification : notifications) {
            resend(notification);
        }
    }

    private void resend(BaseNotification notification) {
        logManager.logDEBUG("Reenvinado notificacao [id=%s]", notification.getId());

        // Volta o status da notificacao pra pendente
        notificationRepository.updateStatusById(notification.getId(), NotificationStatusEnum.PENDING);

        // Envia pra fila de notificacoes
        messageSenderHelper.send(MessagingConstants.NOTIFICATIONS_QUEUE, notification.getId());
    }

    @Transactional
    public void process(long notificationId) {
        logManager.logDEBUG("Executando servico de processamento de Notificacao [id=%s]", notificationId);

        BaseNotification notification = notificationRepository.findOne(notificationId);
        if (notification == null)
            throw new ServiceException("Notificacao nao localizada [id=%s]", notificationId);

        BaseNotifyRequest notifyRequest = new BaseNotifyRequest();
        notifyRequest.setNotification(notification);
        notifyRequest.setRequestDate(DateTimeHelper.getNowDateTime());
        notifyRequestRepository.save(notifyRequest);

        String requestBody = notification.getMessageContent();
        String destinationUrl = notification.getDestinationUrl();
        logManager.logINFO("Notificacao enviada via requisicao HTTP para [" + destinationUrl + "]: " + requestBody);

        Integer httpStatus = null;
        String exceptionMessage = null;
        try {
            HttpResponse response = httpClientHelper.sendPostHttpRequest(destinationUrl, requestBody,
                ContentType.APPLICATION_JSON, Consts.UTF_8.name(), null, requestTimeout, false);
            httpStatus = response.getHttpStatus();

            logManager.logDEBUG("Notificacao HTTP entregue com sucesso [httpStatus=%s, body=%s]", httpStatus,
                response.getResponseBody());

        } catch (HttpClientRequestException e) {
            httpStatus = e.getHttpStatus();

            exceptionMessage = ExceptionUtils.getRootCauseMessage(e);
            logManager.logWARN("Erro entregando Notificacao via requisicao HTTP [%s]", exceptionMessage);

        } catch (Exception e) {
            exceptionMessage = ExceptionUtils.getRootCauseMessage(e);
            logManager.logWARN("Erro entregando Notificacao via requisicao HTTP [%s]", exceptionMessage);
        }

        LocalDateTime responseDate = DateTimeHelper.getNowDateTime();

        BaseNotifyResponse notifyResponse = new BaseNotifyResponse();
        notifyResponse.setNotifyRequest(notifyRequest);
        notifyResponse.setResponseDate(responseDate);
        notifyResponse.setHttpStatus(httpStatus);
        notifyResponse.setExceptionMessage(exceptionMessage);
        notifyResponseRepository.save(notifyResponse);

        if ((httpStatus != null) && (httpStatus == HttpStatus.SC_OK)) {
            // Atualiza o status da notificacao pra entregue
            notificationRepository.updateStatusAndDeliveryDateById(notification.getId(), NotificationStatusEnum.DELIVERED,
                responseDate);

        } else {
            // Se deu erro, verifica se pode enviar para uma nova tentativa de notificacao
            long count = notifyResponseRepository.countByNotifyRequestNotification(notification);

            if (count < retryLimit) {
                long retryDelayInSeconds = retryDelay / 1000;
                logManager.logINFO("Enviando para uma nova tentativa de notificacao daqui a %s segundos [tentativas=%s]",
                    retryDelayInSeconds, count);

                // Reennvia pra fila de notificacoes
                messageSenderHelper.send(MessagingConstants.NOTIFICATIONS_QUEUE, notification.getId(), retryDelay);

            } else {
                logManager.logDEBUG("Estourado o limite de retentativas [" + count + "]");

                // Atualiza o status da notificacao pra nao entregue
                notificationRepository.updateStatusById(notification.getId(), NotificationStatusEnum.UNDELIVERED);
            }
        }

        logManager.logDEBUG("Finalizando servico de processamento de Notificacao");
    }

}
