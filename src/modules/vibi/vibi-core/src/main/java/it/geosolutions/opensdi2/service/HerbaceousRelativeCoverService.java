package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCover;
import it.geosolutions.opensdi2.persistence.HerbaceousRelativeCoverDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "opensdiTransactionManager")
public class HerbaceousRelativeCoverService {

    @Autowired
    private HerbaceousRelativeCoverDao herbaceousRelativeCoverDao;

    public List<HerbaceousRelativeCover> getAll() {
        return herbaceousRelativeCoverDao.findAll();
    }
}
