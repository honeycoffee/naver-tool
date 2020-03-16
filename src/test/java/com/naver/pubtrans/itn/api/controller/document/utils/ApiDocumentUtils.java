package com.naver.pubtrans.itn.api.controller.document.utils;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;


public interface ApiDocumentUtils {

	static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                        modifyUris()
                                .scheme("http")
                                .host("localhost")
                                .removePort(),

                        prettyPrint());
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
        		removeHeaders("Vary", "X-Content-Type-Options", "X-XSS-Protection", "Cache-Control", "Pragma", "Expires", "X-Frame-Options"),
        		prettyPrint());
    }
}
