package webservice.nari.ConstStr;

/**
 * @author llf
 * @version 1.0 2021/1/29
 */
public enum ServiceError {

    //RPC层调用错误码
    SERVICE_OK(000,"正确")
    ,SERVICE_REPLY_FAIL(001,"服务器返回失败")
    ,SERVICE_ACESS_EXCEPTION(002,"服务器访问过程出现异常")
    ,SERVICE_NOTOKEN_ERROR(003,"服务器没有返回token")
    ,SERVICE_FORMAT_ERROR(004,"返回报文格式错误")
    ,SERVICE_TOKEN_INVALID(005,"token失效了")
    ,DATABASE_OPERATION_FAIL(005,"本地数据库操作失败")
    ;

    private String msg;
    private int code;

    private ServiceError(int code,String msg)
    {
        this.code=code;
        this.msg=msg;
    }

    public String getMsg()
    {
        return this.msg;
    }
    public int getCode() {
        return this.code;
    }
    public void addMsg(String msg){
        this.msg += msg;
    }

}
