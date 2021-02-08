package webservice.nari.AuthDto;

import lombok.Data;

/**
 * @author llf
 * @version 1.0 2021/1/30
 */
@Data
public class AccidDTO {
    public String faultId;//故障编号
    public String faultTime;//故障时间
    public String faultUnit;//故障所属单位
    public String lineName;//线路
    public String lineObjId;//线路obj_id
    public String phase;//故障相别
    public String faultTowerName;//故障杆塔
    public String faultTowerObjId;//故障杆塔obj_id
    public String baseTowerName;//基准杆塔
    public String baseTowerObjId;//基准杆塔obj_id
    public String baseTowerDistance;//基准杆塔距离
    public String startTowerName;//区间起始杆塔
    public String startTowerObjId;//区间起始杆塔OBJ_ID
    public String endTowerName;//区间终止杆
    public String endTowerObjId;//区间终止杆 OBJ_ID
    //public String lineLength;//线路总长
    public String waveIndexs;//波形id（多个id用逗号隔开）
    public String captureTime;
    public String uploadTime;
    public String localCityCode;
    public String faultReason;//故障原因
}
