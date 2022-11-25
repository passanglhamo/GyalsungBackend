package com.microservice.erp.controllers.rest;

import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.services.iServices.ICreateDefermentService;
import com.microservice.erp.services.iServices.IReadDefermentService;
import com.microservice.erp.services.iServices.IUpdateDefermentService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/deferment")
@AllArgsConstructor
public class DefermentController {

    private final ICreateDefermentService service;
    private final IReadDefermentService readService;
    private final IUpdateDefermentService updateService;


    @PostMapping
    public ResponseEntity<?> saveDeferment(HttpServletRequest request, @ModelAttribute ICreateDefermentService.
            CreateDefermentCommand command) throws Exception {
        return service.saveDeferment(request, command);
    }

    @GetMapping
    public List<DefermentDto> getAllDefermentList(@RequestHeader("Authorization") String authHeader) {
        return readService.getAllDefermentList(authHeader);
    }

    @PostMapping(value = "/approveByIds")
    public ResponseEntity<?> approveByIds(@RequestHeader("Authorization") String authHeader,
                                          @RequestBody IUpdateDefermentService.UpdateDefermentCommand command) {

        return updateService.approveByIds(authHeader,command);
    }

    @PostMapping(value = "/rejectByIds")
    public ResponseEntity<?> rejectByIds(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody IUpdateDefermentService.UpdateDefermentCommand command) {

        return updateService.rejectByIds(authHeader, command);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
        FileSystemResource file = new FileSystemResource(new File(url));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping(value = "/getDefermentListByToDateStatus")
    public ResponseEntity<?> getDefermentListByToDateStatus(@RequestBody IReadDefermentService.ReadDefermentCommand command) {
        return readService.getDefermentListByToDateStatus(command);
    }

}