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

import it.geosolutions.opensdi2.persistence.BiomassRaw;
import it.geosolutions.opensdi2.persistence.BiomassRawDao;
import it.geosolutions.opensdi2.persistence.GenericVibiDao;
import it.geosolutions.opensdi2.persistence.PlotModuleWoodyRaw;
import it.geosolutions.opensdi2.persistence.derivated.BiomassAccuracy;
import it.geosolutions.opensdi2.persistence.derivated.Corner;
import it.geosolutions.opensdi2.persistence.derivated.DbhClass;
import it.geosolutions.opensdi2.persistence.derivated.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(value = "opensdiTransactionManager")
public class BiomassRawService extends BaseService<BiomassRaw, String> {

    @Autowired
    private BiomassRawDao biomassRawDao;

    @Override
    protected GenericVibiDao<BiomassRaw, String> getDao() {
        return biomassRawDao;
    }

    @Override
    public void persist(BiomassRaw entity) {
        entity.setFid(UUID.randomUUID().toString());
        handleDerivated(entity);
        super.persist(entity);
    }

    @Override
    public void merge(String id, BiomassRaw entity) {
        handleDerivated(entity);
        super.merge(id, entity);
    }

    private void handleDerivated(BiomassRaw entity) {
        persistDerivated(entity.getModuleId(), new Module());
        persistDerivated(entity.getCorner(), new Corner());
        persistDerivated(entity.getActualOrDerived(), new BiomassAccuracy());
    }
}
