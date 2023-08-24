package com.springMVC.practice.dao;

import com.springMVC.practice.dto.BDto;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

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

    public void write(String bName, String bTitle, String bContent){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = DriverManager.getConnection(url, username, password);
            String query = "insert into board_mvc (bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) " +
                    "values (?,?,?,0,0,0,0)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, bName);
            preparedStatement.setString(2, bTitle);
            preparedStatement.setString(3, bContent);

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public BDto contentView(String strId){
        upHit(strId);

        BDto dto = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection(url, username, password);
            String query = "select * from board_mvc where bId = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, parseInt(strId));
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                int bId = resultSet.getInt("bId");
                String bName =resultSet.getString("bName");
                String bTitle =resultSet.getString("bTitle");
                String bContent =resultSet.getString("bContent");
                Timestamp bDate =resultSet.getTimestamp("bDate");
                int bHit = resultSet.getInt("bHit");
                int bGroup = resultSet.getInt("bGroup");
                int bStep = resultSet.getInt("bStep");
                int bIndent = resultSet.getInt("bIndent");

                dto = new BDto(bId, bName, bTitle, bContent, bDate, bHit, bGroup, bStep, bIndent);
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

        return dto;
    }

    // 조회수 증가
    private void upHit(String bId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = DriverManager.getConnection(url, username, password);
            String query = "update board_mvc set bHit = bHit + 1 where bId = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, bId);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
