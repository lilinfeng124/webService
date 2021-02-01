package webservice.nari.AuthDto;

/**
 * @author llf
 * @version 1.0 2021/1/30
 */
public class AccidDTO {
    public String acciId;//故障编号
    public String acciTimeFormat;//故障时间
    public String eleoffName;//故障所属局
    public String mlName;//线路
    public String diaElepo;//故障杆塔
    public String fsltypeName;//故障相别
    public String vovelName;//电压等级
    public String faultReason;//故障原因
    public String errorDistance;//距离基准塔距离
    public String baseElepo;//基准杆塔
    public String elepoid1;//区间起始杆塔
    public String elepoid2;//区间终止杆塔
    public String lineLength;//线路总长
    public String waveBatchId;//波形id（多个id用逗号隔开）
}
