package util;

import com.digi.xbee.api.DigiMeshDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.XBeeNetwork;
import com.digi.xbee.api.exceptions.XBeeException;
import listeners.BCDiscoveryListener;
import ui.Menu;

import javax.swing.*;
import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Constants {

    // Default values until properties file is read and constants are established.
    // TODO: 5/15/20 Do this with Properties.default methods, not manually

    private static String port = "/dev/ttyUSB0";
    private static int baud_Rate = 9600;
    private static int discovery_timeout = 10;


    /**
     * Convenience method for getting a time stamp in the system time zone, such as for prefixing a report to the debug
     * console during runtime. The value in parenthesis is the time since startup.
     * @return a string containing the time since program startup, the local date, and local time (ISO), with a
     * colon and a space appended to the end.
     * @see LocalDateTime
     * @see Runtime
     */
    public static String timestamp(){
        // FIXME: 5/22/20 finish javadoc comment

        LocalDateTime localDateTime = LocalDateTime.now();

        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();

        return "(" + uptime + ")" + // add the uptime
                localDateTime.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE) + " " +
                localDateTime.toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME) + ": ";
    }


    static {
        try {
            // Load the properties file.
            Properties properties = new Properties();
            properties.load(new FileInputStream("util/configuration.properties"));

            // Replace the default values with the values located in the properties file
            port = properties.getProperty("port");
            baud_Rate = Integer.parseInt(properties.getProperty("baud_rate"));
            discovery_timeout = Integer.parseInt(properties.getProperty("discovery_timeout"));


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to read configuration file. \n" +
                            "Ensure that \"configuration.properties\" is present in the same directory (folder) as " +
                            "\"BeeChatNetwork.jar\", and is properly formatted." +
                            "\nProceeding with default values:" +
                            "\nPort: " + port +
                            "\nBaud rate: " + baud_Rate +
                            "\nDiscovery timeout: " + discovery_timeout +
                            "\n\nError message: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);

            System.err.println(timestamp() + e.getMessage());
            // Execution can usually continue normally using default values
        }
    }


    /**
     * Port where the XBee device is located
     *
     */
    public static final String PORT = port;


    /**
     * Baud rate to be used for both input and output.
     *
     */
    public static final int BAUD_RATE = baud_Rate;

    /**
     * Discovery timeout. Determines how long the network discovery process should last.
     * @see BCDiscoveryListener
     */
    public static final int DISCOVERY_TIMEOUT = discovery_timeout;



    public static XBeeDevice device;

    static {
        device = new DigiMeshDevice(PORT, BAUD_RATE);
        try {
            device.open();

        } catch (XBeeException e) {

            JOptionPane.showMessageDialog(null, "An exception has occurred while opening " +
                            "connection interface with local device.\n" + e.getMessage() + "\nDo you have permission" +
                    " to access " + port + "?\nIs your module connected?\nIs your module properly configured?",
                    "Error", JOptionPane.WARNING_MESSAGE);

            e.printStackTrace();
            System.exit(1);
        }
    }

    public static XBeeNetwork network = device.getNetwork();

    public static Menu menu = new Menu();

}
