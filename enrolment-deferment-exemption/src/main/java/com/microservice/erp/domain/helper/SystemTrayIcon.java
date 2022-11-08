package com.microservice.erp.domain.helper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class SystemTrayIcon {
    public static void displayTray(String message) {

        final String IMAGE_PATH = "/resources/images/logo.png";

        if (!SystemTray.isSupported()) {
            System.out.println("System Tray not supported");
            return;
        }

        SystemTray tray = SystemTray.getSystemTray();
//        TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(IMAGE_PATH), "DCA");
        TrayIcon trayIcon = null;
        try {
            trayIcon = new TrayIcon(ImageIO.read(new URL("http://localhost:8080/lis/resources/images/logo.png")), "DCA");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger logger = Logger.getLogger("SystemTrayIcon");
        logger.info("Image path first");
        logger.info("Image path " + Toolkit.getDefaultToolkit().getImage(IMAGE_PATH));
        try {
            assert trayIcon != null;
            trayIcon.setImageAutoSize(Boolean.TRUE);
            tray.add(trayIcon);
        } catch (AWTException e) {
            logger.info("ERROR " + e);
        }
        trayIcon.displayMessage("TTPL Website", message, TrayIcon.MessageType.INFO);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tray.remove(trayIcon);
    }
}