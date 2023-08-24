package com.springMVC.practice.dao;

import com.springMVC.practice.dto.BDto;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class BDao {
    String url;
    String username;
    String password;
    public BDao() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://localhost:3306/spring_practice";
            username = "root";
            password = "0000";

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<BDto> list(){

        ArrayList<BDto> dtos = new ArrayList<BDto>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection(url, username, password);
            String query = "select * from board_mvc order by bGroup desc, bStep asc";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                int bId = resultSet.getInt("bId");
                String bName =resultSet.getString("bName");
                String bTitle =resultSet.getString("bTitle");
                String bContent =resultSet.getString("bContent");
                Timestamp bDate =resultSet.getTimestamp("bDate");
                int bHit = resultSet.getInt("bHit");
                int bGroup = resultSet.getInt("bGroup");
                int bStep = resultSet.getInt("bStep");
                int bIndent = resultSet.getInt("bIndent");

                BDto dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
                dtos.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return dtos;
    }
}
