package com.springMVC.practice.dao;

import com.springMVC.practice.dto.BDto;
import com.springMVC.practice.util.Constant;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.*;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class BDao {
    JdbcTemplate template = null;
    public BDao() {
        this.template = Constant.template; // Bean으로 생성된 JDBC Template 사용
    }

    public ArrayList<BDto> list(){
        // query문을 실행한 결과를 BeanPropertyRowMapper 객체에 저장
        String query = "select * from board_mvc order by bGroup desc, bStep asc";
        return (ArrayList<BDto>) template.query(query, new BeanPropertyRowMapper<BDto>(BDto.class));
    }

    public void write(final String bName, final String bTitle, final String bContent){
        
        int group_num = get_max_id().getbId();
        String query = "insert into board_mvc (bName, bTitle, bContent, bHit, bGroup, bStep, bIndent) values (?,?,?,0,?,0,0)";
        template.update(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, bName);
                ps.setString(2, bTitle);
                ps.setString(3, bContent);
                ps.setInt(4, group_num);
            }
        });
    }

    private BDto get_max_id() {
        int group_num = 1;
        String query = "select bId from board_mvc order by bId desc limit 1";
        return template.queryForObject(query, new BeanPropertyRowMapper<BDto>(BDto.class));
    }

    public BDto contentView(String strId){
        upHit(strId);
        String query = "select * from board_mvc where bId = " + strId;
        return template.queryForObject(query, new BeanPropertyRowMapper<BDto>(BDto.class));
    }

    public void modify(String bId, String bName, String bTitle, String bContent){
        String query = "update board_mvc set bName=?, bTitle=?, bContent=? where bId=?";
        template.update(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, bName);
                ps.setString(2, bTitle);
                ps.setString(3, bContent);
                ps.setInt(4, parseInt(bId));
            }
        });
    }

    public void delete(String strId){
        String query = "delete from board_mvc where bId=?";
        template.update(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, parseInt(strId));
            }
        });
    }

    public BDto reply_view(String strId){
        String query = "select * from board_mvc where bId = " + strId;
        return template.queryForObject(query, new BeanPropertyRowMapper<BDto>(BDto.class));
    }

    public void reply(final String bName, final String bTitle, final String bContent, final String bGroup, final String bStep, final String bIndent){
        String query = "insert into board_mvc (bName, bTitle, bContent, bGroup, bStep, bIndent) values (?,?,?,?,?,?)";
        template.update(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, bName);
                ps.setString(2, bTitle);
                ps.setString(3, bContent);
                ps.setInt(4, parseInt(bGroup));
                ps.setInt(5, parseInt(bStep) + 1);
                ps.setInt(6, parseInt(bIndent) + 1);
            }
        });
    }

    private void reply_shape(final String bGroup, final String bStep) {
        String query = "update board_mvc set bStep=bStep+1 where bGroup=? and bStep > ?";
        template.update(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, parseInt(bGroup));
                ps.setInt(2, parseInt(bStep));
            }
        });
    }

    // 조회수 증가
    private void upHit(final String bId){ // bId 불변하도록 final 키워드 붙임
        String query = "update board_mvc set bHit = bHit + 1 where bId = ?";
        template.update(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setInt(1, parseInt(bId));
            }
        });
    }
}
