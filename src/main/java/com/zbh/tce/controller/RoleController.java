package com.zbh.tce.controller;


import com.zbh.tce.common.constant.UrlConstant;
import com.zbh.tce.dto.DataTablesOutput;
import com.zbh.tce.dto.RoleDTO;
import com.zbh.tce.entity.Role;
import com.zbh.tce.entity.criteria.RoleCriteria;
import com.zbh.tce.mapper.RoleMapper;
import com.zbh.tce.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(UrlConstant.ROLE_API)
@RequiredArgsConstructor
public class RoleController extends BaseController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GetMapping
    public ResponseEntity<DataTablesOutput<RoleDTO>> getAllRolesWithPagniation(RoleCriteria roleCriteria, Pageable pageable) {
        log.trace("Inside getAllRolesWithPagniation method.");
        log.debug("RoleCriteria: {} | Pageable: {}", roleCriteria, pageable);
        Page<Role> rolePage = roleService.findAll(roleCriteria, pageable);
        return ResponseEntity.ok().body(createDataTableOutput(rolePage.map(roleMapper::toDTO), roleService.count()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        log.trace("Inside getAllRoles method.");
        List<Role> roleList = roleService.findAll();
        return ResponseEntity.ok().body(roleMapper.toDTO(roleList));
    }

}
