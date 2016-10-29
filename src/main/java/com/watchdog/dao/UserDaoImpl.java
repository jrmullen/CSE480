package com.watchdog.dao;

/**
 * Created by jmullen on 9/20/16.
 */

import com.watchdog.business.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class UserDaoImpl implements UserDao {

    @Autowired
    private DataSource dataSource;
    
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);
    }

    @Override
    public void save(User user) {
        Object[] args = new Object[]{1, user.getFirstName(), user.getLastName(), user.getEmail(), user.getEncodedPassword()};

        int out = jdbcTemplate.update(Constants.CREATE_USER_QUERY, args);

        if (out != 0) {
            System.out.println("User " + user.getFirstName() + " " + user.getLastName() + " "
                                        + user.getEmail() + " saved");
        } else System.out.println("User " + user.getFirstName() + " " + user.getLastName() + " "
                                        + user.getEmail() + " failed");
    }

    @Override
    public User getById(int id) {

        //using RowMapper anonymous class, we can create a separate RowMapper for reuse
        User user = jdbcTemplate.queryForObject(Constants.GET_BY_USER_ID_QUERY, new Object[]{id}, new RowMapper<User>() {

            @Override
            public User mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                User user = new User();
                user.setId(rs.getInt("USER_ID"));
                user.setPermissionId(rs.getInt("PERMISS_ID"));
                user.setFirstName(rs.getString("USER_FNAME"));
                user.setLastName(rs.getString("USER_LNAME"));
                user.setEmail(rs.getString("USER_EMAIL"));
                return user;
            }
        });
        return user;
    }

    @Override
    public User getByEmail(String email){
        //using RowMapper anonymous class, we can create a separate RowMapper for reuse
        User user = jdbcTemplate.queryForObject(Constants.GET_BY_EMAIL_QUERY, new Object[]{email}, new RowMapper<User>() {

            @Override
            public User mapRow(ResultSet rs, int rowNum)
                    throws SQLException {
                User user = new User();
                user.setId(rs.getInt("USER_ID"));
                user.setPermissionId(rs.getInt("PERMISS_ID"));
                user.setFirstName(rs.getString("USER_FNAME"));
                user.setLastName(rs.getString("USER_LNAME"));
                user.setEmail(rs.getString("USER_EMAIL"));
                return user;
            }
        });
        return user;
    }

    @Override
    public void update(User user) {

        Object[] args = new Object[]{user.getFirstName(), user.getLastName(), user.getEmail(), user.getEncodedPassword(), user.getId()};

        int out = jdbcTemplate.update(Constants.UPDATE_USER_BY_ID_QUERY, args);
        if (out != 0) {
            System.out.println("User updated with id= " + user.getId());
        } else System.out.println("No User found with id= " + user.getId());
    }


    @Override
    public void deleteById(int id) {

        String query = "delete from user where USER_ID=?";

        int out = jdbcTemplate.update(Constants.DELETE_USER_BY_ID_QUERY, id);
        if (out != 0) {
            System.out.println("User deleted with id= " + id);
        } else System.out.println("No User found with id= " + id);
    }

    @Override
    public List<User> getAll() {

        List<User> userList = new ArrayList<User>();

        List<Map<String, Object>> userRows = jdbcTemplate.queryForList(Constants.GET_ALL_USERS_QUERY);

        for (Map<String, Object> userRow : userRows) {
            User user = new User();
            user.setId(Integer.parseInt(String.valueOf(userRow.get("USER_ID"))));
            user.setPermissionId(Integer.parseInt(String.valueOf(userRow.get("PERMISS_ID"))));
            user.setFirstName(String.valueOf(userRow.get("USER_FNAME")));
            user.setLastName(String.valueOf(userRow.get("USER_LNAME")));
            user.setEmail(String.valueOf(userRow.get("USER_EMAIL")));
            userList.add(user);
        }
        return userList;
    }

}

