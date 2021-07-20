package com.quantumdisruption.qwiz.QWiz.controllers;

import com.quantumdisruption.qwiz.QWiz.containers.EmitterContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@Slf4j
public class EventController {

    @Autowired
    EmitterContainer emitterContainer;
    @Autowired private HttpServletRequest servletRequest;


    @GetMapping(value = "/stream-pounces", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin
    public SseEmitter streamPounces() {
        log.info(servletRequest.getRemoteHost());
        SseEmitter emitter = new SseEmitter(7200000L);
        emitterContainer.addPounceSubscriber(emitter);
        emitter.onCompletion(() -> {
            synchronized (emitterContainer) {emitterContainer.removePounceSubscriber(emitter);};
        });
        emitter.onTimeout(()-> {
            emitter.complete();
        });
        return emitter;
    }
}
