package adsyf.renewables.v2;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public enum DistributionArea {
    ELLC(10,"Eastern England","UK Power Networks","0800 029 4285","_A"),
    EMEB(11,"East Midlands","National Grid (formerly Western Power Distribution)","0800 096 3080","_B"),
    LOND(12,"London","UK Power Networks","0800 029 4285","_C"),
    MANW(13,"Merseyside and Northern Wales","SP Energy Networks","0330 10 10 444","_D"),
    MIDE(14,"West Midlands","National Grid (formerly Western Power Distribution)","0800 096 3080","_E"),
    NEEB(15,"North Eastern England","Northern Powergrid","0800 011 3332","_F"),
    NORW(16,"North Western England","Electricity North West","0800 048 1820","_G"),
    HYDE(17,"Northern Scotland","Scottish & Southern Electricity Networks","0800 048 3516","_P"),
    SPOW(18,"Southern Scotland", "SP Energy Networks", "0330 10 10 444", "_N"),
    SEEB(19,"South Eastern England","UK Power Networks","0800 029 4285","_J"),
    SOUT(20,"Southern England","Scottish & Southern Electricity Networks","0800 048 3516","_H"),
    SWAE(21, "Southern Wales", "National Grid (formerly Western Power Distribution)", "0800 096 3080", "_K"),
    SWEB(22, "South Western England", "National Grid (formerly Western Power Distribution)", "0800 096 3080", "_L"),
    YELG(23, "Yorkshire","Northern Powergrid", "0800 011 3332","_M");
    Integer id;
    String name;
    String operator;
    String phoneNumber;
    String gspGroupId;
    DistributionArea(Integer id, String name, String operator, String phoneNumber, String gspGroupId){
        this.id=id;
        this.name=name;
        this.operator=operator;
        this.phoneNumber=phoneNumber;
        this.gspGroupId=gspGroupId;
    }
    public static DistributionArea getDistributionArea(String gspGroupId){
        DistributionArea daForRet = null;
        for (DistributionArea da: DistributionArea.values()){
            if (gspGroupId.equals(da.gspGroupId)){
                daForRet=da;
                break;
            }
        }
        return daForRet;
    }
    public String getGspId(){
        return this.gspGroupId;
    }
}
