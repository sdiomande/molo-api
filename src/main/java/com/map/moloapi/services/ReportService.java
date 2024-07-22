package com.map.moloapi.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.map.moloapi.contracts.Response;
import com.map.moloapi.contracts.reports.ReportRequest;
import com.map.moloapi.contracts.reports.ReportResponse;
import com.map.moloapi.utils.DateComparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 21/02/2024 12:59
 */
@Slf4j
@Service
public class ReportService {

    @Autowired
    ResourceLoader resourceLoader;
    List<Map<String, Object>> list = new ArrayList<>();

    Response response = new Response();

    public ResponseEntity<?> tags(ReportRequest request) {
        log.info("-- Debut recuperation badges, req : {} --", request);

        List<ReportResponse> tags;
        try {
            tags = new ObjectMapper().readValue(resourceLoader.getResource("classpath:jsons/tagsRaw.json").getFile(), new TypeReference<List<ReportResponse>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, List<ReportResponse>> collect = tags.stream()
                .peek(a -> a.setDate(a.getDateCreation().substring(0, 10)))
                .collect(Collectors.groupingBy(ReportResponse::getDate));

        SortedMap<String, List<ReportResponse>> sortedMap = new TreeMap<>(new DateComparator());
        sortedMap.putAll(collect);
        list.clear();
        sortedMap.forEach((key, value) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", key);
            map.put("nombre", value.size());
            map.put("data", value);
            list.add(map);
        });
        Long total = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("nombre", "0")))).reduce(0L, Long::sum);

        log.info("-- Fin recuperation badges --");
//        return ResponseEntity.ok(new HashMap<>(){
//            {
//                put("total", total);
//                put("data", list);
//            }
//        });

        return ResponseEntity.ok(response.success(new HashMap<>(){
            {
                put("total", total);
                put("data", list);
            }
        }));
    }

    public ResponseEntity<?> historiquePostpaye(ReportRequest request){
        log.info("-- Debut recuperation historique postpaye, req : {} --", request);

        List<ReportResponse> historiques;
        try {
            historiques = new ObjectMapper().readValue(resourceLoader.getResource("classpath:jsons/historiquePostpayeRaw.json").getFile(), new TypeReference<List<ReportResponse>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, List<ReportResponse>> collect = historiques.stream()
                .peek(a -> a.setDate(a.getDhms().substring(0, 10)))
                .collect(Collectors.groupingBy(ReportResponse::getDate));

        SortedMap<String, List<ReportResponse>> sortedMap = new TreeMap<>(new DateComparator());
        sortedMap.putAll(collect);
        list.clear();
        sortedMap.forEach((key, value) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", key);
            map.put("nombre", value.size());
            map.put("ffAmount", value.stream().map(ReportResponse::getFfAmount).reduce(0L, Long::sum));
            map.put("amount", value.stream().map(ReportResponse::getAmount).reduce(0L, Long::sum));
            map.put("data", value);
            list.add(map);
        });
        Long total = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("nombre", "0")))).reduce(0L, Long::sum);
        Long ffAmountTotal = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("ffAmount", "0")))).reduce(0L, Long::sum);
        Long amountTotal = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("amount", "0")))).reduce(0L, Long::sum);

        log.info("-- Fin recuperation historique postpaye --");
        return ResponseEntity.ok(response.success(new HashMap<>(){
            {
                put("total", total);
                put("ffAmountTotal", ffAmountTotal);
                put("amountTotal", amountTotal);
                put("data", list);
            }
        }));
    }

    public ResponseEntity<?> rechargementPartenaire(ReportRequest request){
        log.info("-- Debut recuperation rechargement par partenaire, req : {} --", request);

        List<ReportResponse> rechargements;
        try {
            rechargements = new ObjectMapper().readValue(resourceLoader.getResource("classpath:jsons/rechargementPartenaireRaw.json").getFile(), new TypeReference<List<ReportResponse>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Map<String, List<ReportResponse>> collect = rechargements.stream()
                .peek(a -> a.setDate(a.getDhms().substring(0, 10)))
                .collect(Collectors.groupingBy(ReportResponse::getDate));

        SortedMap<String, List<ReportResponse>> sortedMap = new TreeMap<>(new DateComparator());
        sortedMap.putAll(collect);
        list.clear();
        sortedMap.forEach((key, value) -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", key);
            map.put("nombre", value.size());
//            map.put("ffAmount", value.stream().map(ReportResponse::getFfAmount).reduce(0L, Long::sum));
            map.put("amount", value.stream().map(ReportResponse::getAmount).reduce(0L, Long::sum));
            map.put("data", value);
            list.add(map);
        });
        Long total = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("nombre", "0")))).reduce(0L, Long::sum);
        Long amountTotal = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("amount", "0")))).reduce(0L, Long::sum);
        log.info("-- Fin recuperation rechargement par partenaire --");
        return ResponseEntity.ok(response.success(new HashMap<>(){
            {
                put("total", total);
                put("amountTotal", amountTotal);
                put("data", list);
            }
        }));
    }

    public ResponseEntity<?> nbrTotalAbonnes(ReportRequest request){
        log.info("-- Debut recuperation nombre total abonnes, req : {} --", request);
        log.info("-- Fin recuperation nombre total abonnes --");
        return ResponseEntity.ok(response.success(new HashMap<>(){
            {
                put("nbrTotalAbonnes", 129301);
            }
        }));
    }

    public ResponseEntity<?> nbrTotalTransactions(ReportRequest request){
        log.info("-- Debut recuperation nombre total transaction, req : {} --", request);
//
//        List<ReportResponse> historiques;
//        try {
//            historiques = new ObjectMapper().readValue(resourceLoader.getResource("classpath:jsons/historiquePostpayeRaw.json").getFile(), new TypeReference<List<ReportResponse>>() {});
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        Map<String, List<ReportResponse>> collect = historiques.stream()
//                .peek(a -> a.setDate(a.getDhms().substring(0, 10)))
//                .collect(Collectors.groupingBy(ReportResponse::getDate));
//
//        SortedMap<String, List<ReportResponse>> sortedMap = new TreeMap<>(new DateComparator());
//        sortedMap.putAll(collect);
//        list.clear();
//        sortedMap.forEach((key, value) -> {
//            Map<String, Object> map = new HashMap<>();
//            map.put("date", key);
//            map.put("nombre", value.size());
//            map.put("ffAmount", value.stream().map(ReportResponse::getFfAmount).reduce(0L, Long::sum));
//            map.put("amount", value.stream().map(ReportResponse::getAmount).reduce(0L, Long::sum));
//            map.put("data", value);
//            list.add(map);
//        });
//        Long total = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("nombre", "0")))).reduce(0L, Long::sum);
//        Long ffAmountTotal = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("ffAmount", "0")))).reduce(0L, Long::sum);
//        Long amountTotal = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("amount", "0")))).reduce(0L, Long::sum);

        log.info("-- Fin recuperation nombre total transaction --");
        return ResponseEntity.ok(response.success(new HashMap<>(){
            {
                put("nbrTotalTransactions", 939301);
            }
        }));
    }

    public ResponseEntity<?> nbrTotalAbonnesRfidBip(ReportRequest request){
        log.info("-- Debut recuperation nombre total abonne rfid/bip, req : {} --", request);

//        List<ReportResponse> historiques;
//        try {
//            historiques = new ObjectMapper().readValue(resourceLoader.getResource("classpath:jsons/historiquePostpayeRaw.json").getFile(), new TypeReference<List<ReportResponse>>() {});
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        Map<String, List<ReportResponse>> collect = historiques.stream()
//                .peek(a -> a.setDate(a.getDhms().substring(0, 10)))
//                .collect(Collectors.groupingBy(ReportResponse::getDate));
//
//        SortedMap<String, List<ReportResponse>> sortedMap = new TreeMap<>(new DateComparator());
//        sortedMap.putAll(collect);
//        list.clear();
//        sortedMap.forEach((key, value) -> {
//            Map<String, Object> map = new HashMap<>();
//            map.put("date", key);
//            map.put("nombre", value.size());
//            map.put("ffAmount", value.stream().map(ReportResponse::getFfAmount).reduce(0L, Long::sum));
//            map.put("amount", value.stream().map(ReportResponse::getAmount).reduce(0L, Long::sum));
//            map.put("data", value);
//            list.add(map);
//        });
//        Long total = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("nombre", "0")))).reduce(0L, Long::sum);
//        Long ffAmountTotal = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("ffAmount", "0")))).reduce(0L, Long::sum);
//        Long amountTotal = list.stream().map(l -> Long.parseLong(String.valueOf(l.getOrDefault("amount", "0")))).reduce(0L, Long::sum);

        log.info("-- Fin recuperation nombre total abonne rfid/bip --");
        return ResponseEntity.ok(response.success(new HashMap<>(){
            {
                put("rfid", 20020);
                put("bip", 231090);
            }
        }));
    }

}
