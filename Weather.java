public class Weather {
	
	private int locationCode;
	private String date;
	private String time;
	private double PTY;
	private double REH;
    private double RN1;
    private double T1H;
    private double UUU;
    private double VVV;
    private double VEC;
    private double WSD;
    
    public int getLocationCode() {
    	return locationCode;
    }
    public void setLocationCode(int locationCode) {
    	this.locationCode = locationCode;
    }
    
    public String getDate() {
    	return date;
    }
    public void setDate(String date) {
    	this.date = date;
    }
    
    public String getTime() {
    	return time;
    }
    public void setTime(String time) {
    	this.time = time;
    }
    
    // 강수형태
    public double getPTY() {
    	return PTY;
    }
    public void setPTY(double PTY) {
    	this.PTY = PTY;
    }
    
    // 습도
    public double getREH() {
    	return REH;
    }
    public void setREH(double REH) {
    	this.REH = REH;
    }
    
    // 1시간 강수량
    public double getRN1() {
    	return RN1;
    }
    public void setRN1(double RN1) {
    	this.RN1 = RN1;
    }
    
    // 기온
    public double getT1H() {
    	return T1H;
    }
    public void setT1H(double T1H) {
    	this.T1H = T1H;
    }
    
    // 동서바람성분
    public double getUUU() {
    	return UUU;
    }
    public void setUUU(double UUU) {
    	this.UUU = UUU;
    }
    
    // 남북바람성분
    public double getVVV() {
    	return VVV;
    }
    public void setVVV(double VVV) {
    	this.VVV = VVV;
    }
    
    // 풍향
    public double getVEC() {
    	return VEC;
    }
    public void setVEC(double VEC) {
    	this.VEC = VEC;
    }
    
    // 풍속
    public double getWSD() {
    	return WSD;
    }
    public void setWSD(double WSD) {
    	this.WSD = WSD;
    }
    
    @Override
    public String toString() {
    	return "locationCode = " + locationCode +
    			"\ndate = " + date +
    			"\ntime" + time +
                "\nPTY = " + PTY +
                "\nREH = " + REH +
                "\nRN1 = " + RN1 +
                "\nT1H = " + T1H +
                "\nUUU = " + UUU +
                "\nVEC = " + VEC +
                "\nVVV = " + VVV +
                "\nWSD = " + WSD;
    }

}