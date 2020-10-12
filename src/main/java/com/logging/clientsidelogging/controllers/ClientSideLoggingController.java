package com.logging.clientsidelogging.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins="*", allowedHeaders="*")
@RequestMapping("/api/logs")
public class ClientSideLoggingController {
    private final Logger log = LoggerFactory.getLogger(ClientSideLoggingController.class);

    @Value("${clientErrorLogSizeLimit}")
    private String clientErrorLogSizeLimit;

    /**
     * POST Post client-side error log to server.
     */
   @RequestMapping(method = RequestMethod.POST)

    public ResponseEntity<Boolean> logClientSideError(HttpServletRequest request) {

        long contentLength = request.getContentLengthLong();

        if (contentLength > Long.parseLong(clientErrorLogSizeLimit)) {
            log.warn("This request is too big and its content will not be logged.");
        } else {
            try {
                log.info("Request body: " +  request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
            } catch (IOException e) {
                log.error("Client-side error occurred but system cannot process error info.");
            }
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }



}



