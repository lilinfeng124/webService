package webservice.nari;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import webservice.nari.ConstStr.ConstStr;
import webservice.nari.ConstStr.ServiceError;
import webservice.nari.DataDefine.DeviceInfoDto;
import webservice.nari.service.webService;

import javax.jws.WebService;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class NariApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NariApplication.class, args);
        webService webservice = context.getBean(webService.class);
        boolean  error = webservice.AccessAuthToken2();
        if(error==true) {
            SimpleDateFormat df = new SimpleDateFormat(ConstStr.GANSU_TIME_FORMAT);
            String strDate = df.format(new Date());
            DeviceInfoDto dto = new DeviceInfoDto();

            webservice.UpdateDevOnlineStatus("12121212121200002", ConstStr.ONLINE_STATUS, strDate);
        }
    }

}
