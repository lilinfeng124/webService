package webservice.nari;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import webservice.nari.AuthDto.AccidDTO;
import webservice.nari.AuthDto.WaveDTO;
import webservice.nari.ConstStr.ConstStr;
import webservice.nari.ConstStr.ServiceError;
import webservice.nari.DataDefine.DeviceInfoDto;
import webservice.nari.service.webService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class NariApplicationTests {

    @Autowired
    webService GSWbService;
    @Test
    void contextLoads() {
    }

    @Test
    void setDevOnline(){
        SimpleDateFormat df = new SimpleDateFormat(ConstStr.GANSU_TIME_FORMAT);
        String strDate = df.format(new Date());
        DeviceInfoDto dto = new DeviceInfoDto();
        ServiceError error = GSWbService.UpdateDevOnlineStatus("12121212121200002", ConstStr.ONLINE_STATUS, strDate);
        if(error==ServiceError.SERVICE_OK){
            System.out.print("OK");
        }else{
            System.out.print("Fail");
        }
    }

    @Test
    void uploadFault(){

        AccidDTO accidDto = new AccidDTO();
        String strAcidId = UUID.randomUUID().toString();
        accidDto.setFaultId(strAcidId);
        SimpleDateFormat sp = new SimpleDateFormat(ConstStr.GANSU_TIME_FORMAT);
        String strFaultDate = sp.format(new Date());
        accidDto.setFaultTime(strFaultDate);
        accidDto.setFaultUnit("测试单位");
        accidDto.setLineName("线路名称");
        accidDto.setLineObjId("线路id");
        accidDto.setPhase("A相");
        accidDto.setFaultTowerName("故障杆塔");
        accidDto.setFaultTowerObjId("faulttowerid");
        accidDto.setBaseTowerName("基准杆塔");
        accidDto.setBaseTowerObjId("基准杆塔id");
        accidDto.setBaseTowerDistance(String.valueOf("120.5"));
        accidDto.setStartTowerName("起始杆塔");
        accidDto.setStartTowerObjId("起始杆塔id");
        accidDto.setEndTowerName("区间终止杆");
        accidDto.setEndTowerObjId("区间终止杆id");
        String strFaultId = UUID.randomUUID().toString();
        String strFaultId2 = UUID.randomUUID().toString();
        List<String> lst = new ArrayList<>();
        lst.add(strFaultId);
        lst.add(strFaultId2);
        String  strWaveLst = String.join(",",lst);
        accidDto.setWaveIndexs(strWaveLst);
        accidDto.setCaptureTime(strFaultDate);
        accidDto.setUploadTime(strFaultDate);
        accidDto.setLocalCityCode("1");
        ServiceError error = GSWbService.UploadFault(accidDto);
        if(error==ServiceError.SERVICE_OK){
            System.out.println("OK");
        }else{
            System.out.println("fail");
        }
    }

    @Test
    void uploadWave(){
        WaveDTO waveDto = new WaveDTO();

        waveDto.setDeviceId("12121212121200002");
        String strWaveIndexs = UUID.randomUUID().toString();
        waveDto.setWaveIndex(strWaveIndexs);
        waveDto.setWaveType(String.valueOf(ConstStr.GANSU_WAVE_TYPE));
        waveDto.setFaultTowerName("杆塔名称");
        SimpleDateFormat df = new SimpleDateFormat(ConstStr.GANSU_TIME_FORMAT);
        String strFormatTime = df.format(new Date());
        byte[] arry = new byte[24000];
        List<Short>valLst = new ArrayList<>();
        for(int n=0;n<12000;n++){
            short sValue = (short)(100*Math.cos(n*2* Math.PI/12000));
            valLst.add(sValue);
            arry[2*n] = (byte)(sValue>>8);
            arry[2*n+1] = (byte)(sValue);
        }
        try {
            waveDto.formattedData(arry,10000000, ConstStr.TRAVEL_WAVE_TYPE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        waveDto.setCaptureTime(strFormatTime);
        waveDto.setUploadTime(strFormatTime);
        ServiceError error = GSWbService.UploadWave(waveDto);
        if(error==ServiceError.SERVICE_OK){
            System.out.println("OK");
        }else{
            System.out.println("fail");
        }
    }




}
