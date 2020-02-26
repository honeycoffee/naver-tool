package com.naver.pubtrans.itn.api.exception;


import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.naver.pubtrans.itn.api.consts.ResultCode;
import com.naver.pubtrans.itn.api.vo.common.output.CommonOutput;


@RestControllerAdvice
public class ApiExceptionHandler {

	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public CommonOutput CommonInternalServerError(Throwable e, HttpServletRequest request) {
		String errorMsg = "예기치 못한 오류가 발생하였습니다(" + e.getMessage() + ")" ;
		return new CommonOutput(ResultCode.INNER_FAIL, errorMsg) ;
    }
	
	/**
     * HTTP STATUS 400
     * Valid를 쓰지 않는 @RequestParam 파라미터 Missing
     * @param e
     * @return 
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public CommonOutput httpStatus400(MissingServletRequestParameterException e) {
    	return new CommonOutput(ResultCode.PARAMETER_ERROR, ResultCode.PARAMETER_ERROR_MSG) ;

    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonOutput bindExceptionHandle(MethodArgumentNotValidException e) {
        

        BindingResult bindingResult = e.getBindingResult();
       
        StringBuilder builder = new StringBuilder();
        int i=0 ;
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
        	
        	if(i > 0) {
        		builder.append("\n");
        	}
        	
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            
            i++ ;
        }
        
        return new CommonOutput(ResultCode.PARAMETER_ERROR, builder.toString()) ;
    }
    
    
    /**
     * HTTP STATUS 404
     * @param e
     * @return 
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public CommonOutput httpStatus404(NoHandlerFoundException e) {
    	return new CommonOutput(ResultCode.PAGE_NOT_FOUND_ERROR, ResultCode.PAGE_NOT_FOUND_ERROR_MSG) ;
    }
   
    /**
     * HTTP STATUS 401 - 사용자 토큰 만료 예외 
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    public CommonOutput authTokenExpireExceptionHandle(TokenExpiredException e) {
    	return new CommonOutput(ResultCode.AUTH_TOKEN_EXPIRED, ResultCode.AUTH_TOKEN_EXPIRED_MSG) ;
    }
    
    /**
     * HTTP STATUS 401 - 사용자 토큰 디코딩 에러
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JWTDecodeException.class)
    public CommonOutput authTokenDecodeExceptionHandle(JWTDecodeException e) {
    	return new CommonOutput(ResultCode.AUTH_TOKEN_VALID_ERROR, ResultCode.AUTH_TOKEN_VALID_ERROR_MSG) ;
    }
    
    /**
     * HTTP STATUS 401 - 사용자 토큰 검증 에러
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JWTVerificationException.class)
    public CommonOutput authTokenVerificationExceptionHandle(JWTVerificationException e) {
    	return new CommonOutput(ResultCode.AUTH_TOKEN_VALID_ERROR, ResultCode.AUTH_TOKEN_VALID_ERROR_MSG) ;
    }
    
    /**
     * HTTP STATUS 401 - 사용자 인증정보 예외
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessTokenNotFoundException.class)
    public CommonOutput authExceptionhandle(AccessTokenNotFoundException e) {
    	return new CommonOutput(ResultCode.AUTH_TOKEN_EMPTY, ResultCode.AUTH_TOKEN_EMPTY_MSG) ;
    }
    
    /**
     * HTTP STATUS 500 - Api Custom 예외
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApiException.class)
    public CommonOutput apiExceptionHandle(ApiException e) {
    	return new CommonOutput(ResultCode.INNER_FAIL, e.getMessage()) ;
    }
    
}
