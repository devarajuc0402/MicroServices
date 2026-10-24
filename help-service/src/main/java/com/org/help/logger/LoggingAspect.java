package com.org.help.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class LoggingAspect {
	
	public LoggingAspect() {
		log.info("LoggerAspect bean has been created");
	}

	private static final Logger log = 
			LoggerFactory.getLogger(LoggingAspect.class);

	@Pointcut("execution(* com.org.help.service..*(..)) || " + 
			"execution(* com.org.help.query..*(..)) || " + 
			"execution(* com.org.help.command..*(..))")
	public void serviceMethods() {}
	
	@Around("serviceMethods()")
	public Object logServiceMethods(
			ProceedingJoinPoint joinPoint) throws Throwable {
		
		String methodName = 
				joinPoint.getSignature().toShortString();

		double startTime = System.currentTimeMillis() / 1000.0;
		
		log.info("========== AOP TRIGGERED ==========");
		log.info("{} execution starts at {} s", methodName, 
				String.format("%.2f", startTime));
		
		try {
			Object result = joinPoint.proceed();
			double endTime = System.currentTimeMillis() / 1000.0;

			String durationTime = String.format("%.2f", endTime - startTime);
			
			log.info("{} execution duration {} s", methodName, durationTime);
			log.info("========== AOP ENDS ===============");
			return result;
		} catch (Throwable e) {
			
			log.error("{} Failed {}", methodName, e);
			throw e;
		}
	
	}
}
