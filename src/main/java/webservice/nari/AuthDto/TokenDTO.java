package webservice.nari.AuthDto;

import lombok.Data;
import webservice.nari.ConstStr.ConstStr;

import java.util.Date;

/**
 * @author llf
 * @version 1.0 2021/1/30
 */
@Data
public class TokenDTO {
    //token
    String Token;
    //超时时间
    int      ExpireSecond;
    //是否获取
    boolean TokenAccessed = false;

    Date LastAuthorTime;
}
