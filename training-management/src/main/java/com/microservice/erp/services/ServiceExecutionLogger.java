package com.microservice.erp.services;

import com.microservice.erp.domain.entities.Username;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Rajib Kumer Ghosh
 *
 */

@Aspect
@Component
public class ServiceExecutionLogger {



    private static Logger LOG = LoggerFactory.getLogger(ServiceExecutionLogger.class);

    @Around("execution(* com.microservice.erp.services..*(..))")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //Get intercepted method details
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        /*List<String> paramsType = Arrays.stream(methodSignature.getParameterTypes())
                .map(val -> val.getSimpleName())
                .collect(Collectors.toList());*/
        List<String> paramsName = Arrays.asList(methodSignature.getParameterNames());

        //Measure method execution time
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        //Log method execution time
        LOG.info("Execution time of {}.{}({}) :: {} {}"
                , className
                , methodName
                , String.join(",", paramsName)
                , stopWatch.getTotalTimeMillis()
                , "ms");
        return result;
    }

//    @Override
//    public Optional<Username> getCurrentAuditor() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return Optional.of(new Username("User Id"));
//    }

}
