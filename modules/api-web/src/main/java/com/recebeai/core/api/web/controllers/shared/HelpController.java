package tech.jannotti.billing.core.api.web.controllers.shared;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.jannotti.billing.core.api.web.controllers.AbstractWebController;
import tech.jannotti.billing.core.api.web.controllers.dto.response.help.FindHelpVideosRestResponse;
import tech.jannotti.billing.core.commons.log.annotations.InfoLogging;
import tech.jannotti.billing.core.persistence.model.base.help.BaseHelpVideo;
import tech.jannotti.billing.core.persistence.model.base.resultcode.BaseResultCode;
import tech.jannotti.billing.core.rest.ApiConstants;
import tech.jannotti.billing.core.services.HelpService;

@RestController
@RequestMapping(ApiConstants.V1_API_PATH + "help/video")
public class HelpController extends AbstractWebController {

    @Autowired
    private HelpService helpService;

    @GetMapping("/")
    @InfoLogging
    public FindHelpVideosRestResponse find() {

        List<BaseHelpVideo> videos = helpService.findVideos();

        BaseResultCode resultCode = getQueryResultCode(videos);
        return new FindHelpVideosRestResponse(resultCode, dtoMapper, videos);
    }

}
