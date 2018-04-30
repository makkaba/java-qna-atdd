package codesquad.web;

import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class QuestionAcceptanceTest extends AcceptanceTest{
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void 질문Create(){
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("title", "the title");
        params.add("contents", "hello");
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(params, headers);

        ResponseEntity<String> response = basicAuthTemplate()
                .postForEntity("/questions", request, String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));

    }
    @Test
    public void 질문Read(){

    }
    @Test
    public void 질문Update(){

    }
    @Test
    public void 질문Delete(){

    }
}
