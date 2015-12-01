package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceous;
import it.geosolutions.opensdi2.persistence.PlotModuleHerbaceousDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleHerbaceousService {

    @Autowired
    private PlotModuleHerbaceousDao plotModuleHerbaceousDao;

    public List<PlotModuleHerbaceous> getAll() {
        return plotModuleHerbaceousDao.findAll();
    }
}
