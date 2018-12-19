package com.hchat.pojo.vo;

/**
 * <p>将返回给客户端的数据封装到实体中</p>
 *
 * @author xiaodongsun
 * @date 2018/12/19
 */
public class Result {

    /** 是否操作成功 */
    private boolean success;
    /** 返回消息 */
    private String message;
    /** 操作结果 */
    private Object result;

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(boolean success, String message, Object result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
