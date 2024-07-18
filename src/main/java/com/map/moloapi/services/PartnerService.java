package com.map.moloapi.services;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.contracts.partners.PartnerRequest;
import com.map.moloapi.contracts.partners.UpdatePartnerRequest;
import com.map.moloapi.entities.Partner;
import com.map.moloapi.entities.User;
import com.map.moloapi.mappers.BaseMapper;
import com.map.moloapi.repositories.ParamRepository;
import com.map.moloapi.repositories.PartnerRepository;
import com.map.moloapi.repositories.UserRepository;
import com.map.moloapi.utils.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 13/02/2024 11:36
 */
@Service
@Slf4j
public class PartnerService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PartnerRepository partnerRepository;

    @Autowired
    ParamRepository paramRepository;

    @Autowired
    Utilities utilities;

    @Autowired
    BaseMapper mapper;

    Response response = new Response();

    public Response all() {
        log.info("-- Debut recuperation partenaires--");
        List<Partner> partners = partnerRepository.findAll();
        log.info("-- Fin recuperation partenaires--");
        return response.success(mapper.toPartnerDtos(partners));
    }

    public Response create(PartnerRequest request) {
        log.info("-- Debut creation partner --");
        log.info("-- Partner : {}", request);
        // validation
        if (partnerRepository.existsByName(request.getName())) {
            log.error("-- Le nom : {} existe deja --", request.getName());
            return response.error(String.format("Le nom %s existe deja", request.getName()));
        }

        if (request.getFirstCustomerId() == null || request.getLastCustomerId() == null) {
            log.error("-- Plage non reseigné --");
            return response.error("La plage n'est pas renseigné");
        }

        if (request.getFirstCustomerId() >= request.getLastCustomerId()) {
            log.error("-- La plage de client n'est pas valide, premier : {}, dernier : {} --", request.getFirstCustomerId(), request.getLastCustomerId());
            return response.error("La plage de client n'est pas valide");
        }

        // customerId plage verification
        Response checkRange = checkRange(request.getFirstCustomerId(), request.getLastCustomerId(), partnerRepository.findAll());
        if (checkRange.isHasError()) {
            log.error("-- Chevauchement des plages --");
            return checkRange;
        }

        // ip verification
        if (request.getRemoteIp() != null) {
            InetAddressValidator validator = InetAddressValidator.getInstance();
            if (!validator.isValid(request.getRemoteIp())) {
                log.error("-- Remote IP n'est pas valide : {} --", request.getRemoteIp());
                return response.error("L'IP distant n'est pas valide : " + request.getRemoteIp());
            }
        }

        Partner partner = mapper.toEntity(request);
        partner.cAudit();
        partner.uAudit();
        // generate field
        String partnerCode = utilities.nextInternalId();
        partner.setPartnerCode(partnerCode);
        partner.setGrantToken(utilities.generateGrantToken(partnerCode));
        // save partner data
        partnerRepository.saveAndFlush(partner);
        log.info("-- Fin creation partner ok --");
        return new Response().success();
    }

    public Response updateCertificat(UpdatePartnerRequest request) {
        log.info("-- Debut mise a jour du certificat partenaire : {} --", request);
        MultipartFile file = request.getFile();
        Optional<Partner> partnerOptional = partnerRepository.findById(request.getId());
        if (request.getId() == null || partnerOptional.isEmpty()) {
            log.error("-- Acun partenaire trouvé : {} --", request.getId());
            return response.error("Aucun partenaire trouvé");
        }

        Optional<User> userOptional = userRepository.findById(Utilities.userConnectedID());
        if (userOptional.isPresent()) {
            if (userOptional.get().getPartner() == null) {
                log.error("-- L'utilisateur {} n'est lie a aucun partenaire --", userOptional.get().getLogin());
                return response.error("Vous n'etes pas habilite a modifier ce partenaire");
            }

            if (!userOptional.get().getPartner().getId().equalsIgnoreCase(request.getId())) {
                log.error("-- L'utilisateur {} n'est pas lie au partenaire {} --", userOptional.get().getLogin(), request.getId());
                return response.error("Vous n'etes pas habilite a modifier ce partenaire");
            }
        }

        Partner partner = partnerOptional.get();

        if (file != null && !file.isEmpty()) {
            try {
                String targetFile = utilities.getParam("KEYFILE_FOLDER").concat(File.separator).concat(file.getOriginalFilename());
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(targetFile));
                partner.setPublicKeyPath(targetFile);
                partner.setPublicKeyFile(file.getOriginalFilename());
                partner.setPublicKeyContent(FileUtils.readFileToString(new File(targetFile), StandardCharsets.UTF_8.name()));
                log.info("-- fichier enregistre avec succes --");
                partnerRepository.saveAndFlush(partner);
            } catch (IOException e) {
                log.error("## Erreur lors de l'upload du fichier ##");
                e.printStackTrace();
                return response.error();
            }
        }
        log.info("-- Fin mise a jour du certificat partenaire --");
        return response.success();
    }

    public Response update(String id, PartnerRequest request) {
        log.info("-- Debut mise a jour partenaire, id : {}, req : {} --", id, request);

        if (partnerRepository.nameNotUpdatable(id, request.getName())) {
            log.error("-- Le nom : {} existe deja --", request.getName());
            return response.error(String.format("Le nom %s existe deja", request.getName()));
        }

        // ip verification
        if (request.getRemoteIp() != null) {
            InetAddressValidator validator = InetAddressValidator.getInstance();
            if (!validator.isValid(request.getRemoteIp())) {
                log.error("-- Remote IP n'est pas valide : {} --", request.getRemoteIp());
                return response.error("L'IP distant n'est pas valide : " + request.getRemoteIp());
            }
        }

        if (!partnerRepository.existsById(id)) {
            log.error("-- Fin modification partenaires, id : {} n'existe pas --", id);
            return response.error("Aucun partenaire trouvé");
        }

        Partner partner = partnerRepository.findById(id).get();
        partner.setName(request.getName());
        partner.setPassageLane(request.getPassageLane());
        partner.setReloadLane(request.getReloadLane());
        partner.setStaffCode(request.getStaffCode());
        partner.uAudit();
        partnerRepository.saveAndFlush(partner);
        log.info("-- Fin mise a jour partenaire --");
        return response.success();
    }

    public Response delete(String id) {
        log.info("-- Debut suppression partenaires, id : {} --", id);
        if (partnerRepository.existsById(id)) {
            // check if partner as link with other table before
            if (!userRepository.findByPartnerId(id).isEmpty()) {
                log.error("## Impossible de supprimer ce partenaire, il est lie à des utilisateurs ##");
                return response.error("Impossible de supprimer ce partenaire, il est lie à des utilisateurs");
            }
            //make softdelete later
            partnerRepository.deleteById(id);
            log.info("-- Fin suppression partenaires --");
            return response.success();
        }
        log.error("-- Fin suppression partenaires, id : {} n'existe pas --", id);
        return response.error("Aucun partenaire trouvé");
    }

    public Response checkRange(Long begin, Long end, List<Partner> partners) {
        if (!partners.isEmpty()) {
            List<Map<String, String>> listPlage = new ArrayList<>();
            for (Partner partner : partners) {
                listPlage.add(new HashMap<>(Map.of(partner.getFirstCustomerId(), partner.getLastCustomerId())));


//                if ((begin >= Long.parseLong(partner.getFirstCustomerId())
//                        && begin <= Long.parseLong(partner.getLastCustomerId()))
//                        || (end >= Long.parseLong(partner.getFirstCustomerId())
//                        && end <= Long.parseLong(partner.getLastCustomerId()))) {
//                    String msg = String.format("La plage renseigné [%s, %s] chevauche avec celle du partenaire %s", begin, end, partner.getName());
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
        Optional<Partner> partnerOptional = partnerRepository.findById(id);
        if (partnerOptional.isPresent()) {
            Partner partner = partnerOptional.get();
            partner.activate();
            partnerRepository.saveAndFlush(partner);
            log.info("-- Fin activation/desactivation partenaire --");
            return response.success();
        }
        log.error("## Aucun partenaire trouvé ##");
        return response.error("Aucun partenaire trouvé ");
    }


}
