package com.springMVC.practice.command;

import com.springMVC.practice.dao.BDao;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BDeleteCommand implements BCommand{
    @Override
    public void execute(Model model) {
        Map<String, Object> map = model.asMap();
        HttpServletRequest request = (HttpServletRequest) map.get("request");
        String bId = request.getParameter("bId");

        BDao dao = new BDao();
        dao.delete(bId);
    }
}
