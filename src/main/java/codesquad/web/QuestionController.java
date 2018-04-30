package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.dto.QuestionDto;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/questions")
public class QuestionController {
    @Resource(name = "qnaService")
    private QnaService qnaService;

    @GetMapping("/{id}")
    public String get(Model model, @PathVariable long id) {
        model.addAttribute("question", qnaService.findById(id));
        return "/qna/show";
    }

    @PostMapping("")
    public String create(@LoginUser User loginUser, QuestionDto questionDto){
        Question question = qnaService.create(loginUser, questionDto.toQuestion());
        return String.format("redirect:%s", question.generateUrl());
        //model.addAttribute("question", question);
        //return "/qna/show";
        //return "redirect:/qna/show";
    }
}
