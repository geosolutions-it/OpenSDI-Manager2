/*
 *  Copyright (C) 2016 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package it.geosolutions.opensdi2.service;

import it.geosolutions.opensdi2.persistence.*;
import it.geosolutions.opensdi2.persistence.derivated.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional(value = "opensdiTransactionManager")
public class PlotModuleHerbaceousService extends BaseService<PlotModuleHerbaceous, String>{

    @Autowired
    private PlotModuleHerbaceousDao plotModuleHerbaceousDao;


    @Override
    protected GenericVibiDao<PlotModuleHerbaceous, String> getDao() {
        return plotModuleHerbaceousDao;
    }

    @Override
    public void persist(PlotModuleHerbaceous entity) {
        entity.setFid(UUID.randomUUID().toString());
        handleDerivated(entity);
        super.persist(entity);
    }

    @Override
    public void merge(String id, PlotModuleHerbaceous entity) {
        handleDerivated(entity);
        super.merge(id, entity);
    }

    private void handleDerivated(PlotModuleHerbaceous entity) {
        persistDerivated(entity.getModuleId(), new Module());
        persistDerivated(entity.getCornerId(), new Corner());
        persistDerivated(entity.getDepth(), new Depth());
    }
}
