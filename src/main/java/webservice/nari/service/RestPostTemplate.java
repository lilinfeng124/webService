package webservice.nari.service;

import net.sf.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sun.security.util.Password;
import webservice.nari.AuthDto.TokenDTO;
import webservice.nari.ConstStr.ConstStr;
import webservice.nari.ConstStr.ServiceError;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author llf
 * @version 1.0 2021/1/30
 */
public class RestPostTemplate<T> {


    private String BaseUrl = "http://152.136.36.86:1899/zuul/dataServer/";

    /**

     *@描述 rest请求的模板函数

     *@参数

     *@返回值

     *@创建人  llf

     *@创建时间  2021/1/30

     *@修改人和其它信息

     */
    public ServiceError PostRequest(String strUrlDetail,String strToken,T sendObject){
        //请求路径
        String url = BaseUrl+strUrlDetail+"?access_token="+strToken;
        //使用Restemplate来发送HTTP请求
        RestTemplate restTemplate = new RestTemplate();

        //设置请求header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());

        // 请求体，包括请求数据 body 和 请求头 headers
        HttpEntity<T> httpEntity = new HttpEntity<>(sendObject,headers);
        try {
            //使用 exchange 发送请求，以String的类型接收返回的数据
            //ps，我请求的数据，其返回是一个json
            ResponseEntity<String> strbody = restTemplate.postForEntity(url, httpEntity,String.class);
            //解析返回的数据
            JSONObject jsTemp = JSONObject.fromObject(strbody.getBody());
            //没有status，格式跟预期不一样
            if(!jsTemp.has(ConstStr.STATUS)){
                return ServiceError.SERVICE_FORMAT_ERROR;
            }
            String status = jsTemp.getString(ConstStr.STATUS);
            //返回成功
            if(status.equals(ConstStr.SUCCESS)){
                return ServiceError.SERVICE_OK;
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


        }catch (Exception e){
            System.out.println(e);
            return  ServiceError.SERVICE_ACESS_EXCEPTION;
        }

    }

    /**

     *@描述 获取授权的token

     *@参数 用户名、密码

     *@返回值 token

     *@创建人  llf

     *@创建时间  2021/1/30

     *@修改人和其它信息

     */
    public TokenDTO AccessAuthToken(String UserName, String PassWord){
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
                    TokenDTO tokendto = new TokenDTO();
                    String strToken = objData.getString(ConstStr.ACCESSTOKEN);
                    tokendto.setToken(strToken);
                    boolean TokenAccessed = true;
                    int ExpireSecond = objData.getInt(ConstStr.EXPIRESIN);
                    Date LastAuthorTime = new Date();
                    tokendto.setExpireSecond(ExpireSecond);
                    tokendto.setTokenAccessed(TokenAccessed);
                    tokendto.setLastAuthorTime(LastAuthorTime);


                    return tokendto;
                }else{
                    //没有token
                    return null;
                }
            }
            //失败
            else{


                return null;
            }


        }catch (Exception e){
            System.out.println(e);
            return  null;
        }

    }
}
