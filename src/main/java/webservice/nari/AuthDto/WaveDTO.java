package webservice.nari.AuthDto;

import lombok.Data;
import webservice.nari.ConstStr.ConstStr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author llf
 * @version 1.0 2021/2/1
 */
@Data
public class WaveDTO {
    public String deviceId;
    public String waveIndex;
    public String waveType;
    public String faultTowerName;
    public String waveData;
    public String captureTime;
    public String uploadTime;
    public String localCityCode;


    //设置data数据
    public void formattedData(byte[]data,int nSample,int waveType) throws IOException {
        //超过1M的采样率

            ByteArrayInputStream input = new ByteArrayInputStream(data);
            byte[] bts = new byte[2];
            double fTime = 0.0;
            int nCount = 0;
            List<String> strDataLst = new ArrayList<>();
            for(int nPos = 0;nPos<data.length;nPos+=2){
                input.read(bts);
                ByteBuffer bf = ByteBuffer.wrap(bts);
                short sValue = bf.getShort();
                //大于1M的采样率，说明是行波 微秒为单位
                if(waveType== ConstStr.TRAVEL_WAVE_TYPE) {
                    fTime =(1.0*1000000*nCount / nSample);

                }
                else{
                    //小于1M的采样率，说明是工频，毫秒为单位
                    fTime = (1.0*1000*nCount/nSample);
                }
                String strData =fTime+"#"+String.valueOf(sValue);
                strDataLst.add(strData);
                nCount++;
            }
            String strDataStr = String.join(",",strDataLst);
            System.out.println(strDataStr);
            String strLast = strDataLst.get(strDataLst.size()-1);
            System.out.println(strLast);
            waveData = strDataStr;

    }

}
