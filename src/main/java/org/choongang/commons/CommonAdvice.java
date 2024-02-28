package org.choongang.commons;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.exceptions.CommonException;
import org.choongang.commons.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice("org.choongang")
@RequiredArgsConstructor
public class CommonAdvice {

    private final Utils utils;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JSONData> errorHandler(Exception e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        Object message = e.getMessage();

        if (e instanceof CommonException) {
            CommonException commonException = (CommonException) e;
            status = commonException.getStatus();
            Errors errors = commonException.getErrors();
            if (errors != null) {
                Map<String, List<String>> messages = utils.getErrorMessages(errors);
                if (messages != null && !messages.isEmpty()) message = messages;
            }
        } else if ( e instanceof AccessDeniedException) {
            status = HttpStatus.UNAUTHORIZED;
        }

        e.printStackTrace();

        JSONData data = new JSONData();
        data.setSuccess(false);
        data.setStatus(status);
        data.setMessages(message);

        return ResponseEntity.status(status).body(data);
    }
}
