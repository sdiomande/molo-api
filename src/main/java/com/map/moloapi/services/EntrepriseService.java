package com.map.moloapi.services;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.contracts.entreprises.EntrepriseRequest;
import com.map.moloapi.contracts.entreprises.UpdateEntrepriseRequest;
import com.map.moloapi.entities.Entreprise;
import com.map.moloapi.entities.User;
import com.map.moloapi.exceptions.rest.AlreadyExistException;
import com.map.moloapi.mappers.BaseMapper;
import com.map.moloapi.repositories.EntrepriseRepository;
import com.map.moloapi.repositories.ParamRepository;
import com.map.moloapi.repositories.UserRepository;
import com.map.moloapi.utils.Utilities;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 13/02/2024 11:36
 */
@Service
@Slf4j
public class EntrepriseService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    EntrepriseRepository entrepriseRepository;

    @Autowired
    ParamRepository paramRepository;

    @Autowired
    Utilities utilities;

    @Autowired
    BaseMapper mapper;

    @Autowired
    Response response;

    public Response all() {
        log.info("-- Debut recuperation entreprises--");
        List<Entreprise> entreprises = entrepriseRepository.findAll();
        log.info("-- Fin recuperation entreprises--");
        return response.success(mapper.toEntrepriseDtos(entreprises));
    }

    public Response create(@Valid EntrepriseRequest request) {
        log.info("-- Debut creation entreprise, req : {}", request);
        // validation
        if (entrepriseRepository.existsByName(request.getName())) {
            log.error("-- Le nom : {} existe deja --", request.getName());
            throw new AlreadyExistException("Le nom existe deja : %s", request.getName());
        }

        if (entrepriseRepository.existsByEmail(request.getEmail())) {
            log.error("-- L'email : {} existe deja --", request.getEmail());
            throw new AlreadyExistException("L'email existe deja : %s", request.getEmail());
        }

        if (entrepriseRepository.existsByRccm(request.getRccm())) {
            log.error("-- Le rccm : {} existe deja --", request.getRccm());
            throw new AlreadyExistException("Le rccm existe deja : %s", request.getRccm());
        }

        if (request.getWebsite() != null && entrepriseRepository.existsByWebsite(request.getWebsite())) {
            log.error("-- Le site web : {} existe deja --", request.getWebsite());
            throw new AlreadyExistException("Le site web existe deja : %s", request.getWebsite());
        }

//        if (request.getFirstCustomerId() == null || request.getLastCustomerId() == null) {
//            log.error("-- Plage non reseigné --");
//            return response.error("La plage n'est pas renseigné");
//        }
//
//        if (request.getFirstCustomerId() >= request.getLastCustomerId()) {
//            log.error("-- La plage de client n'est pas valide, premier : {}, dernier : {} --", request.getFirstCustomerId(), request.getLastCustomerId());
//            return response.error("La plage de client n'est pas valide");
//        }
//
//        // customerId plage verification
//        Response checkRange = checkRange(request.getFirstCustomerId(), request.getLastCustomerId(), entrepriseRepository.findAll());
//        if (checkRange.isHasError()) {
//            log.error("-- Chevauchement des plages --");
//            return checkRange;
//        }
//
//        // ip verification
//        if (request.getRemoteIp() != null) {
//            InetAddressValidator validator = InetAddressValidator.getInstance();
//            if (!validator.isValid(request.getRemoteIp())) {
//                log.error("-- Remote IP n'est pas valide : {} --", request.getRemoteIp());
//                return response.error("L'IP distant n'est pas valide : " + request.getRemoteIp());
//            }
//        }

        Entreprise entreprise = mapper.toEntity(request);
        entreprise.cAudit();
        entreprise.uAudit();
        entreprise.setCode(utilities.nextInternalId());
        entrepriseRepository.saveAndFlush(entreprise);
        log.info("-- Fin creation entreprise ok --");
        return response.success();
    }

    public Response updateCertificat(UpdateEntrepriseRequest request) {
        log.info("-- Debut mise a jour du certificat partenaire : {} --", request);
        MultipartFile file = request.getFile();
        Optional<Entreprise> partnerOptional = entrepriseRepository.findById(request.getId());
        if (request.getId() == null || partnerOptional.isEmpty()) {
            log.error("-- Acun partenaire trouvé : {} --", request.getId());
            return response.error("Aucun partenaire trouvé");
        }

        Optional<User> userOptional = userRepository.findById(Utilities.userConnectedID());
        if (userOptional.isPresent()) {
            if (userOptional.get().getEntreprise() == null) {
                log.error("-- L'utilisateur {} n'est lie a aucun partenaire --", userOptional.get().getLogin());
                return response.error("Vous n'etes pas habilite a modifier ce partenaire");
            }

//            if (!userOptional.get().getPartner().getId().equalsIgnoreCase(request.getId())) {
//                log.error("-- L'utilisateur {} n'est pas lie au partenaire {} --", userOptional.get().getLogin(), request.getId());
//                return response.error("Vous n'etes pas habilite a modifier ce partenaire");
//            }
        }

        Entreprise entreprise = partnerOptional.get();

        if (file != null && !file.isEmpty()) {
            try {
                String targetFile = utilities.getParam("KEYFILE_FOLDER").concat(File.separator).concat(file.getOriginalFilename());
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(targetFile));
//                entreprise.setPublicKeyPath(targetFile);
//                entreprise.setPublicKeyFile(file.getOriginalFilename());
//                entreprise.setPublicKeyContent(FileUtils.readFileToString(new File(targetFile), StandardCharsets.UTF_8.name()));
                log.info("-- fichier enregistre avec succes --");
                entrepriseRepository.saveAndFlush(entreprise);
            } catch (IOException e) {
                log.error("## Erreur lors de l'upload du fichier ##");
                e.printStackTrace();
                return response.error();
            }
        }
        log.info("-- Fin mise a jour du certificat partenaire --");
        return response.success();
    }

    public Response update(String id, EntrepriseRequest request) {
        log.info("-- Debut mise a jour partenaire, id : {}, req : {} --", id, request);

        if (entrepriseRepository.nameNotUpdatable(id, request.getName())) {
            log.error("-- Le nom : {} existe deja --", request.getName());
            return response.error(String.format("Le nom %s existe deja", request.getName()));
        }

        // ip verification
//        if (request.getRemoteIp() != null) {
//            InetAddressValidator validator = InetAddressValidator.getInstance();
//            if (!validator.isValid(request.getRemoteIp())) {
//                log.error("-- Remote IP n'est pas valide : {} --", request.getRemoteIp());
//                return response.error("L'IP distant n'est pas valide : " + request.getRemoteIp());
//            }
//        }

        if (!entrepriseRepository.existsById(id)) {
            log.error("-- Fin modification partenaires, id : {} n'existe pas --", id);
            return response.error("Aucun partenaire trouvé");
        }

        Entreprise entreprise = entrepriseRepository.findById(id).get();
        entreprise.setName(request.getName());
//        entreprise.setPassageLane(request.getPassageLane());
//        entreprise.setReloadLane(request.getReloadLane());
//        entreprise.setStaffCode(request.getStaffCode());
        entreprise.uAudit();
        entrepriseRepository.saveAndFlush(entreprise);
        log.info("-- Fin mise a jour partenaire --");
        return response.success();
    }

    public Response delete(String id) {
        log.info("-- Debut suppression partenaires, id : {} --", id);
        if (entrepriseRepository.existsById(id)) {
            // check if entreprise as link with other table before
            if (!userRepository.findByPartnerId(id).isEmpty()) {
                log.error("## Impossible de supprimer ce partenaire, il est lie à des utilisateurs ##");
                return response.error("Impossible de supprimer ce partenaire, il est lie à des utilisateurs");
            }
            //make softdelete later
            entrepriseRepository.deleteById(id);
            log.info("-- Fin suppression partenaires --");
            return response.success();
        }
        log.error("-- Fin suppression partenaires, id : {} n'existe pas --", id);
        return response.error("Aucun partenaire trouvé");
    }

    public Response checkRange(Long begin, Long end, List<Entreprise> entreprises) {
        if (!entreprises.isEmpty()) {
            List<Map<String, String>> listPlage = new ArrayList<>();
            for (Entreprise entreprise : entreprises) {
//                listPlage.add(new HashMap<>(Map.of(entreprise.getFirstCustomerId(), entreprise.getLastCustomerId())));


//                if ((begin >= Long.parseLong(entreprise.getFirstCustomerId())
//                        && begin <= Long.parseLong(entreprise.getLastCustomerId()))
//                        || (end >= Long.parseLong(entreprise.getFirstCustomerId())
//                        && end <= Long.parseLong(entreprise.getLastCustomerId()))) {
//                    String msg = String.format("La plage renseigné [%s, %s] chevauche avec celle du partenaire %s", begin, end, entreprise.getName());
//                    return response.error(msg);
//                }
            }
            List<String> plageExistant = new ArrayList<>();
            for (Map<String, String> plage : listPlage) {
                plage.forEach((k, v) -> {
                    for (long i = Long.parseLong(k); i < Long.parseLong(v) + 1; i++) {
                        plageExistant.add(String.valueOf(i));
                    }
                });
            }

            List<String> newPlageToAdd = new ArrayList<>();
            for (long j = begin; j < end + 1; j++) {
                newPlageToAdd.add(String.valueOf(j));
            }

            List<String> intersectionPlage = plageExistant.stream().filter(newPlageToAdd::contains).toList();

            if (!intersectionPlage.isEmpty()) {
                String msg = String.format("La plage renseigné [%s, %s] chevauche avec un partenaire", begin, end);
                return response.error(msg);
            }

        }
        return response.success();
    }

    public Response activate(String id) {
        log.error("-- Debut activation/desactivation partenaire --, id : {}", id);
        Optional<Entreprise> partnerOptional = entrepriseRepository.findById(id);
        if (partnerOptional.isPresent()) {
            Entreprise entreprise = partnerOptional.get();
            entreprise.activate();
            entrepriseRepository.saveAndFlush(entreprise);
            log.info("-- Fin activation/desactivation partenaire --");
            return response.success();
        }
        log.error("## Aucun partenaire trouvé ##");
        return response.error("Aucun partenaire trouvé ");
    }


}
