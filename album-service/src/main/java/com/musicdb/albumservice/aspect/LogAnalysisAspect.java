package com.musicdb.albumservice.aspect;

import static java.lang.System.currentTimeMillis;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.musicdb.albumservice.aspect.core.Util;
import com.musicdb.albumservice.controller.config.ControllerExceptionHandler.ApiError;
import com.musicdb.albumservice.controller.config.ErrorCode;
import com.musicdb.albumservice.service.IArtistProxyService;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAnalysisAspect {

	private IArtistProxyService artistProxyService;

	@Autowired
	public LogAnalysisAspect(IArtistProxyService artistProxyService) {
		this.artistProxyService = artistProxyService;
	}

	@Around(value = "ControllerLayerExecution()")
	public Object auditAroundService(ProceedingJoinPoint joinPoint) throws Throwable {

		Long startTime = currentTimeMillis();
		log.info("ArtistController start: {}", startTime);

		final Object[] args = joinPoint.getArgs();
		final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		final Method method = methodSignature.getMethod();
		final HttpServletRequest httpServletRequest = Util.extractRequest(args);
		final Map<String, String> pathVariables = Util.extractPathVariables(httpServletRequest);

		final String artistId = Util.extractArtistId(pathVariables);


		Object obj;
		final boolean success = artistProxyService.checkArtistById(artistId);
		if (!success) {
			ApiError apiError = new ApiError(ErrorCode.DOCUMENT_NOT_FOUND, null);
			obj = new ResponseEntity<>(apiError, apiError.getCode().getHttpStatus());
		}
		else {
		 obj = joinPoint.proceed();
		}

		Long endTime = currentTimeMillis();
		log.info("ArtistController end time: {}. Duration: {} ms EndPoint: {} ", endTime, endTime - startTime,
				methodSignature.getMethod().getName());

		return obj;
	}

	@Around(value = "ServiceLayerExecution()")
	public Object analyzeRequest(ProceedingJoinPoint joinPoint) throws Throwable {

		Long startTime = currentTimeMillis();
		log.info("ArtistController start: {}", startTime);
		String methodName = joinPoint.getSignature().getName();
		Object obj = joinPoint.proceed();

		Long endTime = currentTimeMillis();
		log.info("ArtistController end time: {}. Duration: {} ms EndPoint: {} ", endTime, endTime - startTime,
				methodName);
		return obj;
	}

	@Pointcut("within(com.musicdb.albumservice.controller.impl.*.*)")
	public void ControllerLayerExecution() {
	}

	@Pointcut("within(com.musicdb.albumservice.service.*.*)")
	public void ServiceLayerExecution() {
	}
}
