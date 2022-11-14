package tech.jannotti.billing.core.services.user;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.persistence.repository.base.user.UserRepository;
import tech.jannotti.billing.core.services.AbstractService;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

public abstract class AbstractUserService extends AbstractService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Transactional
    protected void updatePassword(BaseUser user, String password) {

        if (!user.getStatus().equals(EntityStatusEnum.ACTIVE))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_USER_STATUS_TO_UPDATE);

        String encodedPassword = passwordEncoder.encode(password);
        // TODO Criar um servico de validacao de formato da senha

        userRepository.updatePasswordById(user.getId(), encodedPassword);
    }

}
