package com.microservice.erp.domain.helper;


import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


import com.jcraft.jsch.*;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class FileUploadToExternalLocation {
    public static ResponseMessage fileUploader(MultipartFile attachedFile, String fileName, String propertiesFileName,
                                               HttpServletRequest request) throws IOException {
        ResponseMessage responseMessage = new ResponseMessage();
        String uploadFileName = fileName;
        if (attachedFile == null) {
            responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_UNSUCCESSFUL.value());
            responseMessage.setText("Please select the file.");
            return responseMessage;
        }

        String originalFileName = attachedFile.getOriginalFilename();
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName.length()).toUpperCase();

        FileUploadDTO fileUploadDTO = fileUploadPathRetriever(request);
        String uploadedDirectory = fileUploadDTO.getUploadFilePath();

        File uploadDirectoryName = new File(uploadedDirectory);
        uploadDirectoryName.mkdirs();

        if (fileName.equals("")) {
            uploadFileName = originalFileName;
        }

        File uploadFile = new File(uploadedDirectory.concat(uploadFileName));

        //check if the directory exists
        if (!uploadDirectoryName.isDirectory()) {
            responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_UNSUCCESSFUL.value());
            responseMessage.setText("The given file location path " + uploadedDirectory + " does not exist.");
            return responseMessage;
        }
        //check if the file exist in the directory
        if (uploadFile.exists()) {
            responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_UNSUCCESSFUL.value());
            responseMessage.setText("The file name " + fileName + " already exist. Please change the file name and try again.");
            return responseMessage;
        }
        //check if the directory has write access
        if (!uploadDirectoryName.canWrite()) {
            responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_UNSUCCESSFUL.value());
            responseMessage.setText("The given file location path " + uploadedDirectory + " does not have the write permission.");
            return responseMessage;
        }

        FileOutputStream fileOutputStream = new FileOutputStream(uploadFile);
        fileOutputStream.write(attachedFile.getBytes());
        fileOutputStream.close();

        responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_SUCCESSFUL.value());
        return responseMessage;
    }

    public static FileUploadDTO fileUploadPathRetriever(HttpServletRequest request) throws IOException {
        Resource resource = new ClassPathResource("/fileConfig/fileUpload.properties");
        Properties props = PropertiesLoaderUtils.loadProperties(resource);
        String url = props.getProperty("fileupload.fileUploadPath");
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);
        Integer currentMonth = calendar.get(Calendar.MONTH) + 1;
        String monthName = new SimpleDateFormat("MMM").format(calendar.getTime());
        Integer currentDay = calendar.get(Calendar.DATE);
        String day = currentDay.toString().length() == 1 ? "0" + currentDay.toString() : currentDay.toString();
        String month = currentMonth.toString().length() == 1 ? "0" + currentMonth.toString() : currentMonth.toString();

        String year = currentYear.toString();
        String folderName = "/gyalsung/" + year + "/" + monthName + "/" + day + "/";
         FileUploadDTO fileUploadDTO = new FileUploadDTO();
        String fileUploadPath = url.concat(folderName);
        fileUploadDTO.setUploadFilePath(fileUploadPath);
        return fileUploadDTO;
    }

    public static ResponseMessage checkIfDownloadPathIsValid(ResponseMessage responseMessage, String uploadFilePath)
            throws IOException {
        String uploadedDirectory = uploadFilePath.substring(0, uploadFilePath.lastIndexOf("/") + 1);
        File uploadDirectoryName = new File(uploadedDirectory);
        File uploadFile = new File(uploadFilePath);

        //check if the directory exists
        if (!uploadDirectoryName.isDirectory()) {
            responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_UNSUCCESSFUL.value());
            responseMessage.setText("The given file location path " + uploadedDirectory + " does not exist.");
            return responseMessage;
        }
        //check if the file exist in the directory
        if (!uploadFile.exists()) {
            responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_UNSUCCESSFUL.value());
            responseMessage.setText("The file does not exist.");
            return responseMessage;
        }
        //check if the directory has read access
        if (!uploadDirectoryName.canRead()) {
            responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_UNSUCCESSFUL.value());
            responseMessage.setText("The given file location path " + uploadedDirectory + " does not have the read permission.");
            return responseMessage;// 4000
        }

        responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_SUCCESSFUL.value());
        return responseMessage;
    }

    /**
     * This method is to convert the file into byte and download the file
     */
    public static ResponseMessage fileDownloader(String fileName, String uploadFilePath, HttpServletResponse response)
            throws IOException {
        ResponseMessage responseMessage = new ResponseMessage();
        String fileExtension = fileName.substring(fileName.indexOf('.') + 1);
        byte[] bytes = convertFileToByte(uploadFilePath);
        if (fileExtension.equals("xlsx")) {
            response.setContentType("application/xlsx");
        }
        if (fileExtension.equals("xls")) {
            response.setContentType("application/xls");

        }
        if (fileExtension.equals("docx")) {
            response.setContentType("application/docx");
        }
        if (fileExtension.equals("doc")) {
            response.setContentType("application/doc");
        }
        if (fileExtension.equals("pdf")) {
            response.setContentType("application/pdf");
        }
        if (fileExtension.equals("jpg")) {
            response.setContentType("image/jpg");
        }
        if (fileExtension.equals("jpeg")) {
            response.setContentType("image/jpeg");
        }

        if (fileExtension.equals("png")) {
            response.setContentType("image/png");
        }

        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentLength(bytes.length);
        FileCopyUtils.copy(bytes, response.getOutputStream());
        responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_SUCCESSFUL.value());
        return responseMessage;
    }

    public static ResponseMessage viewFile(String fileName, String uploadFilePath, HttpServletResponse response)
            throws IOException {
        ResponseMessage responseMessage = new ResponseMessage();
        String fileExtension = fileName.substring(fileName.indexOf('.') + 1);
        byte[] bytes = convertFileToByte(uploadFilePath);
        if (fileExtension.equals("pdf")) {
            response.setContentType("application/pdf");
        }
        if (fileExtension.equals("jpg")) {
            response.setContentType("image/jpg");
        }
        if (fileExtension.equals("jpeg")) {
            response.setContentType("image/jpeg");
        }
        if (fileExtension.equals("png")) {
            response.setContentType("image/png");
        }
        response.setHeader("Content-Disposition;inline", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        response.setContentLength(bytes.length);
        FileCopyUtils.copy(bytes, response.getOutputStream());
        responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_SUCCESSFUL.value());
        return responseMessage;
    }

    /**
     * to convert file into array of byte
     */

    public static byte[] convertFileToByte(String fileUploadPath) throws IOException {
        File file = new File(fileUploadPath);
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bufferedFile = new byte[1024];
        for (int readNumber; (readNumber = fileInputStream.read(bufferedFile)) != -1; ) {
            byteArrayOutputStream.write(bufferedFile, 0, readNumber);
        }
        return byteArrayOutputStream.toByteArray();
    }


    public static ResponseMessage fileUploaderRemote(MultipartFile attachedFile, String fileName, String propertiesFileName,
                                                       HttpServletRequest request) throws IOException, JSchException, SftpException {
        ResponseMessage responseMessage = new ResponseMessage();

        String uploadFileName = fileName;
        if (attachedFile == null) {
            responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_UNSUCCESSFUL.value());
            responseMessage.setText("Please select the file.");
            return responseMessage;
        }

        String originalFileName = attachedFile.getOriginalFilename();

        FileUploadDTO fileUploadDTO = fileUploadPathRetrieverRemote(request);
        String uploadedDirectory = fileUploadDTO.getUploadFilePath();
        String username = "sysadmin";
        String password = "Sys@2023";
        String server = "172.30.84.147";
        int port = 22;
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, server, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no"); // Skip host key checking (can be dangerous in production)
            session.connect();

            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();

            // Change to the desired upload directory on the server
            channelSftp.cd(uploadedDirectory);

            // Upload the file to the server
            try (InputStream inputStream = attachedFile.getInputStream()) {
                channelSftp.put(inputStream, uploadFileName);
            }

            channelSftp.disconnect();
            session.disconnect();

            responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_SUCCESSFUL.value());
            responseMessage.setText("File uploaded successfully.");
        } catch (JSchException | SftpException | IOException e) {
            responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_UNSUCCESSFUL.value());
            responseMessage.setText("Failed to upload file: " + e.getMessage());
        }
        responseMessage.setStatus(SystemDataInt.MESSAGE_STATUS_SUCCESSFUL.value());
        return responseMessage;
    }


    public static FileUploadDTO fileUploadPathRetrieverRemote(HttpServletRequest request) throws IOException, SftpException, JSchException {
        int port = 22;
        String username = "sysadmin";
        String password = "Sys@2023";
        String serverAddress = "172.30.84.147"; // Replace with the server address
        Calendar calendar = Calendar.getInstance();
        Integer currentYear = calendar.get(Calendar.YEAR);
        Integer currentMonth = calendar.get(Calendar.MONTH) + 1;
        String monthName = new SimpleDateFormat("MMM").format(calendar.getTime());
        Integer currentDay = calendar.get(Calendar.DATE);
        String day = currentDay.toString().length() == 1 ? "0" + currentDay.toString() : currentDay.toString();
        String year = currentYear.toString();
        String remoteBaseDirectory = "opt/gyalsungDocument/profilePicture/" + year + "/" + monthName + "/" + day + "/";
        String encodedPassword;
        try {
            // Initialize the VFS manager
            StandardFileSystemManager manager = new StandardFileSystemManager();
            manager.init();

            // Create the options for the connection
            FileSystemOptions opts = new FileSystemOptions();
            SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
            encodedPassword = URLEncoder.encode(password, "UTF-8");

            // Create the remote URL for the directory
            String remoteUrl = "sftp://" + username + ":" + encodedPassword + "@" + serverAddress + "/" + remoteBaseDirectory;

            // Create the remote folder
            FileObject remoteFolder = manager.resolveFile(remoteUrl, opts);
            if (!remoteFolder.exists()) {
                remoteFolder.createFolder();
                System.out.println("Remote directory created successfully.");
            } else {
                System.out.println("Remote directory already exists.");
            }

            // Close the manager
            manager.close();
        } catch (FileSystemException e) {
            e.printStackTrace();
        }

        FileUploadDTO fileUploadDTO = new FileUploadDTO();
        fileUploadDTO.setUploadFilePath(remoteBaseDirectory);
        return fileUploadDTO;
    }

}