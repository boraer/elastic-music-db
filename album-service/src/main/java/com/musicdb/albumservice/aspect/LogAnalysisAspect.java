package com.musicdb.albumservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import static java.lang.System.currentTimeMillis;

@Aspect
@Component
@Slf4j
public class LogAnalysisAspect {


	@Around(value = "ControllerLayerExecution()")
	public Object auditAroundController(ProceedingJoinPoint joinPoint) throws Throwable {

		Long startTime = currentTimeMillis();
		log.info("ArtistController start: {}", startTime);
		String methodName = joinPoint.getSignature().getName();
		Object obj = joinPoint.proceed();

		Long endTime = currentTimeMillis();
		log.info("ArtistController end time: {}. Duration: {} ms EndPoint: {} ", endTime, endTime - startTime,methodName);
		return obj;
	}

	@Around(value = "ServiceLayerExecution()")
	public Object auditAroundService(ProceedingJoinPoint joinPoint) throws Throwable {

		Long startTime = currentTimeMillis();
		log.info("ArtistService start: {}", startTime);
		String methodName = joinPoint.getSignature().getName();
		Object obj = joinPoint.proceed();

		Long endTime = currentTimeMillis();
		log.info("ArtistService end time: {}. Duration: {} ms Method: {}", endTime, endTime - startTime,methodName);
		return obj;
	}

	
	@Pointcut("within(com.musicdb.albumservice.controller.impl..*)")
	public void ControllerLayerExecution() {
	}

	@Pointcut("within(com.musicdb.albumservice.service.*.*)")
	public void ServiceLayerExecution() {
	}
}
