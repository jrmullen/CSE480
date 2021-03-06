package com.watchdog.dao.device;

/**
 * Created by BBuck on 10/4/16.
 */

import com.watchdog.business.Device;
import com.watchdog.business.Video;
import com.watchdog.dao.Constants;
import com.watchdog.dao.video.VideoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DeviceDaoImpl implements DeviceDao {

    @Autowired
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Override
    public void save (Device device) {

        Object[] args = new Object[]{device.getDeviceName(), device.getDeviceMac(), device.getDeviceAddress(), device.getDevicePort()};

        int out = jdbcTemplate.update(Constants.CREATE_DEVICE_QUERY, args);

        if (out !=0) {
            System.out.println("Device " + device.getDeviceName() + " " + device.getDeviceMac() + " " + device.getDeviceAddress()
            + " saved");
        } else System.out.println("Device " + device.getDeviceName() + " " + device.getDeviceMac() + " " + device.getDeviceAddress()
                + " failed");

    }

    @Override
    public Device getById(int id) {

        //using RowMapper anonymous clas, we can create a separate RowMapper for reuse
        Device device = jdbcTemplate.queryForObject(Constants.GET_BY_DEVICE_ID_QUERY, new Object[]{id}, new RowMapper<Device>() {

            @Override
            public Device mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                Device device = new Device();
                device.setId(rs.getInt("DEVICE_ID"));
                device.setUserId(rs.getInt("USER_ID"));
                device.setDeviceName(rs.getString("DEVICE_NAME"));
                device.setDeviceMac(rs.getString ("DEVICE_MAC"));
                device.setDeviceAddress(rs.getString("DEVICE_ADDRESS"));
                device.setDevicePort(rs.getString("DEVICE_PORT"));

                return device;
            }
        });
        return device;
    }

    @Override
    public boolean checkDeviceNameExists(String deviceName){
        //using RowMapper anonymous class, we can create a separate RowMapper for reuse
        try {
            Device user = jdbcTemplate.queryForObject(Constants.SELECT_DEVICE_NAME_QUERY, new Object[]{deviceName}, new RowMapper<Device>() {

                @Override
                public Device mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    Device device = new Device();
                    device.setDeviceName(rs.getString("DEVICE_NAME"));
                    return device;
                }
            });
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    @Override
    public boolean checkMacExists(String mac){
        //using RowMapper anonymous class, we can create a separate RowMapper for reuse
        try {
            Device user = jdbcTemplate.queryForObject(Constants.SELECT_MAC_QUERY, new Object[]{mac}, new RowMapper<Device>() {

                @Override
                public Device mapRow(ResultSet rs, int rowNum)
                        throws SQLException {
                    Device device = new Device();
                    device.setDeviceMac(rs.getString("DEVICE_MAC"));
                    return device;
                }
            });
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    @Override
    public String getDeviceNameByVidId(int id) {
        //Initialize database and create Dao object
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
        VideoDao videoDao = ctx.getBean("videoDaoImpl", VideoDao.class); //first parameter is the id found in the spring.xml file
        Video video = videoDao.getByVidId(id);

        //using RowMapper anonymous class, we can create a separate RowMapper for reuse
        Device device = jdbcTemplate.queryForObject(Constants.GET_DEVICE_BY_DEVICE_MAC_QUERY, new Object[]{video.getDeviceMac()}, new RowMapper<Device>() {

            @Override
            public Device mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                Device device = new Device();
                device.setDeviceName(rs.getString("DEVICE_NAME"));
                return device;
            }

        });
        return device.getDeviceName();
    }

    @Override
    public void update(Device device) {

        Object[] args = new Object[]{device.getDeviceName(), device.getDeviceAddress(), device.getDeviceMac(), device.getDevicePort()};

        int out = jdbcTemplate.update(Constants.UPDATE_BY_DEVICE_ID_QUERY, args);
        if (out !=0) {
            System.out.println("Device updated with id= " + device.getId() +
                                            "  MAC= " + device.getDeviceMac());
        } else System.out.println("No device found with id= " + device.getId() +
                                            "  MAC= " + device.getDeviceMac());
    }


    @Override
    public void deleteById(int id) {

        int out = jdbcTemplate.update(Constants.DELETE_DEVICE_BY_ID_QUERY, id);
        if (out !=0) {
            System.out.println("Device deleted with id= " + id);
        } else System.out.println("No device found with id= " + id);
    }

    @Override
    public List<Device> getAll() {


        List<Device> deviceList = new ArrayList<Device>();

        List<Map<String, Object>> deviceRows = jdbcTemplate.queryForList(Constants.GET_ALL_DEVICES_QUERY);

        for (Map<String, Object> deviceRow : deviceRows) {
            Device device = new Device();
            device.setId(Integer.parseInt(String.valueOf(deviceRow.get("DEVICE_ID"))));
            device.setUserId(Integer.parseInt(String.valueOf(deviceRow.get("USER_ID"))));
            device.setDeviceName(String.valueOf(deviceRow.get("DEVICE_NAME")));
            device.setDeviceMac(String.valueOf(deviceRow.get("DEVICE_MAC")));
            device.setDeviceAddress(String.valueOf(deviceRow.get("DEVICE_ADDRESS")));
            device.setDevicePort(String.valueOf(deviceRow.get("DEVICE_PORT")));
            device.buildDeviceUrl(device.getDeviceAddress(), device.getDevicePort());

            deviceList.add(device);
        }
        return deviceList;
    }
}