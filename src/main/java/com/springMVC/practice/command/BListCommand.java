package com.springMVC.practice.command;

import com.springMVC.practice.dao.BDao;
import com.springMVC.practice.dto.BDto;
import org.springframework.ui.Model;

import java.util.ArrayList;

public class BListCommand implements BCommand{
    @Override
    public void execute(Model model) {
        // DAO에서 구성한 리스트 반환
        BDao dao = new BDao();
        ArrayList<BDto> dtos = dao.list();
        model.addAttribute("list", dtos);
    }
}
