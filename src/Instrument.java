public class Instrument {

    private static String serial;
    private static String instrumentType;
    private static String instrumentNum;
    private static String model;
    private static String make;
    private static String dateOut;
    private static String timeOut;

    public Instrument(String serial, String instrumentType, String instrumentNum, String model, String make){
        this.serial= serial;
        this.instrumentType = instrumentType;
        this.instrumentNum = instrumentNum;
        this.model = model;
        this.make = make;
    }

    public void setDateOut(String dateOut) {
        this.dateOut = dateOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getSerial (){
        return serial;
    }

    public String getInstrumentType(){
        return instrumentType;
    }

    public String dgetInstrumentNum(){
        return instrumentNum;
    }

    public String getModel(){
        return model;
    }

    public String getMake(){
        return make;
    }

    public String getDateOut(){
        return dateOut;
    }

    public String getTimeOut(){
        return timeOut;
    }
}
