import org.jwat.warc.WarcWriter;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by l_ckha on 14-09-2016.
 */
public class Service {

    private DateFormat dateFormat;

    public Service() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public Date parseDate(String dir, String pageName) {
        Date date = null;
        String dateStr = "";

        try {
            dateStr = toWarcDateFormat(dir, pageName);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) { /* Ignore */ }
        return date;
    }

    //returns the date and time for the creation of the warc file, in the right format
    public String warcInfoDate() {
        //I thought the warc info date should be from when the first of the records where taken but it seems to be a date for when the warc file is created
//        String infoDate = "";
//        File dirPath = new File(dir);
//        File[] filestest = dirPath.listFiles();
//        for (File f : filestest) {
//            String[] infoDateParts = infoDate.split("[\\D]");
//            String[] fParts = f.getName().split("[\\D]");
//            if (infoDate.equals("")) {
//                infoDate = f.getName();
//            } else if (Integer.parseInt(fParts[0]) <= Integer.parseInt(infoDateParts[0]) && //find the lowest date and time for the warcinfo
//                    Integer.parseInt(fParts[1]) <= Integer.parseInt(infoDateParts[1]) &&
//                    Integer.parseInt(fParts[2]) <= Integer.parseInt(infoDateParts[2]) &&
//                    Integer.parseInt(fParts[3]) <= Integer.parseInt(infoDateParts[3])) {
//                infoDate = f.getName();
//            }
//        }
//
//        infoDate = toWarcDateFormat(dir+"\\"+infoDate);
//        return infoDate;
        return dateFormat.format(new Date());
    }

    //removes the date from the complete path and returns a String with a path that can be used as an URI
    public String makeUriFromPath(String path, String pageName) {
        String newString = path.substring(path.indexOf(pageName), path.indexOf(pageName) + pageName.length()) + path.substring(path.indexOf(pageName) + pageName.length()+16);
        newString = newString.replace("\\", "/");

        //Removes the index.html from urls because it messed with some links, removing it messed with other links though,
        // and it seems like it should be the wayback machine taking care of this part.
//        if (newString.substring(newString.lastIndexOf("/") + 1).equals("index.html")) {
//            newString = newString.substring(0, newString.lastIndexOf("/") + 1);
//        }

        //Reverse the changes i made to directory names because of windows conflicts, things might need to be added if some files have other characters windows ins unhappy about
        newString = newString.replace("__", "?").replace("--", ":").replace("/AUX1", "/AUX").replace("@@@","...");

        //Adds http:// to the start of the url if it is not already present
        if(!newString.substring(0, "http://".length()).equals("http://")){
            newString = "http://" + newString;
        }
        return newString;
    }

    //take the time from the dir path and returns a String with the date in the right format for warc
    public String toWarcDateFormat(String dir, String pageName) {
        String dateStr = "";
        String dateStrToParse = "";
        try {
            File filePath = new File(dir);
            while (filePath.getParent() != null && !(filePath.getParentFile()).getName().equals(pageName)) {
                filePath = filePath.getParentFile();
            }
            dateStr = filePath.getName();

            // turn the input string into a string that can be parsed to date with the given date format
            dateStrToParse = dateStr.substring(0, 10) + "T" + dateStr.substring(11, 13) + ":" + dateStr.substring(13, 15) + ":00Z";
        } catch (Exception e) { /* Ignore */ }
        return dateStrToParse;
    }

    private int toDelete(List<Byte> outputbytes) {
        int linefeeds = 0;
        int toDelete = 0;
        for (byte b : outputbytes) {
            if (linefeeds != 2) {
                if (b == '\n') {
                    linefeeds++;
                    toDelete++;
                } else {
                    toDelete++;
                    linefeeds = 0;
                }
            }
        }
        return toDelete;
    }

    public byte[] arrayListToArray(ArrayList<Byte> list) {
        byte[] output = new byte[list.size()];

        for (int i = 0; i < list.size(); i++) {
            output[i] = list.get(i);
        }
        return output;
    }

    public ArrayList<Byte> getHtppHeader(File srcFile) {
        ArrayList<Byte> httpHeader = new ArrayList<>();

        try (FileInputStream fInputStream = new FileInputStream(srcFile)) {

            //add the content from the inputstream to the outputbytes arraylist
            int content;
            while ((content = fInputStream.read()) != -1) {
                httpHeader.add((byte) content);
            }

            int counter = toDelete(httpHeader);
            int fileLenght = httpHeader.size();

            for (int i = counter; i < fileLenght; i++) {
                httpHeader.remove(counter);
            }

            int replaceAt = 0;
            for (Byte b : "HTTP/1.1".getBytes()) {
                httpHeader.set(replaceAt, b);
                replaceAt++;
            }

            httpHeader.add(8, (byte) ' ');

//            for (Byte b: httpHeader
//                 ) {
//                System.out.println((char) (byte) b);
//            }
            fInputStream.close();
            return httpHeader;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //TODO der var adgang næsgtet på sbArc2WarcTest mappen, ved ikke hvorfor(det er der ikke længere, aner ikke hvad der har ændret sig)?!?
    public ArrayList<Byte> getPayload(File srcFile) {
        ArrayList<Byte> payload = new ArrayList<>();

        try (FileInputStream fInputStream = new FileInputStream(srcFile)) {


            //add the content from the inputstream to the outputbytes arraylist
            int content;
            while ((content = fInputStream.read()) != -1) {
                payload.add((byte) content);
            }

            int counter = toDelete(payload);

            for (int i = 0; i < counter; i++) {
                payload.remove(0);
            }

            payload.add((byte) '\r');
            payload.add((byte) '\n');
            payload.add((byte) '\r');
            payload.add((byte) '\n');

            fInputStream.close();
            return payload;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String dir = "C:\\Users\\ckha\\Desktop\\sb_pack\\noget\\unpackedsoeg.sol\\soeg.sol.dk\\2001-12-03.1601\\img\\kv_logo.gif"; //path to the chosen directory

        Service d = new Service();
//        System.out.println(d.toWarcDateFormat(dir));
//        System.out.println(d.parseDate(dir));
        System.out.println(d.warcInfoDate());

        System.out.println();
    }


}
