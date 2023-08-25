package com.springMVC.practice.controller;

import com.springMVC.practice.command.*;
import com.springMVC.practice.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

@Controller
public class BController {
    BCommand command; // 각 페이지에서 실행할 커맨드 생성
    Logger logger = LoggerFactory.getLogger(BController.class);

    // JDBC Template Setter
    JdbcTemplate template;
    @Autowired
    public void setTemplate(JdbcTemplate template) {
        this.template = template;
        Constant.template = this.template;
    }

    @RequestMapping("/")
    public String home(Locale locale, Model model) {
        logger.info("Welcome home! The client locale is {}.", locale);

        Date date = new Date();
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate );

        return "home";
    }

    // 글 리스트 보이기
    @RequestMapping("/list")
    public String list(Model model){
        // execute list command
        command = new BListCommand();
        command.execute(model);
        
        return "list";
    }
    
    // 글 작성 페이지 보이기
    @RequestMapping("/write_view")
    public String write_view(Model model){
        
        return "write_view";
    }
    
    // 글 작성 페이지
    @RequestMapping("/write")
    public String write(HttpServletRequest request, Model model){
        
        // 글 작성 기능 수행
        model.addAttribute("request", request);
        command = new BWriteCommand();
        command.execute(model);
        
        return "redirect:list"; // 글 작성 마치고 리스트 페이지로 이동
    }
    
    // 글 내용 보는 페이지
    @RequestMapping("/content_view")
    public String content_view(HttpServletRequest request, Model model){
        
        // 글 보이기 기능 수행
        model.addAttribute("request", request);
        command = new BContentCommand();
        command.execute(model);
        
        return "content_view";
    }

    // 글 수정 페이지
    @RequestMapping(method = RequestMethod.POST, value = "/modify")
    public String modify(HttpServletRequest request, Model model){
        // 글 수정 기능 수행
        model.addAttribute("request", request);
        command = new BModifyCommand();
        command.execute(model);

        return "redirect:list"; // 글 수정 마친 후 리스트로 이동
    }

    // 답변 확인 페이지
    @RequestMapping("/reply_view")
    public String reply_view(HttpServletRequest request, Model model){
        // 답글 확인 기능 수행
        model.addAttribute("request", request);
        command = new BReplyViewCommand();
        command.execute(model);

        return "reply_view";
    }

    // 답변 작성 페이지
    @RequestMapping("/reply")
    public String reply(HttpServletRequest request, Model model){

        // 답글 작성 기능 수행
        model.addAttribute("request", request);
        command = new BReplyCommand();
        command.execute(model);

        return "redirect:list"; // 답글 작성 후 리스트로 이동
    }

    // 글 삭제 페이지
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, Model model){

        // 글 삭제 기능 수행
        model.addAttribute("request", request);
        command = new BDeleteCommand();
        command.execute(model);

        return "redirect:list"; // 글 삭제 후 리스트로 이동
    }
}
