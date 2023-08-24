package com.springMVC.practice.command;

import com.springMVC.practice.dao.BDao;
import com.springMVC.practice.dto.BDto;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class BContentCommand implements BCommand{

    @Override
    public void execute(Model model) {
        // Content를 확인하기 위해 id 구하기
        Map<String, Object> map = model.asMap(); // Model에서 Map 형식으로 값 구하기
        HttpServletRequest request = (HttpServletRequest) map.get("request"); // Model에서 HttpServletRequest 구하기
        String bId = request.getParameter("bId"); // Model에서 id 구하기

        BDao dao = new BDao();
        BDto dto = dao.contentView(bId);

        model.addAttribute("content_view", dto);
    }
}
