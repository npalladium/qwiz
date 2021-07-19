package com.quantumdisruption.qwiz.QWiz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@Slf4j
public class EventController {

    @Autowired EmitterContainer emitterContainer;
    @Autowired private HttpServletRequest servletRequest;


    @GetMapping("/stream-pounces")
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
