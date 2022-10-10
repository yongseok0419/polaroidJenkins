package com.polaroid.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionAdviceController {

			//500에러 처리를 할 수 있는 코드
			@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) //브라우저 개발자모드의 Network에 표시되는 Http상태코드를 보여줌
			@ExceptionHandler(Exception.class)	//Exception타입이 아니라 .class를 붙임으로써 값으로 인식한다.
			public String except(Exception e, Model model) {
				
				log.error("Exception...................."+e.getMessage());
				
				//mode.addAttribute(exception)에 담아준것으로 view에서 어떤 에러가 났는지 표시하고, 에러페이지를 만들어준다.
				model.addAttribute("exception", e); //attributename명을 "exception"으로 한 것이다.
				
				log.error(model.toString());
				
				return "error/500";
			}
			
			//500 메세지는 Internal Server Error이므로 @ExceptionHandler를 이용해서 처리가 가능하지만,
			//404 메세지는 문법 오류가 아니고 잘못된 URL을 호출할 때 보이므로 다르게 처리해주어야 한다.
			@ResponseStatus(value = HttpStatus.NOT_FOUND)	//브라우저 개발자모드의 Network에 표시되는 Http상태코드를 보여줌
			@ExceptionHandler(NoHandlerFoundException.class)
			public String handle404(NoHandlerFoundException e) {
				log.error("Exception...................."+e.getMessage());
				return "error/404";
			}
}
