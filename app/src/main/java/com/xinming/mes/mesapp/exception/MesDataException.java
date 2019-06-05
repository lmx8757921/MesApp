package com.xinming.mes.mesapp.exception;

/**
 * Created by Administrator on 2019/6/3.
 */

public class MesDataException extends Exception {
    public MesDataException() {
        super();
    }

    public MesDataException(String message) {
        super(message);
    }

    public MesDataException(String message, Throwable cause) {
        super(message,cause);
    }

    public MesDataException(Throwable cause) {
        super(cause);
    }
}
