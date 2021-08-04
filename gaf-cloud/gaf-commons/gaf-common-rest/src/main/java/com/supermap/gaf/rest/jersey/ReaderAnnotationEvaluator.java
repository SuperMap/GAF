package com.supermap.gaf.rest.jersey;

public class ReaderAnnotationEvaluator extends AbstractAnnotationEvaluator<Reader> {
    public ReaderAnnotationEvaluator() {
        super(Reader.class);
    }


    @Override
    protected boolean isAbleToByAnnotation(Reader annotation, Class<?> clz) {
        return annotation.value().equals(clz);
    }
}
