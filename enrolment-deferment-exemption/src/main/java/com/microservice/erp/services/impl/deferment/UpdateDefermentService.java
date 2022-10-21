package com.microservice.erp.services.impl.deferment;

import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.mapper.deferment.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.helper.ApprovalStatus;
import com.microservice.erp.services.helper.MessageResponse;
import com.microservice.erp.services.iServices.deferment.IUpdateDefermentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateDefermentService implements IUpdateDefermentService {
    private final IDefermentInfoRepository repository;
    private final DefermentMapper mapper;
    private boolean doesDefermentExist;

    @Override
    public ResponseEntity<?> approveByIds(UpdateDefermentCommand command) {

        DefermentInfo defermentInfo = repository.findAllById(command.getDefermentIds())
                .stream()
                .filter(d->(d.getStatus().equals(ApprovalStatus.APPROVED.value())||
                        d.getStatus().equals(ApprovalStatus.REJECTED.value()))
        ).findFirst().orElse(null);

        if(!Objects.isNull(defermentInfo)){
            return new ResponseEntity<>("There are some application that are already approved or rejected.", HttpStatus.ALREADY_REPORTED);

        }
        repository.findAllById(command.getDefermentIds()).stream().map(d -> {
            d.setStatus(ApprovalStatus.APPROVED.value());
            d.setApprovalRemarks(command.getRemarks());
            return repository.save(d);
        }).map(mapper::mapToDomain).collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(new MessageResponse("Approved successfully"));
    }

    @Override
    public ResponseEntity<?> rejectByIds(@Valid UpdateDefermentCommand command) {

        DefermentInfo defermentInfo = repository.findAllById(command.getDefermentIds())
                .stream()
                .filter(d->(d.getStatus().equals(ApprovalStatus.APPROVED.value())||
                        d.getStatus().equals(ApprovalStatus.REJECTED.value()))
                ).findFirst().orElse(null);

        if(!Objects.isNull(defermentInfo)){
            return new ResponseEntity<>("There are some application that are already approved or rejected.", HttpStatus.ALREADY_REPORTED);

        }
        repository.findAllById(command.getDefermentIds()).stream().map(d -> {
            d.setStatus(ApprovalStatus.REJECTED.value());
            d.setApprovalRemarks(command.getRemarks());
            return repository.save(d);
        }).map(mapper::mapToDomain).collect(Collectors.toUnmodifiableList());

        return ResponseEntity.ok(new MessageResponse("Rejected successfully"));

    }
}
