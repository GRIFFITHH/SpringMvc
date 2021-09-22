package com.momoto.firstProject.basic.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.momoto.firstProject.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username = {} , age = {}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }

    /**
     * @RequestBody
     * @RequestBody 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다. 참고로 헤더 정보가
     * 필요하다면 HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 된다.
     * 이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam ,
     * @ModelAttribute 와는 전혀 관계가 없다.
     * 요청 파라미터 vs HTTP 메시지 바디
     * 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
     * HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody
     * @ResponseBody
     * @ResponseBody 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다.
     * 물론 이 경우에도 view를 사용하지 않는다.
     */
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messageBody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username = {} , age = {}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    /**
     * 스프링은 @ModelAttribute , @RequestParam 해당 생략시 다음과 같은 규칙을 적용한다.
     * String , int , Integer 같은 단순 타입 = @RequestParam
     * 나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)
     * 따라서 이 경우 HelloData에 @RequestBody 를 생략하면 @ModelAttribute 가 적용되어버린다.
     * HelloData data @ModelAttribute HelloData data
     * 따라서 생략하면 HTTP 메시지 바디가 아니라 요청 파라미터를 처리하게 된다.
     */
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) throws IOException {
        //HelloData 앞의 @RequestBody를 삭제하면 @ModelAttribute로 처리해버린다. 따라서 생략불가
        //
        log.info("username = {} , age = {}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public HelloData requestBodyJsonV4(@RequestBody HelloData helloData) throws IOException {
        log.info("username = {} , age = {} ", helloData.getUsername(), helloData.getAge());
        return helloData;
    }
}
