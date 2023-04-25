package edu.tongji.plantary.circle.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {

    private T data;
    private int status = 200;
    private String message;
    private long srvTime = System.currentTimeMillis();

    public BaseResponse(String message) {
        this.message = message;
    }

    public BaseResponse<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static <T> BaseResponse<T> ok() {
        return new BaseResponse<>("操作成功");
    }

    public static <T> BaseResponse<T> bad() {
        BaseResponse<T> badResult=new BaseResponse<>("失败！");
        badResult.status=600;

        return badResult;
    }

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<T>("操作成功").setData(data);
    }

}


