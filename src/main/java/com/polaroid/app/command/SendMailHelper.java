package com.polaroid.app.command;

import java.util.Collections;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SendMailHelper {

	@Autowired
	private JavaMailSender javaMailSender;

	// 메일을 송신한다.
	public void sendMail(String fromAddress, String[] toAddress, String subect, String body) throws Exception {

		final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
		final MimeMessageHelper msg = new MimeMessageHelper(mimeMessage, "UTF-8");
		msg.setFrom(fromAddress);
		msg.setTo(toAddress);
		msg.setSubject(subect);
		msg.setText(body, true); //중요

		try {
			javaMailSender.send(mimeMessage);
		} catch (Exception ex) {
			log.error("failed to send mail : {}", ex);
			throw ex;
		}
	}

	/*
	 * Map<String, Object> map = new HashMap<String, Object>(); map.put("name",
	 * "일길동"); map.put("message", "안녕하세요");
	 */

	// 지정한 템플릿의 메일 본문을 반환한다.
	// @param template : mail (resources/templates/mail.html) : 템플릿
	// @param objects : Template에 전달할 데이터 설정

	public String getMailBody(String template, Map<String, Object> objects) {

		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(htmlTemplateResolver());
		
		Context ctx = new Context();
		// Template에 전달할 데이터 설정
		//objects.forEach(context::setVariable);
		ctx.setVariable("url", objects.get("url"));
		//ctx.setVariable("nickName", objects.get("nickName"));
		
		//templateName : "html/email-inlineimage.html"
		return templateEngine.process(template, ctx);

	}

	private ITemplateResolver htmlTemplateResolver() {
		final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();      
        templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        templateResolver.setPrefix("/mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        //templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCacheable(false);
        return templateResolver;
	}

}