//import java.io.File;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
///**
// * Created by l_ckha on 06-09-2016.
// */
//public class WarcRecord {
//    private WarcRecordType warcType;
//    private Date date; //need to find the right date format (YYYY-MM-DDThh:mm:ssZ)
//    private UUID recordId;
//    private int contentLenght;
//    private String contentType;
//    private String concurrentTo;
//    private String blockDigest;
//    private String payloadDigest;
//    private String ipAdress;
//    private String warcRefersTo;
//    private String warcTargetUri;
//    private String truncated;
//    private UUID warcinfoId;
//    private String filename;  //warcinfo only
//    private String profile;  //revisit only
//    private String identifiedPayloadType;
//    private String segmentOriginId;  //continuation only
//    private int segmentNumber;  //continuation
//    private int segmentTotalLenght;  //continuation
//    private List<Byte> contentBlock = new ArrayList<>();
//    private Service service;
//
//    public WarcRecord(WarcRecordType warcType) {
//        this.warcType = warcType;
//        if (warcType == warcType.WARCINFO) {
//            warcinfoId = UUID.randomUUID();
//        }
//        recordId = UUID.randomUUID();
//        service = new Service();
//        //date =
//        //recordId =
//        //contentLength =
//
//
//    }
//
//    public WarcRecordType getWarcType() {
//        return warcType;
//    }
//
//    public void setWarcType(WarcRecordType warcType) {
//        this.warcType = warcType;
//    }
//
//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }
//
//    public UUID getRecordId() {
//        return recordId;
//    }
//
//    public void setRecordId(UUID recordId) {
//        this.recordId = recordId;
//    }
//
//    public int getContentLenght() {
//        return contentLenght;
//    }
//
//    public void setContentLenght(int contentLenght) {
//        this.contentLenght = contentLenght;
//    }
//
//    public String getContentType() {
//        return contentType;
//    }
//
//    public void setContentType(String contentType) {
//        this.contentType = contentType;
//    }
//
//    public String getConcurrentTo() {
//        return concurrentTo;
//    }
//
//    public void setConcurrentTo(String concurrentTo) {
//        this.concurrentTo = concurrentTo;
//    }
//
//    public String getBlockDigest() {
//        return blockDigest;
//    }
//
//    public void setBlockDigest(String blockDigest) {
//        this.blockDigest = blockDigest;
//    }
//
//    public String getPayloadDigest() {
//        return payloadDigest;
//    }
//
//    public void setPayloadDigest(String payloadDigest) {
//        this.payloadDigest = payloadDigest;
//    }
//
//    public String getIpAdress() {
//        return ipAdress;
//    }
//
//    public void setIpAdress(String ipAdress) {
//        this.ipAdress = ipAdress;
//    }
//
//    public String getWarcRefersTo() {
//        return warcRefersTo;
//    }
//
//    public void setWarcRefersTo(String warcRefersTo) {
//        this.warcRefersTo = warcRefersTo;
//    }
//
//    public String getWarcTargetUri() {
//        return warcTargetUri;
//    }
//
//    //Takes a String path and a String pageName and sets the property warcTargetUri
//    public void setWarcTargetUri(String path, String pageName) {
//        warcTargetUri = service.makeUriFromPath(path, pageName);
//    }
//
//    public String getTruncated() {
//        return truncated;
//    }
//
//    public void setTruncated(String truncated) {
//        this.truncated = truncated;
//    }
//
//    public UUID getWarcinfoId() {
//        return warcinfoId;
//    }
//
//    public void setWarcinfoId(UUID warcinfoId) {
//        this.warcinfoId = warcinfoId;
//    }
//
//    public String getFilename() {
//        return filename;
//    }
//
//    public void setFilename(String filename) {
//        this.filename = filename;
//    }
//
//    public String getProfile() {
//        return profile;
//    }
//
//    public void setProfile(String profile) {
//        this.profile = profile;
//    }
//
//    public String getIdentifiedPayloadType() {
//        return identifiedPayloadType;
//    }
//
//    public void setIdentifiedPayloadType(String identifiedPayloadType) {
//        this.identifiedPayloadType = identifiedPayloadType;
//    }
//
//    public String getSegmentOriginId() {
//        return segmentOriginId;
//    }
//
//    public void setSegmentOriginId(String segmentOriginId) {
//        this.segmentOriginId = segmentOriginId;
//    }
//
//    public int getSegmentNumber() {
//        return segmentNumber;
//    }
//
//    public void setSegmentNumber(int segmentNumber) {
//        this.segmentNumber = segmentNumber;
//    }
//
//    public int getSegmentTotalLenght() {
//        return segmentTotalLenght;
//    }
//
//    public void setSegmentTotalLenght(int segmentTotalLenght) {
//        this.segmentTotalLenght = segmentTotalLenght;
//    }
//
//    public List<Byte> getContentBlock() {
//        return new ArrayList<>(contentBlock);
//    }
//
//    public void setContentBlock(List<Byte> contentBlock) {
//        this.contentBlock = contentBlock;
//    }
//
//    public String contentBlockToString() {
//        if (!contentBlock.isEmpty()) {
//            contentBlock.add(0, (byte) '\n'); //TODO need to add the \n to the start of contentblock somewhere, maybe not here. don't add if the block is empty though
//        } //TODO doing it like this is a problem for response record atm
//        String returnString = "";
//        for (Byte b : contentBlock) {
//            returnString += (char) (byte) b;
//        }
//        return returnString;
//    }
//
//    //take a String path and returns a String with the right payload type
//    private String checkPayloadType(String path){
//        String payloadType = "";
//        if(new File(path).getName().endsWith(".gif")){ //TODO need to make it for all payload types
//            payloadType = "image/gif";
//        } else if(new File(path).getName().endsWith(".jpg")){
//            payloadType = "image/jpg";
//        }
//        return payloadType;
//    }
//
//    //Returns a String that represents a warc info record
//    public String warcInfoRecord() { //TODO should have both header and content for the different warc records
//        return "WARC/1.0\n" +
//                "WARC-Type: warcinfo\n" +
//                "WARC-Record-ID: <urn:uuid:" + warcinfoId + ">\n" +
//                "WARC-Date: " + service.warcInfoDate() + "\n" +
//                "Content-Length: " + "0\n" +
//                "Content-Type: application/warc-fields\n" +
//                contentBlockToString() +
//                "\n" +
//                "\n" +
//                "\n";
//    }
//
//    public String warcResponseRecord(UUID infoId, String path, int contentLength, String pageName) {
//        setWarcTargetUri(path, pageName); //TODO not sure if the warcTargetUri should be made here
//        String payloadType = checkPayloadType(path);
//        return "WARC/1.0\n" +
//                "WARC-Type: " + "response\n" +
//                "WARC-Target-URI: http://" + warcTargetUri + "\n" +
//                "WARC-Warcinfo-ID: <urn:uuid:" + infoId + ">\n" +
//                "WARC-Date: " + service.toWarcDateFormat(path, "soeg.sol.dk") + "\n" +
//                "WARC-IP-Address: " + "0.0.0.0\n" + //TODO if there is an ip adress in some of the sb_arc files this should be changed
//                "WARC-Record-ID: " + "<urn:uuid:" + recordId + ">\n" +
//                "Content-Type: " + "application/http;msgtype=response\n" +
//                "WARC-Identified-Payload-Type: " + payloadType+"\n" +
//                "Content-Length: " + contentLength + "\n" + //TODO der er \r\n\r\n før content imellem header og content block der ikke bliver talt med, ikke sikker på hvor den skal tælle fra, den siger det passer hvis jeg adder 1 til content lenght men kan ikke se hvorfor
//                "\n";
//    }
//}
