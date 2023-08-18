package com.microservice.erp.services.impl;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.microservice.erp.domain.dto.ApplicationProperties;
import com.microservice.erp.domain.dto.DefermentDto;
import com.microservice.erp.domain.dto.DefermentListDto;
import com.microservice.erp.domain.dto.UserProfileDto;
import com.microservice.erp.domain.entities.DefermentInfo;
import com.microservice.erp.domain.helper.ApprovalStatus;
import com.microservice.erp.domain.mapper.DefermentMapper;
import com.microservice.erp.domain.repositories.IDefermentInfoAuditRepository;
import com.microservice.erp.domain.repositories.IDefermentInfoRepository;
import com.microservice.erp.services.iServices.IReadDefermentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReadDefermentService implements IReadDefermentService {
    private final IDefermentInfoRepository repository;
    private final IDefermentInfoAuditRepository auditRepository;
    private final DefermentMapper mapper;
    private final DefermentExemptionValidation defermentExemptionValidation;
    private final UserInformationService userInformationService;
    private final HeaderToken headerToken;

    @Autowired
    @Qualifier("trainingManagementTemplate")
    RestTemplate restTemplate;

    @Autowired
    @Qualifier("userProfileTemplate")
    RestTemplate userRestTemplate;

    @Override
    public List<DefermentDto> getAllDefermentList(String authHeader) {
        //To get deferment list
        List<DefermentDto> defermentDtoList = repository.findAll()
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

        List<BigInteger> userIdsVal = defermentDtoList
                .stream()
                .map(DefermentDto::getUserId)
                .collect(Collectors.toList());


        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal, authHeader);


        //Merge deferment and user information
        defermentDtoList.forEach(defermentDto -> {
            UserProfileDto userProfileDto = userProfileDtos
                    .stream()
                    .filter(user -> defermentDto.getUserId().equals(user.getId()))
                    .findAny()
                    .orElse(null);
            defermentDto.setFullName(Objects.requireNonNull(userProfileDto).getFullName());
            defermentDto.setCid(Objects.requireNonNull(userProfileDto).getCid());
            defermentDto.setDob(Objects.requireNonNull(userProfileDto).getDob());
            defermentDto.setGender(Objects.requireNonNull(userProfileDto).getGender());
        });

        return defermentDtoList;

    }


    @Override
    public List<DefermentListDto> getDefermentListByDefermentYearReasonStatus(String authHeader, String defermentYear,
                                                                              BigInteger reasonId, Character status,
                                                                              Character gender, String cid, String caseNumber) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationProperties.class);
        ApplicationProperties properties = context.getBean(ApplicationProperties.class);

        HttpEntity<String> httpRequest = headerToken.tokenHeader(authHeader);


        defermentYear = defermentYear.isEmpty() ? null : defermentYear;
        cid = cid.isEmpty() ? null : cid;
        caseNumber = caseNumber.isEmpty() ? null : caseNumber;

        List<UserProfileDto> userProfileDtos;

        List<DefermentDto> defermentDtoList = repository.getDefermentListByToDateStatus(defermentYear, status, gender, reasonId, caseNumber)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());

        List<BigInteger> userIdsVal;
        List<DefermentListDto> defermentDtos = new ArrayList<>();

        if (!Objects.isNull(cid)) {
            userProfileDtos = userInformationService.getUserInformationByPartialCid(cid, authHeader);
        } else {
            userIdsVal = defermentDtoList
                    .stream()
                    .map(DefermentDto::getUserId)
                    .collect(Collectors.toList());
            userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal, authHeader);

        }
        userProfileDtos.forEach(item -> {
            DefermentDto defermentDto = defermentDtoList.stream()
                    .filter(deferment -> item.getUserId().equals(deferment.getUserId()))
                    .max(Comparator.comparing(DefermentDto::getId))
                    .orElse(null);
            List<DefermentDto> defermentList = repository.findAllByUserIdOrderByDefermentIdDesc(item.getId())
                    .stream()
                    .map(mapper::mapToDomain)
                    .collect(Collectors.toUnmodifiableList());
            DefermentListDto defermentData = new DefermentListDto();
            if (!Objects.isNull(defermentDto)) {


                defermentData.setId(defermentDto.getId());
                defermentData.setRemarks(defermentDto.getRemarks());
                defermentData.setApprovalRemarks(defermentDto.getApprovalRemarks());
                defermentData.setDefermentYear(defermentDto.getDefermentYear());
                defermentData.setStatus(defermentDto.getStatus());
                defermentData.setFullName(Objects.requireNonNull(item).getFullName());
                defermentData.setCid(Objects.requireNonNull(item).getCid());
                defermentData.setDob(Objects.requireNonNull(item).getDob());
                defermentData.setGender(Objects.requireNonNull(item).getGender());
                defermentData.setDefermentFileDtos(defermentDto.getDefermentFileDtos());
                defermentData.setReasonId(defermentDto.getReasonId());
                defermentData.setApplicationDate(defermentDto.getApplicationDate());
                defermentData.setUserId(defermentDto.getUserId());
                defermentData.setCaseNumber(defermentDto.getCaseNumber());
                defermentData.setMailStatus(defermentDto.getMailStatus());
                defermentData.setDefermentList(defermentList);
                if(!Objects.isNull(defermentDto.getReviewerId())){
                    String userUrl = properties.getUserProfileById() + defermentDto.getReviewerId();
                    ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);
                    defermentData.setReviewerFullName(Objects.requireNonNull(Objects.requireNonNull(userResponse).getBody()).getFullName());
                }

                if(!Objects.isNull(defermentDto.getApproverId())){
                    String userUrl = properties.getUserProfileById() + defermentDto.getApproverId();
                    ResponseEntity<UserProfileDto> userResponse = userRestTemplate.exchange(userUrl, HttpMethod.GET, httpRequest, UserProfileDto.class);
                    defermentData.setApproverFullName(Objects.requireNonNull(Objects.requireNonNull(userResponse).getBody()).getFullName());
                }

            }
            defermentDtos.add(defermentData);
        });
        return defermentDtos;
    }

    @Override
    public ResponseEntity<?> getDefermentByUserId(BigInteger userId) {
        DefermentInfo defermentInfo = Objects.isNull(repository.getDefermentByUserId(userId)) ? null :
                repository.getDefermentByUserId(userId);
        return ResponseEntity.ok(defermentInfo);
    }

    @Override
    public ResponseEntity<?> getDefermentValidation(BigInteger userId) {
        return defermentExemptionValidation
                .getDefermentAndExemptValidation(userId, 'D', "");
    }

    @Override
    public List<DefermentDto> getApprovedListByDefermentYearAndUserId(String authHeader, String defermentYear, BigInteger userId) {
        List<Character> statuses = Arrays.asList(ApprovalStatus.APPROVED.value(),
                ApprovalStatus.PENDING.value());

        return repository.findAllByDefermentYearAndUserIdAndStatusIn(defermentYear, userId, statuses)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public ResponseEntity<?> getDefermentListByUserId(BigInteger userId) {
        return ResponseEntity.ok(repository.findAllByUserIdOrderByDefermentIdDesc(userId)
                .stream()
                .map(mapper::mapToDomain)
                .collect(Collectors.toUnmodifiableList()));
    }

    @Override
    public ResponseEntity<?> downloadFile(String urls) throws IOException, SftpException, URISyntaxException {
//        String remoteFileUrl = "http://172.30.84.147/opt/gyalsungDocument/edeDocument/2023/Aug/09/alert_message.pdf";
//        String username = "sysadmin";
//        String password = "Sys@2023";
//        try {
//            URL url = new URL(remoteFileUrl);
//            URLConnection connection = url.openConnection();
//
//            InputStream inputStream = connection.getInputStream();
//            FileOutputStream outputStream = new FileOutputStream(urls);

//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//
//            System.out.println("File downloaded successfully.");
//
//            outputStream.close();
//            inputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("File download failed.");
//        }
        String server = "172.30.84.147";
        int port = 22;
        String username = "sysadmin";
        String password = "Sys@2023";
        String remoteFilePath = "/opt/gyalsungDocument/edeDocument/2023/Aug/11/alert_message.pdf";
        String localDirectory = "/path/to/local/directory";

        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, server, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no"); // Use with caution
            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            // List details about the remote file
            Vector<ChannelSftp.LsEntry> entries = channelSftp.ls("/opt/gyalsungDocument/edeDocument/2023/Aug/11/alert_message.pdf");
            if (entries.size() > 0) {
                ChannelSftp.LsEntry entry = entries.get(0);
                System.out.println("File Name: " + entry.getFilename());
                System.out.println("File Size: " + entry.getAttrs().getSize());
                System.out.println("File Permissions: " + entry.getAttrs().getPermissionsString());
                // Add more details as needed
            } else {
                System.out.println("Remote file not found.");
            }

//            JSch jsch = new JSch();
//            Session session = jsch.getSession(username, server, port);
//            session.setPassword(password);
//            session.setConfig("StrictHostKeyChecking", "no"); // Use with caution
//            session.connect();
//
//            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
//            channelSftp.connect();
//
//            // Get the file name from the remote file path
//            String fileName = remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);
//
//            // Create the local directory if it doesn't exist
//            File localDir = new File(localDirectory);
//            if (!localDir.exists()) {
//                localDir.mkdirs();
//            }
//
//            // Create the local file
//            File localFile = new File(localDir, fileName);
//            FileOutputStream outputStream = new FileOutputStream(localFile);
//
//            // Download the remote file
//            channelSftp.get("/opt/gyalsungDocument/edeDocument/2023/Aug/11/alert_message.pdf", "/path/to/local/directory/alert_message.pdf");

//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }

//            System.out.println("File fetched successfully.");
//
//            outputStream.close();
//            inputStream.close();
//            channelSftp.disconnect();
//            session.disconnect();
            channelSftp.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File fetch failed.");
        }
//        String server = "172.30.84.147";
//        String username = "sysadmin";
//        String password = "Sys@2023";
//        int port = 22;
//        String remoteFilePath = uri.getPath();
//
//        // Set up the authentication
//        try {
//            JSch jsch = new JSch();
//            Session session = jsch.getSession(username, server, port);
//            session.setPassword(password);
//            session.setConfig("StrictHostKeyChecking", "no"); // Skip host key checking (can be dangerous in production)
//            session.connect();
//
//
//
//            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
//            channelSftp.connect();
//            // Download the file
//            channelSftp.get(remoteFilePath, url);
//
//            channelSftp.disconnect();
//            session.disconnect();
//
//        }catch (JSchException  e) {
//            return ResponseEntity.notFound().build();
//        }

        return null;
//        File file = new File(url);
//        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
//
//        if (file.exists()) {
//            FileSystemResource resource = new FileSystemResource(file);
//            contentType = determineContentType(file.getName());
//            return ResponseEntity
//                    .ok()
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//                    .contentType(MediaType.parseMediaType(contentType))
//                    .body(resource);
//        } else {
//            // Handle file not found scenario
//            return ResponseEntity.notFound().build();
//        }.
    }

    @Override
    public List<DefermentDto> getDefermentAuditListByDefermentId(String authHeader, BigInteger defermentId) {
        List<DefermentDto> defermentDtoList = auditRepository.findAllByDefermentIdOrderByDefermentAuditIdDesc(defermentId)
                .stream()
                .map(mapper::mapToAuditDomain)
                .collect(Collectors.toUnmodifiableList());
        List<BigInteger> userIdsVal = defermentDtoList
                .stream()
                .map(DefermentDto::getUserId)
                .collect(Collectors.toList());
        List<UserProfileDto> userProfileDtos = userInformationService.getUserInformationByListOfIds(userIdsVal, authHeader);

        defermentDtoList.forEach(defermentDto -> {
            UserProfileDto userProfileDto = userProfileDtos.stream()
                    .filter(user -> defermentDto.getUserId().equals(user.getUserId()))
                    .max(Comparator.comparing(UserProfileDto::getId))
                    .orElse(null);
            if (!Objects.isNull(userProfileDto)) {
                defermentDto.setFullName(userProfileDto.getFullName());
                defermentDto.setCid(userProfileDto.getCid());
                defermentDto.setDob(userProfileDto.getDob());
                defermentDto.setGender(userProfileDto.getGender());
            }
        });

        return defermentDtoList;
    }

    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        switch (extension.toLowerCase()) {
            case "pdf":
                return "application/pdf";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            default:
                return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }


}
