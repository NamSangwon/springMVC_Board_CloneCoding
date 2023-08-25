package com.springMVC.practice.dao;

import com.springMVC.practice.dto.BDto;
import com.springMVC.practice.util.Constant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

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
    JdbcTemplate template = null;
    public BDao() {
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//            url = "jdbc:mysql://localhost:3306/spring_practice";
//            username = "root";
//            password = "0000";
//
//        } catch (Exception e){
//            e.printStackTrace();
//        }
        template = Constant.template; // Bean으로 생성된 JDBC Template 사용
    }

    public ArrayList<BDto> list(){
        ArrayList<BDto> dtos = null;

        String query = "select * from board_mvc order by bGroup desc, bStep asc";
        // query문을 실행한 결과를 BeanPropertyRowMapper 객체에 저장
        dtos = (ArrayList<BDto>) template.query(query, new BeanPropertyRowMapper<BDto>(BDto.class));

        return dtos;
    }

    public void write(String bName, String bTitle, String bContent){
        
        int group_num = get_max_id();
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = DriverManager.getConnection(url, username, password);
            String query = "insert into board_mvc (bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) values (?,?,?,0,?,0,0)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, bName);
            preparedStatement.setString(2, bTitle);
            preparedStatement.setString(3, bContent);
            preparedStatement.setInt(4, group_num);
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

    private int get_max_id() {
        int group_num = 1;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection(url, username, password);
            String query = "select bId from board_mvc order by bId desc limit 1";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) group_num = resultSet.getInt("bId")+1;

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

        return group_num;
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

    public void modify(String bId, String bName, String bTitle, String bContent){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = DriverManager.getConnection(url, username, password);
            String query = "update board_mvc set bName=?, bTitle=?, bContent=? where bId=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,bName);
            preparedStatement.setString(2,bTitle);
            preparedStatement.setString(3,bContent);
            preparedStatement.setInt(4, parseInt(bId));

            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void delete(String strId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = DriverManager.getConnection(url, username, password);
            String query = "delete from board_mvc where bId=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, parseInt(strId));

            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public BDto reply_view(String strId){
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

        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return dto;
    }

    public void reply(String bName, String bTitle, String bContent, String bGroup, String bStep, String bIndent){
        reply_shape(bGroup, bStep);
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = DriverManager.getConnection(url, username, password);
            String query = "insert into board_mvc (bName, bTitle, bContent, bGroup, bStep, bIndent) values (?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, bName);
            preparedStatement.setString(2, bTitle);
            preparedStatement.setString(3, bContent);
            preparedStatement.setInt(4, parseInt(bGroup));
            preparedStatement.setInt(5, parseInt(bStep)+1);
            preparedStatement.setInt(6, parseInt(bIndent)+1);

            preparedStatement.executeUpdate();

        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void reply_shape(String bGroup, String bStep) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = DriverManager.getConnection(url, username, password);
            String query = "update board_mvc set bStep=bStep+1 where bGroup=? and bStep > ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, parseInt(bGroup));
            preparedStatement.setInt(2, parseInt(bStep));

        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
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
