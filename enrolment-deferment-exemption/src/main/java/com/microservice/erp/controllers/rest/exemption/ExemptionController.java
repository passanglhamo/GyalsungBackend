package com.microservice.erp.controllers.rest.exemption;

import com.microservice.erp.domain.dto.exemption.ExemptionDto;
import com.microservice.erp.services.iServices.exemption.ICreateExemptionService;
import com.microservice.erp.services.iServices.exemption.IReadExemptionService;
import com.microservice.erp.services.iServices.exemption.IUpdateExemptionService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/exemption")
@AllArgsConstructor
public class ExemptionController {

    private final ICreateExemptionService service;
    private final IReadExemptionService readService;
    private final IUpdateExemptionService updateService;

    @PostMapping
    public ResponseEntity<?> saveExemption(HttpServletRequest request, @ModelAttribute
    ICreateExemptionService.CreateExemptionCommand command) throws  IOException {
        return service.saveExemption(request, command);
    }

    @GetMapping
    public List<ExemptionDto> getAllExemptionList(@RequestHeader("Authorization") String authHeader) {

        return readService.getAllExemptionList(authHeader);
    }

    @PostMapping(value = "/approveByIds")
    public ResponseEntity<?> approveByIds(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody IUpdateExemptionService.UpdateExemptionCommand command) {

        return updateService.approveByIds(authHeader,command);
    }

    @PostMapping(value = "/rejectByIds")
    public ResponseEntity<?> rejectByIds(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody IUpdateExemptionService.UpdateExemptionCommand command) {

        return updateService.rejectByIds(authHeader,command);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
        FileSystemResource file = new FileSystemResource(new File(url));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
