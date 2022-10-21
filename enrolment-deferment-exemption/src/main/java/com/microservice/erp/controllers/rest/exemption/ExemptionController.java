package com.microservice.erp.controllers.rest.exemption;

import com.microservice.erp.domain.dto.exemption.ExemptionDto;
import com.microservice.erp.services.iServices.exemption.ICreateExemptionService;
import com.microservice.erp.services.iServices.exemption.IReadExemptionService;
import com.microservice.erp.services.iServices.exemption.IUpdateExemptionService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/exemption")
@AllArgsConstructor
public class ExemptionController {

    private final ICreateExemptionService service;
    private final IReadExemptionService readService;
    private final IUpdateExemptionService updateService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> save(HttpServletRequest request, @ModelAttribute ExemptionDto exemptionDto) throws ParseException, IOException {
        return service.save(request, exemptionDto);
    }

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<ExemptionDto> getAll() {

        return readService.getAll();
    }

    @PostMapping(value = "/approveByIds")
    public ResponseEntity<?> approveByIds(@RequestBody IUpdateExemptionService.UpdateExemptionCommand command) {

        return updateService.approveByIds(command);
    }

    @PostMapping(value = "/rejectByIds")
    public ResponseEntity<?> rejectByIds(@RequestBody IUpdateExemptionService.UpdateExemptionCommand command) {

        return updateService.rejectByIds(command);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
        FileSystemResource file = new FileSystemResource(new File(url));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
}
