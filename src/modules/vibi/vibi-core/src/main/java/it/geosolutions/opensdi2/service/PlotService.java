package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.Plot;
import it.geosolutions.opensdi2.persistence.PlotDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "opensdiTransactionManager")
public class PlotService {

    @Autowired
    private PlotDao plotDao;

    public List<Plot> getAll() {
        return plotDao.findAll();
    }
}
