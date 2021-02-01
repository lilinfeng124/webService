package webservice.nari.AuthDto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author llf
 * @version 1.0 2021/1/28
 */
@Slf4j
@Data
public class AuthDto {
    String username;
    String password;
}
