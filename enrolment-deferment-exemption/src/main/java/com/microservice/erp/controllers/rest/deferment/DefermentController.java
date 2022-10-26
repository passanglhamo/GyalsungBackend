package com.microservice.erp.controllers.rest.deferment;

import com.microservice.erp.domain.dto.deferment.DefermentDto;
import com.microservice.erp.services.iServices.deferment.ICreateDefermentService;
import com.microservice.erp.services.iServices.deferment.IReadDefermentService;
import com.microservice.erp.services.iServices.deferment.IUpdateDefermentService;
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

@RestController
@RequestMapping("/deferment")
@AllArgsConstructor
public class DefermentController {

    private final ICreateDefermentService service;
    private final IReadDefermentService readService;
    private final IUpdateDefermentService updateService;


    @PostMapping(value = "/save")
    public ResponseEntity<?> save(HttpServletRequest request, @ModelAttribute ICreateDefermentService.
            CreateDefermentCommand command) throws ParseException, IOException {
        return service.save(request, command);
    }

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    public Collection<DefermentDto> getAll() {

        return readService.getAll();
    }

    @PostMapping(value = "/approveByIds")
    public ResponseEntity<?> approveByIds(@RequestBody IUpdateDefermentService.UpdateDefermentCommand command) {

        return updateService.approveByIds(command);
    }

    @PostMapping(value = "/rejectByIds")
    public ResponseEntity<?> rejectByIds(@RequestBody IUpdateDefermentService.UpdateDefermentCommand command) {

        return updateService.rejectByIds(command);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public ResponseEntity<?> downloadFile(@RequestParam("url") String url) {
        FileSystemResource file = new FileSystemResource(new File(url));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
