package com.microservices.demo.elastic.query.web.client.handler;

import com.microservices.demo.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ElasticQueryWebClientErrorHandler {

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public String handle(AccessDeniedException e, Model model) {
        log.error("Access denied exception!");
        model.addAttribute("error", HttpStatus.UNAUTHORIZED.getReasonPhrase());
        model.addAttribute("error_description, You are not authorized to access this resource!");
        return "error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handle(IllegalArgumentException e, Model model) {
        log.error("Illegal argument exception!", e);
        model.addAttribute("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute("error_description", "Illegal argument exception!" + e.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handle(Exception e, Model model) {
        log.error("Internal server error!", e);
        model.addAttribute("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute("error_description", "A server error occurred!");
        return "error";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handle(RuntimeException e, Model model) {
        log.error("Service runtime exception!", e);
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        model.addAttribute("error", "Could not get response! " + e.getMessage());
        model.addAttribute("error_description", "Service runtime exception! " + e.getMessage());
        return "home";
    }

    @ExceptionHandler({BindException.class})
    public String handle(BindException e, Model model) {
        log.error("Method argument validation exception!", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error ->
                errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        model.addAttribute("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute("error_description", errors);
        return "home";
    }

}
