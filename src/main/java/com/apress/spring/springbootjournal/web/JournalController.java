package com.apress.spring.springbootjournal.web;

import com.apress.spring.springbootjournal.domain.Journal;
import com.apress.spring.springbootjournal.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class JournalController {

    @Autowired
    JournalRepository repo;

    // Journal 레코드 전체 조회용 웹 컨트롤러
    @RequestMapping("/") // 기본 경로 '/' 요청 담당 핸들러
    public String index(Model model) {
        model.addAttribute("journal", repo.findAll());
        return "index";
    }

    // ResponseBody 선언을 통해 Spring MVC에 의하여 produces에 선언한 JSON 데이터로 변환
    @RequestMapping(value="/journal", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public @ResponseBody List<Journal> getJuurnal() {
        return repo.findAll();
    }
}
