//package com.map.moloapi.partners;
//
//import com.map.moloapi.contracts.Response;
//import com.map.moloapi.entities.Partner;
//import com.map.moloapi.services.PartnerService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class PlagePartenaireTest {
//
//    @Autowired
//            PartnerService partnerService;
//
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//    List<Partner> partners = new ArrayList<>(Arrays.asList(
//            new Partner(UUID.randomUUID().toString(), "test", "test", "test", "PART1","test", "test","test","test", "127.0.0.1", false,"test", "0", "100", "test", "test", true, sdf.format(new Date()), "test", sdf.format(new Date()), "test"),
//            new Partner(UUID.randomUUID().toString(), "test1", "test1", "test1", "PART2","test1", "test1","test1","test1", "127.0.0.1", false,"test1", "200", "300", "test1", "test1", true, sdf.format(new Date()), "test1", sdf.format(new Date()), "test1")
//    ));
//
//    @Test
//    void checkrangeFailed(){ // La plage ne se chevauche pas d'où léchec du test
//        Response response = partnerService.checkRange(50L, 150L, partners);
//        assertThat(response.getMessage()).isEqualTo("La plage renseigné [10, 2000] chevauche avec un partenaire");
//    }
//
//    @Test
//    void checkrangeSuccess(){ // La palge se chevauche d'où la réussite du test
//        Response response = partnerService.checkRange(150L, 300L, partners);
//        assertThat(response.getMessage()).isEqualTo("La plage renseigné [150, 300] chevauche avec un partenaire");
//    }
//
//}
