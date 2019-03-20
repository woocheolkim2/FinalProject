package com.spring.bnb.aop;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.spring.common.MyUtil;

// ===== 52. 공통 관심사 클래스(Aspect 클래스) 생성하기 =====
@Aspect
@Component
public class LoginCheck {
	// ===== Pointcut을 생성한다. =====
	@Pointcut("execution(public * com.spring..*Controller.requireLogin_*(..))") 
	public void requireLogin() {}
	
	// ===== Before Advice 선언 및 Before advice 내용 선언 =====
	@Before("requireLogin()")
	public void before(JoinPoint joinpoint) {
		// joinpoint는 포인트컷 되어진 주업무의 메소드이다.
		// 로그인 유무를 확인하기 위해서 request를 통해 세션을 얻어온다.
		HttpServletRequest req = (HttpServletRequest)joinpoint.getArgs()[0];
		HttpServletResponse res = (HttpServletResponse)joinpoint.getArgs()[1];
		HttpSession session = req.getSession();
		
		if(session.getAttribute("loginuser") == null || "".equals(session.getAttribute("loginuser"))) {
			try {
				// 해당 요청자가 로그인을 하지 않은 상태이라면 로그인 하는 페이지로 이동 시키겠다.
				req.setAttribute("msg", "먼저 로그인 하세요.");
				req.setAttribute("loc","javascript:history.back();");
				// 로그인 성공 후 로그인 하기 전 페이지로 돌아가는 작업하기
				// ===>> 현재 페이지 주소 URL알아오기 <<===
				String url = MyUtil.getCurrentURL(req);
				session.setAttribute("gobackURL", url);
				RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/msg.jsp");
				
				dispatcher.forward(req, res);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}
	} // end of  before(JoinPoint joinpoint)
}
