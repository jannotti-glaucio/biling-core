package tech.jannotti.billing.core.services.user;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import tech.jannotti.billing.core.commons.util.DateTimeHelper;
import tech.jannotti.billing.core.constants.ResultCodeConstants;
import tech.jannotti.billing.core.persistence.enums.EntityStatusEnum;
import tech.jannotti.billing.core.persistence.model.base.BaseRole;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.persistence.repository.base.RoleRepository;
import tech.jannotti.billing.core.services.dto.request.user.AddUserServiceRequest;
import tech.jannotti.billing.core.services.dto.request.user.UpdateUserServiceRequest;
import tech.jannotti.billing.core.services.exception.ResultCodeServiceException;

public abstract class AbstractUserEntityService extends AbstractUserService {

    @Autowired
    protected RoleRepository roleRepository;

    protected <T extends BaseUser> T add(Class<T> type, AddUserServiceRequest request, String roleCode) {

        if (userRepository.existsByUsername(request.getUserName()))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_DUPLICATED_USERNAME, request.getUserName());

        T user = dtoMapper.map(request, type);

        BaseRole role = roleRepository.getByCode(roleCode);

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        // TODO Criar um servico de validacao de formato da senha

        user.setToken(generateToken());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setStatus(EntityStatusEnum.ACTIVE);
        user.setCreationDate(DateTimeHelper.getNowDateTime());

        return user;
    }

    private String generateToken() {
        return tokenGenerator.generateRandomHexToken("user.token", 12);
    }

    protected void update(BaseUser user, UpdateUserServiceRequest request) {

        if (!user.getStatus().equals(EntityStatusEnum.ACTIVE))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_INVALID_USER_STATUS_TO_UPDATE);

        if (userRepository.existsByUsernameAndTokenNot(request.getUserName(), user.getToken()))
            throw new ResultCodeServiceException(ResultCodeConstants.CODE_DUPLICATED_USERNAME, request.getUserName());

        dtoMapper.map(request, user);
    }

    @Transactional
    protected void block(BaseUser user) {

        if (!user.getStatus().equals(EntityStatusEnum.ACTIVE))
            throw new ResultCodeServiceException(CODE_INVALID_USER_STATUS_TO_BLOCK);

        userRepository.updateStatusById(user.getId(), EntityStatusEnum.BLOCKED);
    }

    @Transactional
    protected void unblock(BaseUser user) {

        if (!user.getStatus().equals(EntityStatusEnum.BLOCKED))
            throw new ResultCodeServiceException(CODE_INVALID_USER_STATUS_TO_UNBLOCK);

        userRepository.updateStatusById(user.getId(), EntityStatusEnum.ACTIVE);
    }

    @Transactional
    protected void delete(BaseUser user) {

        if (user.getStatus().equals(EntityStatusEnum.DELETED))
            throw new ResultCodeServiceException(CODE_INVALID_USER_STATUS_TO_DELETE);

        LocalDateTime deletionDate = DateTimeHelper.getNowDateTime();
        userRepository.updateStatusAndDeletionDateById(user.getId(), EntityStatusEnum.DELETED, deletionDate);
    }

}
