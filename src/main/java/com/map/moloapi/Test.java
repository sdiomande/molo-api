package com.map.moloapi;

import com.map.moloapi.contracts.reports.ReportResponse;
import com.map.moloapi.utils.Utilities;

import java.util.Arrays;
import java.util.List;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 03/11/2023 15:04
 */
public class Test {
    public static void main(String[] args) {
//        System.out.println("admin : " + Utilities.encryptPassword("admin"));
//        System.out.println("partenaire : " + Utilities.encryptPassword("partenaire"));
//        System.out.println("commercial : " + Utilities.encryptPassword("commercial"));
//        System.out.println("comptable : " + Utilities.encryptPassword("comptable"));
//
//        System.out.println(Utilities.match("comptable", "$2a$10$iDCOcdUqSUx7i6kopRz30.pVJ7ETsmw.4gdrUgNDbeStNrxUJo7SW"));
        List<ReportResponse> tags = Arrays.asList(
                ReportResponse.builder().tagId("9380900010100100").date("01/04/2024 10:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100101").date("01/04/2024 10:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100102").date("01/04/2024 10:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100103").date("02/04/2024 11:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100104").date("02/04/2024 11:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100105").date("03/04/2024 09:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100106").date("04/04/2024 10:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100107").date("05/04/2024 10:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100108").date("06/04/2024 09:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100109").date("06/04/2024 10:14:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100110").date("06/04/2024 10:15:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100111").date("06/04/2024 10:16:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100112").date("06/04/2024 08:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100113").date("11/04/2024 14:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100114").date("11/04/2024 16:11:09").etat("CREE").build(),
                ReportResponse.builder().tagId("9380900010100115").date("20/04/2024 19:11:09").etat("CREE").build()
        );

        System.out.println(Utilities.encryptPassword("admin"));
    }
}
