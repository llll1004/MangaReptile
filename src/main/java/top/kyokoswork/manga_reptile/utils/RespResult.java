package top.kyokoswork.manga_reptile.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import top.kyokoswork.manga_reptile.enums.StateE;

import static top.kyokoswork.manga_reptile.enums.StateE.*;

@Data
@Accessors(chain = true)
public class RespResult<T> {
    private Integer code;
    private StateE state;
    private String message;
    private T data;

    public RespResult(T data) {
        this.code = 200;
        this.state = SUCCESS;
        this.message = "操作成功";
        this.data = data;
    }

    public RespResult(StateE state) {
        switch (state) {
            case SUCCESS:
                this.code = 200;
                this.state = SUCCESS;
                this.message = "操作成功";
                break;
            case ERROR:
                this.code = 500;
                this.state = ERROR;
                this.message = "未知错误";
                break;
            case FAIL:
                this.code = 500;
                this.state = FAIL;
                this.message = "操作失败";
                break;
            case DETAILS_ERROR:
                this.code = 500;
                this.state = DETAILS_ERROR;
                this.message = "还未获取漫画详情";
        }
    }
}
