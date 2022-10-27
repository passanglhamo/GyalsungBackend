package com.microservice.erp.services.impl.exemption;

import com.microservice.erp.domain.dto.exemption.ExemptionDto;
import com.microservice.erp.domain.mapper.exemption.ExemptionMapper;
import com.microservice.erp.domain.repositories.IExemptionInfoRepository;
import com.microservice.erp.services.helper.ApprovalStatus;
import com.microservice.erp.services.helper.MessageResponse;
import com.microservice.erp.services.iServices.exemption.ICreateExemptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateExemptionService implements ICreateExemptionService {
    private final IExemptionInfoRepository repository;
    private final ExemptionMapper mapper;

    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> saveExemption(HttpServletRequest request, CreateExemptionCommand command) throws IOException {
        boolean exemptionInfoExist = repository.existsByUserIdAndStatusIn(command.getUserId(),
                Set.of(ApprovalStatus.PENDING.value(), ApprovalStatus.APPROVED.value()));

        if (exemptionInfoExist) {
            return new ResponseEntity<>("Exemption is already saved.", HttpStatus.ALREADY_REPORTED);
        }
        var exemption = repository.save(
                mapper.mapToEntity(
                        request, command
                )
        );

        repository.save(exemption);

        return ResponseEntity.ok(new MessageResponse("An acknowledgement notifcation will be sent  to you as soon as you submit your  application.\" +\n" +
                "                    \"Your Deferment application will be  reviewed and the outcome  of the deferment wil be sent \" +\n" +
                "                    \" to you throught your email. If you  are not approved for deferment , you will have to complete the \" +\n" +
                "                    \" Gyalsung pre-enlistment procedure"));
    }
}
