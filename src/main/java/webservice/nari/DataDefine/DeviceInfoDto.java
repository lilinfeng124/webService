package webservice.nari.DataDefine;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author llf
 * @version 1.0 2021/1/29
 */
@Data
public class DeviceInfoDto {

    private String deviceId;
    private String deviceName;
    private String deviceType;
    private String phase;
    private String splitWire;
    private String uploadTime;
    private String localCityCode = "1";
}
