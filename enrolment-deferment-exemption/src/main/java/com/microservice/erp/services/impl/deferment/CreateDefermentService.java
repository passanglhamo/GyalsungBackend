package com.microservice.erp.services.impl.deferment;

import com.microservice.erp.domain.mapper.deferment.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.helper.ApprovalStatus;
import com.microservice.erp.services.helper.MessageResponse;
import com.microservice.erp.services.iServices.deferment.ICreateDefermentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateDefermentService implements ICreateDefermentService {

    private final IDefermentInfoRepository repository;
    private final DefermentMapper mapper;

    Integer fileLength = 5;


    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> saveDeferment(HttpServletRequest request, CreateDefermentCommand command) {
        boolean defermentInfoExist = repository.existsByUserIdAndStatusInAndToDateGreaterThanEqual(command.getUserId(),
                Set.of(ApprovalStatus.PENDING.value(), ApprovalStatus.APPROVED.value()), command.getToDate());


        if (defermentInfoExist) {
            return new ResponseEntity<>("Deferment is already saved.", HttpStatus.ALREADY_REPORTED);
        }


        if (!Objects.isNull(command.getProofDocuments())) {
            if (command.getProofDocuments().length > fileLength) {
                return new ResponseEntity<>("You can upload maximum of 5 files.", HttpStatus.ALREADY_REPORTED);
            }
        }
        var deferment = repository.save(
                mapper.mapToEntity(
                        request, command
                )
        );

        repository.save(deferment);

        return ResponseEntity.ok(new MessageResponse("Deferment is successfully saved"));
    }


}
