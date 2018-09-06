package com.lagab.cmanager.service.mapper;

import com.lagab.cmanager.domain.*;
import com.lagab.cmanager.service.dto.ContractDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Contract and its DTO ContractDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class})
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {

    @Mapping(source = "project.id", target = "projectId")
    ContractDTO toDto(Contract contract);

    @Mapping(target = "requests", ignore = true)
    @Mapping(source = "projectId", target = "project")
    Contract toEntity(ContractDTO contractDTO);

    default Contract fromId(Long id) {
        if (id == null) {
            return null;
        }
        Contract contract = new Contract();
        contract.setId(id);
        return contract;
    }
}
