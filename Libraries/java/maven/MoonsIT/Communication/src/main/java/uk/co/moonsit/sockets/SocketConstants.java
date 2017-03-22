/* 
  *  This software is the property of Moon's Information Technology Ltd.
  * 
  *  All rights reserved.
  * 
  *  The software is only to be used for development and research purposes.
  *  Commercial use is only permitted under license or agreement.
  * 
  *  Copyright (C)  Moon's Information Technology Ltd.
  *  
  *  Author: rupert@moonsit.co.uk
  * 
  * 
 */
package uk.co.moonsit.sockets;

/**
 *
 * @author Rupert Young
 */
public class SocketConstants {

    public static final int rBA_1_rBV_1_rCP_2_rCV_3 = 0;
    public static final int pBA_1_pBV_2_pCP_3_pCV_4 = 1;
    public static final int eBA_1_eBV_2_eCP_3_eCV_3 = 2;
    public static final int Gy_1_rBa_2_pBa_2_oBa_3 = 3;
    public static final int rBv_1_pBv_2_oBv_3 = 4;
    public static final int Gy_1_rCp_2_pCp_2_oCp_3 = 5;
    public static final int Gy_1_rCv_2_pCv_2_oCv_3 = 6;
    public static final int Gy_1_rBr_2_pBr_2_oBr_ = 7;
    public static final int Memory_1_gRaw_3_Rate_2_gSpeed_3_gOff_4 = 8;
    public static final int A_1_B_1_M_2 = 9;
    public static final int A_1 = 10;
    public static final int A_1_B_2 = 11;
    public static final int A_1_B_2_C_3 = 12;
    public static final int A_1_B_2_C_3_D_4 = 13;    
    public static final int gRaw_1_gSpeed_1_gAngle_2=14;
    public static final int gSpeed_1_armVel_2_armOut_3_armPos_4=15;

    public static String[] getConfigList() {
        return new String[]{
            "",
            "rBA_1,rBV_1,rCP_2,rCV_3;Time",
            "pBA_1,pBV_2,pCP_3,pCV_4;Time",
            "eBA_1,eBV_2,eCP_3,eCV_3;Time",
            "Gy_1,rBa_2,pBa_2,oBa_3;Time",
            "rBv_1,pBv_2,oBv_3;Time",
            "Gy_1,rCp_2,pCp_2,oCp_3;Time",
            "Gy_1,rCv_2,pCv_2,oCv_3;Time",
            "Gy_1,rBr_2,pBr_2,oBr;Time",
            "Memory_1,gRaw_3,Rate_2,gSpeed_3, gOff_4;Time",
            "A_1,B_1,M_2;Time",
            "A_1;Time",
            "A_1,B_2;Time",
            "A_1,B_2,C_3;Time",
            "A_1,B_2,C_3,D_4;Time",
            "gRaw_1,gSpeed_1,gAngle_2;Time",
            "gSpeed_1,armVel_2,armOut_3,armPos_4;Time"
        
        };
    }
}
