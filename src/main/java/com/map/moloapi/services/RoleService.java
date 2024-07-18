package com.map.moloapi.services;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.dtos.RoleDto;
import com.map.moloapi.entities.Role;
import com.map.moloapi.entities.User;
import com.map.moloapi.mappers.BaseMapper;
import com.map.moloapi.repositories.RoleRepository;
import com.map.moloapi.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author DIOMANDE Souleymane
 * @Date 29/03/2023 23:47
 */
@Service
@Slf4j
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BaseMapper mapper;

    Response response = new Response();

    public Response all() {
        log.info("-- Debut recuperation roles --");
        List<RoleDto> roles = mapper.toRoleDtos(roleRepository.findAll(Sort.by(Sort.Direction.ASC, "updatedAt")));
        log.info("-- Fin recuperation roles --");
        return response.success(roles);
    }

    public Response findById(String id) {
        log.info("-- Debut recuperation role, id : {} --", id);
        Optional<Role> role = roleRepository.findById(id);
        if (!role.isPresent()) {
            log.error("-- Aucun role trouvée --");
            return response.error("Aucun role trouvée");
        }
        log.info("-- Fin recuperation role --");
        return response.success(mapper.toDto(role.get()));
    }

    public Response create(RoleDto request) {
        log.info("-- Debut creation role : {} --", request);
        if (roleRepository.existsByLibelle(request.getLibelle())) {
            log.error("-- Role existe deja --");
            return response.error("Role deja existant : " + request.getLibelle());
        }
        Role role = mapper.toEntity(request);
        role.cAudit();
        Role roleSaved = roleRepository.saveAndFlush(role);
        log.info("-- Fin creation role --");
        return response.success(mapper.toDto(roleSaved));
    }

    public Response update(String id, RoleDto request) {
        log.info("-- Debut modification role, id {}, req : {} --", id, request);
        if (roleRepository.libelleNotUpdatable(id, request.getLibelle())) {
            log.error("-- Le libelle existe deja --");
            return response.error("Role avec le libelle deja existant : " + request.getLibelle());
        }
        Role role = mapper.toEntity(request);
        role.setId(id);
        role.uAudit();
        Role roleSaved = roleRepository.saveAndFlush(role);
        log.info("-- Fin modification role --");
        return response.success(mapper.toDto(roleSaved));
    }

    public Response delete(String id) {
        log.info("-- Debut suppression role, id {} --", id);
        if (!roleRepository.existsById(id)) {
            log.error("-- Aucun role trouvé --");
            return response.error("Aucun role trouvé");
        }

        if (userRepository.existsByRoleId(id)){
            log.error("-- Le role ne peut etre supprimé car il est lié a des utilisateurs --");
            return response.error("Ce role ne peut etre supprimé car il est lié à des utilisateurs");
        }
        roleRepository.deleteById(id);
        log.info("-- Fin suppression role --");
        return response.success();
    }
}
