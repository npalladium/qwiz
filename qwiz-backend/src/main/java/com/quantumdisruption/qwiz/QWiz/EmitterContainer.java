package com.quantumdisruption.qwiz.QWiz;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Component
@ApplicationScope
public class EmitterContainer {
    private final List<SseEmitter> pounceSubscribers = Collections.synchronizedList(new ArrayList<>());


    public EmitterContainer() {
    }

    public void addPounceSubscriber(SseEmitter emitter) {
        pounceSubscribers.add(emitter);
    }

    public void removePounceSubscriber(SseEmitter emitter) {
        pounceSubscribers.remove(emitter);
    }

}
