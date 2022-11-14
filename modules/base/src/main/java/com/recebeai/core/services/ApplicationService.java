package tech.jannotti.billing.core.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseApplication;
import tech.jannotti.billing.core.persistence.model.base.dealer.BaseDealer;
import tech.jannotti.billing.core.persistence.repository.base.ApplicationRepository;
import tech.jannotti.billing.core.services.dto.request.ApplicationServiceRequest;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

@Service
public class ApplicationService extends AbstractService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<BaseApplication> find(BaseDealer dealer) {
        return applicationRepository.findByDealerAndStatusNot(dealer, EntityStatusEnum.DELETED);
    }

    public BaseApplication get(BaseDealer dealer, String token) {

        BaseApplication application = applicationRepository.getByDealerAndTokenAndStatusNot(dealer, token,
            EntityStatusEnum.DELETED);
        if (application == null)
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_TOKEN_NOT_FOUND, token);

        return application;
    }

    public BaseApplication get(String clientId) {
        return applicationRepository.getByClientId(clientId);
    }

    public boolean isActive(BaseApplication application) {

        if (!application.getStatus().equals(EntityStatusEnum.ACTIVE))
            return false;

        if (!application.getDealer().getStatus().equals(EntityStatusEnum.ACTIVE))
            return false;

        if (!application.getDealer().getCompany().getStatus().equals(EntityStatusEnum.ACTIVE))
            return false;

        return true;
    }

    public boolean compareClientSecret(BaseApplication application, String clientSecret) {
        return passwordEncoder.matches(clientSecret, application.getClientSecret());
    }

    public BaseApplication add(BaseDealer dealer, ApplicationServiceRequest request) {

        BaseApplication application = dtoMapper.map(request, BaseApplication.class);

        String clientSecret = generateClientSecret();
        String encodedClientSecret = passwordEncoder.encode(clientSecret);

        application.setDealer(dealer);
        application.setToken(generateToken());
        application.setClientId(generateClientId());
        application.setClientSecret(encodedClientSecret);
        application.setStatus(EntityStatusEnum.ACTIVE);
        application.setCreationDate(DateTimeHelper.getNowDateTime());
        applicationRepository.save(application);

        application.setPlainClientSecret(clientSecret);
        return application;
    }

    private String generateToken() {
        return tokenGenerator.generateRandomHexToken("application.token", 9);
    }

    private String generateClientId() {
        return tokenGenerator.generateRandomHexToken("application.clientId", 30);
    }

    private String generateClientSecret() {
        return tokenGenerator.generateRandomHexToken("application.clientSecret", 60);
    }

    @Transactional
    public void update(BaseDealer dealer, String token, ApplicationServiceRequest request) {

        BaseApplication application = get(dealer, token);

        if (!application.getStatus().equals(EntityStatusEnum.ACTIVE))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_APPLICATION_STATUS_TO_UPDATE, token);

        dtoMapper.map(request, application);
        applicationRepository.save(application);
    }

    @Transactional
    public BaseApplication generateSecret(BaseDealer dealer, String token) {

        BaseApplication application = get(dealer, token);

        if (!application.getStatus().equals(EntityStatusEnum.ACTIVE))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_APPLICATION_STATUS_TO_UPDATE, token);

        String clientSecret = generateClientSecret();
        String encodedClientSecret = passwordEncoder.encode(clientSecret);
        applicationRepository.updateClientSecretById(application.getId(), encodedClientSecret);

        application.setPlainClientSecret(clientSecret);
        return application;
    }

    @Transactional
    public void delete(BaseDealer dealer, String token) {

        BaseApplication application = get(dealer, token);

        if (application.getStatus().equals(EntityStatusEnum.DELETED))
            throw new ResultCodeServiceException(CODE_INVALID_APPLICATION_STATUS_TO_DELETE);

        LocalDateTime deletionDate = DateTimeHelper.getNowDateTime();
        applicationRepository.updateStatusAndDeletionDateById(application.getId(), EntityStatusEnum.DELETED, deletionDate);
    }

}
