package com.microservice.erp.services;

import com.microservice.erp.domain.mapper.deferment.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.deferment.ICreateDefermentService;
import com.microservice.erp.services.impl.deferment.CreateDefermentService;
import com.microservice.erp.webapp.config.TestJPAH2Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestJPAH2Config.class})
public class IDefermentInfoServiceUnitTest {

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    IDefermentInfoRepository repository;

    @InjectMocks
    CreateDefermentService service;

    @Mock
    DefermentMapper mapper;

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void happyPathTest() throws Exception {

        ICreateDefermentService.CreateDefermentCommand createCommand = createCommand();
        ResponseEntity<?> response = service.saveDeferment(null, createCommand);

        assertNotNull(response);
        assertNotNull(response.getStatusCode());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void saveWithoutToDateTest() {
        ICreateDefermentService.CreateDefermentCommand createCommand = createCommand();
        createCommand.setToDate(null);
        Set<ConstraintViolation<ICreateDefermentService.CreateDefermentCommand>> violations = validator.validate(createCommand);
        assertEquals("Till date cannot be null", violations.stream().findFirst().get().getMessage());

    }

    @Test
    public void saveWithoutReasonTest() {
        ICreateDefermentService.CreateDefermentCommand createCommand = createCommand();
        createCommand.setReasonId(null);
        Set<ConstraintViolation<ICreateDefermentService.CreateDefermentCommand>> violations = validator.validate(createCommand);
        assertEquals("Reason cannot be null", violations.stream().findFirst().get().getMessage());
    }

    @Test
    public void saveWithoutUserIdTest() {
        ICreateDefermentService.CreateDefermentCommand createCommand = createCommand();
        createCommand.setUserId(null);
        Set<ConstraintViolation<ICreateDefermentService.CreateDefermentCommand>> violations = validator.validate(createCommand);
        assertEquals("User id cannot be null", violations.stream().findFirst().get().getMessage());
    }


    private ICreateDefermentService.CreateDefermentCommand createCommand() {

        return new ICreateDefermentService.CreateDefermentCommand(
                null,
                1L,
                1L,
                null,
                new Date(),
                'P',
                null,
                null
        );
    }


}
