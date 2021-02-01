/**

 *@描述 本文件主要是用于南京侧主站向甘肃主站同步信息

 *@参数

 *@返回值

 *@创建人  llf

 *@创建时间  2021/1/29

 *@修改人和其它信息

 */
package webservice.nari.service;

import com.sun.deploy.net.HttpResponse;
import net.sf.json.JSONObject;


import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.http.client.methods.HttpHead;
import org.springframework.http.*;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import webservice.nari.AuthDto.AccidDTO;
import webservice.nari.AuthDto.AuthDto;
import webservice.nari.AuthDto.TokenDTO;
import webservice.nari.ConstStr.ConstStr;
import webservice.nari.ConstStr.ServiceError;
import webservice.nari.DataDefine.DeviceInfoDto;


import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author llf
 * @version 1.0 2021/1/28
 */
@Component
@Controller
public class webService {

    private String UserName = "nanrui1";
    private String PassWord = "rzhNW84c2WN5n8ck611mmw%3D%3D";
    private String BaseUrl = "http://152.136.36.86:1899/zuul/dataServer/";

    TokenDTO tokenDTO;
    private boolean TokenAccessed = false;
    private String Token = "";
    private long ExpireSecond = 0;//超时时间
    private Date LastAuthorTime;//上次授权时间

    @RequestMapping("/auth2")
    @ResponseBody
    public boolean AccessAuthToken2(){
        RestPostTemplate<AuthDto> restTemplate = new RestPostTemplate<>();
        tokenDTO = restTemplate.AccessAuthToken(UserName,PassWord);
        if(tokenDTO==null){
            return false;
        }
        else{
            return true;
        }
    }
    /**

     *@描述 检查token是否过期，所有api调用都需要调用本函数检查，除了第一个获取token的函数

     *@参数

     *@返回值

     *@创建人  llf

     *@创建时间  2021/1/30

     *@修改人和其它信息

     */
    ServiceError CheckTokenValid(){
        //token失效了，并且token获取不到
        if(GetIsTokenValid()==false&&AccessAuthToken2()==false){
            return ServiceError.SERVICE_NOTOKEN_ERROR;
        }else{
            return ServiceError.SERVICE_OK;
        }
    }
/**

 *@描述   装置在线情况更新

 *@参数

 *@返回值

 *@创建人  llf

 *@创建时间  2021/1/30

 *@修改人和其它信息

 */
    public ServiceError UpdateDevOnlineStatus(String strDev,int nOnline,String strDateTime){

        ServiceError error = CheckTokenValid();
        if(error!=ServiceError.SERVICE_OK){
            return error;
        }

        Map<String,String>map = new HashMap<>();
        map.put(ConstStr.DEVICEID,strDev);
        map.put(ConstStr.ONLINE,String.valueOf(nOnline));
        map.put(ConstStr.UPLOADTIME,strDateTime);
        List<Map<String,String>>lst = new ArrayList<>();
        lst.add(map);

        RestPostTemplate<List> restPostTemp = new RestPostTemplate<>();
        //可以批量更新
        return restPostTemp.PostRequest("devices/v1/updateStatus",tokenDTO.getToken(),lst);
    }

    /**

     *@描述 装置经纬度更新，解析装置基本报文的时候调用

     *@参数

     *@返回值

     *@创建人  llf

     *@创建时间  2021/1/30

     *@修改人和其它信息

     */
    public ServiceError UpdateDevLocation(String strId,float latitude,float longitude){
        ServiceError error = CheckTokenValid();
        if(error!=ServiceError.SERVICE_OK){
            return error;
        }

        Map<String,String>map = new HashMap<>();
        map.put(ConstStr.DEVICEID,strId);
        map.put(ConstStr.LATITUDE,String.valueOf(latitude));
        map.put(ConstStr.LONGITUDE,String.valueOf(longitude));
        List<Map<String,String>>lst = new ArrayList<>();
        lst.add(map);

        RestPostTemplate<List> restPostTemp = new RestPostTemplate<>();
        //可以批量更新
        return restPostTemp.PostRequest("devices/v1/updateStatus",tokenDTO.getToken(),lst);

    }

    /**

     *@描述 装置更新负荷电流，解析装置工况的时候调用

     *@参数

     *@返回值

     *@创建人  llf

     *@创建时间  2021/1/30

     *@修改人和其它信息

     */
    public ServiceError UpdateDevCurrent(String strId,int current,String captureTime,
                                         String uploadTime,int cityCode ){
        ServiceError error = CheckTokenValid();
        if(error!=ServiceError.SERVICE_OK){
            return error;
        }

        Map<String,String>map = new HashMap<>();
        map.put(ConstStr.DEVICEID,strId);
        map.put(ConstStr.CURRENT,String.valueOf(current));
        map.put(ConstStr.CAPTURETIME,captureTime);
        map.put(ConstStr.UPLOADTIME,uploadTime);
        map.put(ConstStr.LOCALCITYCODE,String.valueOf(cityCode));

        List<Map<String,String>>lst = new ArrayList<>();
        lst.add(map);

        RestPostTemplate<List> restPostTemp = new RestPostTemplate<>();
        //可以批量更新
        return restPostTemp.PostRequest("devices/v1/updateStatus",tokenDTO.getToken(),lst);

    }

    /**

     *@描述 故障数据上送

     *@参数

     *@返回值

     *@创建人  llf

     *@创建时间  2021/1/30

     *@修改人和其它信息

     */
    public ServiceError UploadFault(AccidDTO dto){
        ServiceError error = CheckTokenValid();
        if(error!=ServiceError.SERVICE_OK){
            return error;
        }

        List<AccidDTO>lst = new ArrayList<>();
        lst.add(dto);

        RestPostTemplate<List> restPostTemp = new RestPostTemplate<>();
        //可以批量更新
        return restPostTemp.PostRequest("?",tokenDTO.getToken(),lst);

    }

    /**

     *@描述 故障数据上送

     *@参数

     *@返回值

     *@创建人  llf

     *@创建时间  2021/1/30

     *@修改人和其它信息

     */
    public ServiceError UploadWaveData(AccidDTO dto){
        ServiceError error = CheckTokenValid();
        if(error!=ServiceError.SERVICE_OK){
            return error;
        }

        List<AccidDTO>lst = new ArrayList<>();
        lst.add(dto);

        RestPostTemplate<List> restPostTemp = new RestPostTemplate<>();
        //可以批量更新
        return restPostTemp.PostRequest("?",tokenDTO.getToken(),lst);

    }



    /**

     *@描述 故障数据上送

     *@参数

     *@返回值

     *@创建人  llf

     *@创建时间  2021/1/30

     *@修改人和其它信息

     */



    /**

     *@描述 利用用户名和密码，获取token(废弃)

     *@参数

     *@返回值

     *@创建人  llf

     *@创建时间  2021/1/28

     *@修改人和其它信息

     */
    /*
    @RequestMapping("/auth")
    @ResponseBody
    public ServiceError AccessAuthToken(){
        //请求路径
        String url = BaseUrl+"auth/token";

        //使用Restemplate来发送HTTP请求
        RestTemplate restTemplate = new RestTemplate();

        Map<String,String> authMap = new HashMap<>();
        authMap.put(ConstStr.USERNAME,UserName);
        authMap.put(ConstStr.PASSWORD,PassWord);

        //设置请求header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());

        // 请求体，包括请求数据 body 和 请求头 headers
        HttpEntity<Map> httpEntity = new HttpEntity<>(authMap,headers);
        try {
            //使用 exchange 发送请求，以String的类型接收返回的数据
            //ps，我请求的数据，其返回是一个json
            ResponseEntity<String> strbody = restTemplate.postForEntity(url, httpEntity,String.class);
            //解析返回的数据
            JSONObject jsTemp = JSONObject.fromObject(strbody.getBody());
            String status = jsTemp.getString(ConstStr.STATUS);
            //返回成功
            if(status.equals(ConstStr.SUCCESS)&&jsTemp.has(ConstStr.DATA)){
                JSONObject objData = jsTemp.getJSONObject(ConstStr.DATA);
                if(objData.has(ConstStr.ACCESSTOKEN)) {
                    //设置token
                    Token = objData.getString(ConstStr.ACCESSTOKEN);
                    TokenAccessed = true;
                    ExpireSecond = objData.getInt(ConstStr.EXPIRESIN);
                    LastAuthorTime = new Date();
                }else{
                    //没有token
                    return ServiceError.SERVICE_NOTOKEN_ERROR;
                }
            }
            //失败
            else{

                System.out.println(jsTemp.toString());
                ServiceError error = ServiceError.SERVICE_REPLY_FAIL;
                if(jsTemp.has(ConstStr.MSG)) {
                    error.addMsg(jsTemp.getString(ConstStr.MSG));
                }
                return error;
            }
            return ServiceError.SERVICE_OK;

        }catch (Exception e){
            System.out.println(e);
            return  ServiceError.SERVICE_ACESS_EXCEPTION;
        }

    }*/




    /**
     *
     *@描述 判断token 是否有效

     *@参数

     *@返回值

     *@创建人  llf

     *@创建时间  2021/1/28

     *@修改人和其它信息

     */
    public boolean GetIsTokenValid(){

        //还没获取不用算了
        if(tokenDTO==null){
            return false;
        }
        //获取当前时间
        Date nowDate  = new Date();
        long nowMiSecond = nowDate.getTime();
        long beforMisSecond =tokenDTO.getLastAuthorTime().getTime();
        //算出时间秒数差
        long  nSecond = (nowMiSecond-beforMisSecond)/1000;
        //超时了
        if(ExpireSecond<=nSecond){
            return false;
        }else{
            return true;
        }
    }

    /**

     *@描述 更新装置在线情况

     *@参数

     *@返回值

     *@创建人  llf

     *@创建时间  2021/1/29

     *@修改人和其它信息

     */
    public ServiceError UpdateDevOnline(String strDev,int nOnline,String strDateTime){

        String url = BaseUrl +"devices/v1/updateStatus?access_token="+Token;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders head = new HttpHeaders();
        head.setContentType(MediaType.APPLICATION_JSON);
        head.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
        Map<String,String>map = new HashMap<>();
        map.put(ConstStr.DEVICEID,strDev);
        map.put(ConstStr.ONLINE,String.valueOf(nOnline));
        map.put(ConstStr.UPLOADTIME,strDateTime);
        List<Map<String,String>>lst = new ArrayList<>();
        lst.add(map);

        HttpEntity<List> entity = new HttpEntity<List>(lst,head);
        try{
            ResponseEntity<String>strBody =  restTemplate.postForEntity(url,entity,String.class);
            JSONObject jsRply = JSONObject.fromObject(strBody.getBody());
            //格式错误
            if(!jsRply.has(ConstStr.STATUS)){

                return ServiceError.SERVICE_FORMAT_ERROR;
            }else{
                //获取返回值
                String strResult = jsRply.getString(ConstStr.STATUS);
                //返回值判断
                if(strResult.equals(ConstStr.SUCCESS)){
                    return ServiceError.SERVICE_OK;
                }else{
                    ServiceError error = ServiceError.SERVICE_REPLY_FAIL;
                    if(jsRply.has(ConstStr.MSG)){
                        String strMsg = jsRply.getString(ConstStr.MSG);
                        error.addMsg(strMsg);
                    }
                    return error;

                }
            }

        }catch (Exception e){
            System.out.println(e);
            return ServiceError.SERVICE_ACESS_EXCEPTION;
        }
    }

    /**

     *@描述 插入装置信息

     *@参数

     *@返回值

     *@创建人  llf

     *@创建时间  2021/1/29

     *@修改人和其它信息

     */
    /*public ServiceError UpdateDevInfo(DeviceInfoDto dto){
        String url = BaseUrl+"devices/v1/update?access_token="+Token;
        //使用Restemplate来发送HTTP请求
        RestTemplate restTemplate = new RestTemplate();

        //设置请求header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());

        // 请求体，包括请求数据 body 和 请求头 headers
        HttpEntity<DeviceInfoDto> httpEntity = new HttpEntity<>(dto,headers);
        try {
            //使用 exchange 发送请求，以String的类型接收返回的数据
            //ps，我请求的数据，其返回是一个json
            ResponseEntity<String> strbody = restTemplate.postForEntity(url, httpEntity,String.class);
            //解析返回的数据
            JSONObject jsTemp = JSONObject.fromObject(strbody.getBody());
            String status = jsTemp.getString(ConstStr.STATUS);
            //返回成功
            if(status.equals(ConstStr.SUCCESS)&&jsTemp.has(ConstStr.DATA)){
                JSONObject objData = jsTemp.getJSONObject(ConstStr.DATA);
                if(objData.has(ConstStr.ACCESSTOKEN)) {
                    //设置token
                    Token = objData.getString(ConstStr.ACCESSTOKEN);
                    TokenAccessed = true;
                    ExpireSecond = objData.getInt(ConstStr.EXPIRESIN);
                    LastAuthorTime = new Date();
                }else{
                    //没有token
                    return ServiceError.SERVICE_NOTOKEN_ERROR;
                }
            }
            //失败
            else{

                System.out.println(jsTemp.toString());
                ServiceError error = ServiceError.SERVICE_REPLY_FAIL;
                if(jsTemp.has(ConstStr.MSG)) {
                    error.addMsg(jsTemp.getString(ConstStr.MSG));
                }
                return error;
            }
            return ServiceError.SERVICE_OK;

        }catch (Exception e){
            System.out.println(e);
            return  ServiceError.SERVICE_ACESS_EXCEPTION;
        }

    }*/

}
