package com.microservice.erp.services;

import com.microservice.erp.domain.mapper.deferment.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.deferment.ICreateDefermentService;
import com.microservice.erp.services.impl.common.HeaderToken;
import com.microservice.erp.services.impl.deferment.CreateDefermentService;
import com.microservice.erp.webapp.config.TestJPAH2Config;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TestJPAH2Config.class})
public class IDefermentInfoServiceUnitTest {


    @Mock
    IDefermentInfoRepository repository;

    @Mock
    DefermentMapper mapper;

    @InjectMocks
    CreateDefermentService service;

    HeaderToken headerToken;

    private Validator validator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void happyPathTest() {
        Mockito.mock(DefermentMapper.class);
        ICreateDefermentService.CreateDefermentCommand createCommand = createCommand();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testingtoken");
        //DefermentInfo d=mapper.mapToEntity(request, createCommand);
        //ResponseEntity<?> response = service.saveDeferment(request, createCommand);
//        given(mapper.mapToEntity(request,createCommand)).willReturn(DefermentInfo.builder().build());
//        verify(mapper).mapToEntity(request,createCommand);
//        then(mapper).should().mapToEntity(request, createCommand);//verify whether method is called in mock component
//        verify(mapper,never()).mapToEntity(request,createCommand);

        // assertNotNull(response);
        //assertNotNull(response.getStatusCode());
        //assertEquals(200, response.getStatusCodeValue());
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
