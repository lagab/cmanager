package com.lagab.cmanager.service.mapper;

import com.lagab.cmanager.domain.*;
import com.lagab.cmanager.service.dto.SignatureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Signature and its DTO SignatureDTO.
 */
@Mapper(componentModel = "spring", uses = {SignatureRequestMapper.class})
public interface SignatureMapper extends EntityMapper<SignatureDTO, Signature> {

    @Mapping(source = "request.id", target = "requestId")
    SignatureDTO toDto(Signature signature);

    @Mapping(source = "requestId", target = "request")
    Signature toEntity(SignatureDTO signatureDTO);

    default Signature fromId(Long id) {
        if (id == null) {
            return null;
        }
        Signature signature = new Signature();
        signature.setId(id);
        return signature;
    }
}
