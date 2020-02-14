package com.apress.spring.springbootjournal.web;

import com.apress.spring.springbootjournal.domain.Journal;
import com.apress.spring.springbootjournal.repository.JournalRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class JournalController {
    private static Logger log = LoggerFactory.getLogger(JournalController.class);

    @Autowired
    JournalRepository repository;

    // Journal 레코드 전체 조회용 웹 컨트롤러
    @RequestMapping("/") // 기본 경로 '/' 요청 담당 핸들러
    public String index(Model model) throws Exception {
        model.addAttribute("journal", repository.findAll());
        log.info("root init :: ");
        return "index";
    }

    // ResponseBody 선언을 통해 Spring MVC에 의하여 produces에 선언한 JSON 데이터로 변환
    @GetMapping("/journalList")
    public @ResponseBody List<Journal> getJournalList() throws Exception {
        log.info("journal init :: ");
        return repository.findAll();
    }

    // TODO : PostMapping으로 변경
    @GetMapping("/setJournal")
    public @ResponseBody String setJournal(@RequestParam(value = "title") String title,
                                           @RequestParam(value = "summary") String summary,
                                           @RequestParam(value = "date") String date) throws Exception  {
        /* [ Init Param Example ]
         * title : String | summary : String | date : String(format : dd/MM/yyyy)
         */
        log.info("[init setJournal Param] title : " + title + " / summary : " + summary + " / date : " + date);
        repository.save(new Journal(title, summary, date));
        return "success";
    }

    @GetMapping("/getJournal/{id}")
    public @ResponseBody Object getJournal(@PathVariable(value = "id") Long id) throws Exception {
        // repository에 데이터가 없는 경우가 있을 수 있으므로 return type - Optional<Journal)
        log.info("[init getJournal Param] title : " + id);
        // findById()의 return type > Optional
        Optional<Journal> journal = repository.findById(id);

        if (!journal.isPresent()) {
            return "no such data";
        }
        return journal;
    }

    @GetMapping("/getJournalCount")
    public @ResponseBody long getJournalListCount() throws Exception {
        log.info("[init getJournalListCount]");
        return repository.count();
    }
}
