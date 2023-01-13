package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.AutoExemptionDto;
import com.microservice.erp.domain.entities.AutoExemption;
import com.microservice.erp.services.iServices.IAutoExemptionService;
import com.microservice.erp.services.iServices.IEnrolmentInfoService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/autoExemption")
@AllArgsConstructor
public class AutoExemptionController {

    private final IAutoExemptionService iAutoExemptionService;

    @PostMapping(value = "/readFile")
    public ResponseEntity<?> readFile(@ModelAttribute AutoExemptionDto autoExemptionDto) throws Exception {
        return iAutoExemptionService.readFile(autoExemptionDto);
    }

    @PostMapping(value = "/uploadFile")
    public ResponseEntity<?> uploadFile(HttpServletRequest request, @ModelAttribute AutoExemptionDto autoExemptionDto) throws Exception {
        return iAutoExemptionService.uploadFile(request, autoExemptionDto);
    }

    @GetMapping(value = "/getUploadedFiles")
    public ResponseEntity<?> getUploadedFiles() {
        return iAutoExemptionService.getUploadedFiles();
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
        FileSystemResource file = new FileSystemResource(new File(url));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
    public ResponseEntity<?> deleteFile(@RequestParam("fileId") BigInteger fileId) {
        return iAutoExemptionService.deleteFile(fileId);
    }

    @RequestMapping(value = "/getExemptedList", method = RequestMethod.GET)
    public ResponseEntity<?> getExemptedList() {
        return iAutoExemptionService.getExemptedList();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseEntity<?> update(@Valid @RequestBody AutoExemption autoExemption) {
        return iAutoExemptionService.update(autoExemption);
    }

    @PostMapping(value = "/deleteList")
    public ResponseEntity<?> deleteList(@RequestBody IAutoExemptionService.AutoExemptionCommand command) {
        return iAutoExemptionService.deleteList(command);
    }

}
