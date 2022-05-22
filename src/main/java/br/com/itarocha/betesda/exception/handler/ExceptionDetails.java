package br.com.itarocha.betesda.exception.handler;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExceptionDetails {

    private String title;
    private Integer status;
    private String detail;

}
