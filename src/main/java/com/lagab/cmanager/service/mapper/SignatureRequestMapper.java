package com.lagab.cmanager.service.mapper;

import com.lagab.cmanager.domain.*;
import com.lagab.cmanager.service.dto.SignatureRequestDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SignatureRequest and its DTO SignatureRequestDTO.
 */
@Mapper(componentModel = "spring", uses = {ContractMapper.class})
public interface SignatureRequestMapper extends EntityMapper<SignatureRequestDTO, SignatureRequest> {

    @Mapping(source = "contract.id", target = "contractId")
    SignatureRequestDTO toDto(SignatureRequest signatureRequest);

    @Mapping(target = "signatures", ignore = true)
    @Mapping(source = "contractId", target = "contract")
    SignatureRequest toEntity(SignatureRequestDTO signatureRequestDTO);

    default SignatureRequest fromId(Long id) {
        if (id == null) {
            return null;
        }
        SignatureRequest signatureRequest = new SignatureRequest();
        signatureRequest.setId(id);
        return signatureRequest;
    }
}
