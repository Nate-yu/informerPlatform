package cn.edu.hut.exceptions;

import lombok.Getter;

/**
 * @author Nate yu
 * @date 2022/4/24 - 下午 09:28
 * @Description TODO 自定义异常
 */
@Getter
public class ServiceException extends RuntimeException {
    private String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

}
