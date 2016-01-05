package data;

import java.io.Serializable;


public class JmglDutyRecord implements Serializable{  
    /**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	private String uuid;
	private String name;
	private String notice;
	private String info;
	private boolean isEqpOk = true;
	private String eqpInfo;
	private int safeMenNum;
	private int safeCarNum;
	private String nextName;
	private String startTime;
	private String endTime;
    private int starter;
    private boolean isClean = true;
    private boolean isFinish = false;
    
    
  
    public JmglDutyRecord() {  
       
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the nextName
	 */
	public String getNextName() {
		return nextName;
	}

	/**
	 * @param nextName the nextName to set
	 */
	public void setNextName(String nextName) {
		this.nextName = nextName;
	}

	/**
	 * @return the info
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * @param info the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * @return the isEqpOk
	 */
	public boolean isEqpOk() {
		return isEqpOk;
	}

	/**
	 * @param isEqpOk the isEqpOk to set
	 */
	public void setEqpOk(boolean isEqpOk) {
		this.isEqpOk = isEqpOk;
	}

	/**
	 * @return the safeMenNum
	 */
	public int getSafeMenNum() {
		return safeMenNum;
	}

	/**
	 * @param safeMenNum the safeMenNum to set
	 */
	public void setSafeMenNum(int safeMenNum) {
		this.safeMenNum = safeMenNum;
	}

	/**
	 * @return the eqpInfo
	 */
	public String getEqpInfo() {
		return eqpInfo;
	}

	/**
	 * @param eqpInfo the eqpInfo to set
	 */
	public void setEqpInfo(String eqpInfo) {
		this.eqpInfo = eqpInfo;
	}

	/**
	 * @return the safeCarNum
	 */
	public int getSafeCarNum() {
		return safeCarNum;
	}

	/**
	 * @param safeCarNum the safeCarNum to set
	 */
	public void setSafeCarNum(int safeCarNum) {
		this.safeCarNum = safeCarNum;
	}

	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the starter
	 */
	public int getStarter() {
		return starter;
	}

	/**
	 * @param starter the starter to set
	 */
	public void setStarter(int starter) {
		this.starter = starter;
	}

	/**
	 * @return the isFinish
	 */
	public boolean isFinish() {
		return isFinish;
	}

	/**
	 * @param isFinish the isFinish to set
	 */
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	/**
	 * @return the isClean
	 */
	public boolean isClean() {
		return isClean;
	}

	/**
	 * @param isClean the isClean to set
	 */
	public void setClean(boolean isClean) {
		this.isClean = isClean;
	}

	/**
	 * @return the notice
	 */
	public String getNotice() {
		return notice;
	}

	/**
	 * @param notice the notice to set
	 */
	public void setNotice(String notice) {
		this.notice = notice;
	}
}
