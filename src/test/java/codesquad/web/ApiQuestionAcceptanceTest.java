package codesquad.web;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import codesquad.domain.*;
import codesquad.dto.QuestionDto;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import support.test.AcceptanceTest;

import java.util.List;

public class ApiQuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiQuestionAcceptanceTest.class);
    public static final User SANJIGI = new User(2L, "sanjigi", "test", "name", "sanjigi@slipp.net");

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private DeleteHistoryRepository deleteHistoryRepository;

    @Test
    public void create() throws Exception {
        QuestionDto newQuestion = createQuestionDto(4L);
        ResponseEntity<String> response = basicAuthTemplate().postForEntity("/api/questions", newQuestion, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        QuestionDto dbQuestion = getResource("/api/questions/4/", QuestionDto.class);
        assertTrue(dbQuestion.equals(newQuestion));
    }

    private QuestionDto createQuestionDto() {
        return new QuestionDto("제목입니다gg", "글입니다gg");
    }

    private QuestionDto createQuestionDto(long id) {
        return new QuestionDto(id, "제목입니다gg", "글입니다gg");
    }

    @Test
    public void show() throws Exception {
        ResponseEntity<String> response = basicAuthTemplate().getForEntity("/api/questions/1", String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        Question dbQuestion = questionRepository.findOne(1L);
        assertThat(response.getBody().contains(dbQuestion.getTitle()), is(true));
        assertThat(response.getBody().contains(dbQuestion.getContents()), is(true));
    }

    @Test
    public void delete_fail_noLogin() throws Exception {
        template().delete("/api/questions/2/", String.class);
        Question dbQuestion = questionRepository.findOne(2L);
        assertTrue(!dbQuestion.isDeleted());
    }

    @Test
    public void delete_fail_anotherUser() throws Exception {
        basicAuthTemplate().delete("/api/questions/3/", String.class);
        Question dbQuestion = questionRepository.findOne(3L);
        assertTrue(!dbQuestion.isDeleted());
    }

    @Test
    public void delete() throws Exception {
        basicAuthTemplate(SANJIGI).delete("/api/questions/2/", String.class);
        Question dbQuestion = questionRepository.findOne(2L);
        log.debug("dbQuestion: {}", dbQuestion.toString());
        log.debug("delete history : {}", deleteHistoryRepository.findAll());
        assertTrue(dbQuestion.isDeleted());
    }

    @Test
    public void delete_fail_haveAnotherUserAnswer() throws Exception {
        basicAuthTemplate().delete("/api/questions/1/", String.class);
        Question dbQuestion = questionRepository.findOne(1L);
        log.debug("dbQuestion: {}", dbQuestion.toString());
        assertTrue(!dbQuestion.isDeleted());
        Answer dbAnswer = answerRepository.findOne(1L);
        log.debug("dbQuestion: {}", dbAnswer.toString());
        assertTrue(!dbAnswer.isDeleted());
    }

    @Test
    public void update_fail_noLogin() throws Exception {
        QuestionDto newQuestion = createQuestionDto(1L);
        template().put("/api/questions/1/", newQuestion);
        QuestionDto dbQuestion = getResource("/api/questions/1/", QuestionDto.class);
        assertTrue(!dbQuestion.equals(newQuestion));
    }

    @Test
    public void update_fail_anotherUser() throws Exception {
        QuestionDto newQuestion = createQuestionDto(2L);
        basicAuthTemplate().put("/api/questions/2/", newQuestion);
        QuestionDto dbQuestion = getResource("/api/questions/2/", QuestionDto.class);
        assertTrue(!dbQuestion.equals(newQuestion));
    }

    @Test
    public void update() throws Exception {
        QuestionDto newQuestion = createQuestionDto(1L);
        basicAuthTemplate().put("/api/questions/1/", newQuestion);
        QuestionDto dbQuestion = getResource("/api/questions/1/", QuestionDto.class);
        assertTrue(dbQuestion.equals(newQuestion));
    }

}