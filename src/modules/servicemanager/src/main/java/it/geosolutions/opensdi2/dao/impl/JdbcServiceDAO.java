/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package it.geosolutions.opensdi2.dao.impl;

import it.geosolutions.opensdi2.dao.ServiceDAO;
import it.geosolutions.opensdi2.model.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * <pre>
 * {@code
 * CREATE TABLE service (
 *  "ID" serial,
 *  "SERVICE_ID" text not null,
 *  "PARENT" text not null,
 *  PRIMARY KEY ("ID")
 * )
 * }
 * </pre>
 * 
 * @author Alessio
 * 
 */
public class JdbcServiceDAO implements ServiceDAO {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Service service) {

        String sql = "INSERT INTO SERVICE (SERVICE_ID, PARENT) VALUES (?, ?)";
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, service.getServiceId());
            ps.setString(2, service.getParent());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

    }

    @Override
    public Service findByServiceId(String serviceId) {

        String sql = "SELECT * FROM SERVICE WHERE SERVICE_ID = ?";

        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, serviceId);
            Service service = null;
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                service = new Service(rs.getString("SERVICE_ID"), rs.getString("PARENT"));
                service.setId(rs.getInt("ID"));
            }
            rs.close();
            ps.close();
            return service;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

    }

}
