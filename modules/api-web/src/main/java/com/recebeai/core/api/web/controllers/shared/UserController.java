package tech.jannotti.billing.core.api.web.controllers.shared;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.request.user.UpdateLoggedUserPasswordRestRequest;
import tech.jannotti.billing.core.api.web.controllers.dto.response.user.CurrentUserRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.persistence.model.base.user.BaseUser;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.rest.controllers.dto.response.RestResponseDTO;
import tech.jannotti.billing.core.rest.validation.ValidateBody;
import tech.jannotti.billing.core.services.user.UserService;

@RestController
@RequestMapping(ApiConstants.V1_API_PATH + "user")
public class UserController extends AbstractWebController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/current")
    @InfoLogging
    public CurrentUserRestResponse currentUser() {
        BaseUser user = getLoggedUser();

        Map<String, Object> environment = userService.getEnvironment(user);

        BaseResultCode resultCode = getSuccessResultCode();
        return new CurrentUserRestResponse(resultCode, dtoMapper, user, environment);
    }

    @PutMapping(value = "/password")
    @InfoLogging
    @ValidateBody
    public RestResponseDTO updatePassword(@RequestBody UpdateLoggedUserPasswordRestRequest request) {
        BaseUser user = getLoggedUser();

        userService.updatePassword(user, request.getCurrentPassword(), request.getNewPassword());
        return createSuccessResponse();
    }

}
