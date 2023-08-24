package com.springMVC.practice.command;

import com.springMVC.practice.dao.BDao;
import com.springMVC.practice.dto.BDto;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BReplyViewCommand implements BCommand{
    @Override
    public void execute(Model model) {
        Map<String, Object> map = model.asMap();
        HttpServletRequest request = (HttpServletRequest) map.get("request");
        String bId = request.getParameter("bId");

        BDao dao = new BDao();
        BDto dto = dao.reply_view(bId);

        model.addAttribute("reply_view", dto);
    }
}
