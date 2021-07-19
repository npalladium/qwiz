package com.quantumdisruption.qwiz.QWiz;

public enum PresentationState {
    Intro {
        @Override
        public PresentationState nextState() {
            return Question;
        }
    },
    Question {
        @Override
        public PresentationState nextState() {
            return Hint;
        }
    },
    Hint {
        @Override
        public PresentationState nextState() {
            return Answer;
        }
    },
    Answer {
        @Override
        public PresentationState nextState() {
            return Question;
        }
    };
    public abstract PresentationState nextState();
}