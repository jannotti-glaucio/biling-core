package tech.jannotti.billing.core.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tech.jannotti.billing.core.persistence.model.base.help.BaseHelpVideo;
import tech.jannotti.billing.core.persistence.repository.base.HelpVideoRepository;

@Service
public class HelpService extends AbstractService {

    @Autowired
    private HelpVideoRepository helpVideoRepository;

    public List<BaseHelpVideo> findVideos() {
        return helpVideoRepository.findAll();
    }

}
