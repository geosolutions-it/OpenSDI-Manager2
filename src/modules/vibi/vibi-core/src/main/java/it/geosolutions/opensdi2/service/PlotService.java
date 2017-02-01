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

import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.Plot;
import it.geosolutions.opensdi2.persistence.PlotDao;
import it.geosolutions.opensdi2.persistence.derivated.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(value = "opensdiTransactionManager")
public class PlotService extends BaseService<Plot, String> {

    @Autowired
    private PlotDao plotDao;

    @Override
    protected GenericVibiDao<Plot, String> getDao() {
        return plotDao;
    }

    @Override
    public void persist(Plot entity) {
        handleDerivated(entity);
        super.persist(entity);
    }

    @Override
    public void merge(String id, Plot entity) {
        handleDerivated(entity);
        super.merge(id, entity);
    }

    private void handleDerivated(Plot entity) {
        persistDerivated(entity.getVegclass(), new VegClass());
        persistDerivated(entity.getHgmclass(), new HgmClass());
        persistDerivated(entity.getHgmsubclass(), new HgmSubClass());
        persistDerivated(entity.getHgmgroup(), new HgmGroup());
        persistDerivated(entity.getLandformType(), new LandFormType());
        persistDerivated(entity.getHomogeneity(), new Homogeneity());
        persistDerivated(entity.getStandSize(), new StandSize());
        persistDerivated(entity.getDrainage(), new Drainage());
        persistDerivated(entity.getSalinity(), new Salinity());
        persistDerivated(entity.getHydrologicRegime(), new HydrologicRegime());
        persistDerivated(entity.getOneoDisturbanceType(), new DisturbanceType());
        persistDerivated(entity.getOneoDisturbanceSeverity(), new DisturbanceSeverity());
        persistDerivated(entity.getTwooDisturbanceType(), new DisturbanceType());
        persistDerivated(entity.getTwooDisturbanceSeverity(), new DisturbanceSeverity());
        persistDerivated(entity.getThreeoDisturbanceType(), new DisturbanceType());
        persistDerivated(entity.getThreeoDisturbanceSeverity(), new DisturbanceSeverity());
    }
}
