package data;

import java.io.Serializable;


public class CriminalRecord implements Serializable{  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uuid;
	private String name;  
    private int sex;
    private int company;
    private int state;
    private String st_reason;
    private int destination;
    private int starter;
    private int send_to;
    private String watcher;
    private String door_watcher;
    private String other_place = null;
    private String st_time;
    private String fatherId = null;
    private boolean isMuti = false;
    private boolean isFinish = false;
    private boolean isDel = false;
    private byte[] watherHW;
    private byte[] doorHW;
    private int num;
    
    
  
    public CriminalRecord() {  
       
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
	 * @return the sex
	 */
	public int getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(int sex) {
		this.sex = sex;
	}

	/**
	 * @return the company
	 */
	public int getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(int company) {
		this.company = company;
	}

	/**
	 * @return the st_reason
	 */
	public String getSt_reason() {
		return st_reason;
	}

	/**
	 * @param st_reason the st_reason to set
	 */
	public void setSt_reason(String st_reason) {
		this.st_reason = st_reason;
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
	 * @return the destination
	 */
	public int getDestination() {
		return destination;
	}

	/**
	 * @param destination the destination to set
	 */
	public void setDestination(int destination) {
		this.destination = destination;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * @return the send_to
	 */
	public int getSend_to() {
		return send_to;
	}

	/**
	 * @param send_to the send_to to set
	 */
	public void setSend_to(int send_to) {
		this.send_to = send_to;
	}

	/**
	 * @return the door_watcher
	 */
	public String getDoor_watcher() {
		return door_watcher;
	}

	/**
	 * @param door_watcher the door_watcher to set
	 */
	public void setDoor_watcher(String door_watcher) {
		this.door_watcher = door_watcher;
	}

	/**
	 * @return the watcher
	 */
	public String getWatcher() {
		return watcher;
	}

	/**
	 * @param watcher the watcher to set
	 */
	public void setWatcher(String watcher) {
		this.watcher = watcher;
	}

	/**
	 * @return the st_time
	 */
	public String getSt_time() {
		return st_time;
	}

	/**
	 * @param st_time the st_time to set
	 */
	public void setSt_time(String st_time) {
		this.st_time = st_time;
	}

	/**
	 * @return the other_place
	 */
	public String getOther_place() {
		return other_place;
	}

	/**
	 * @param other_place the other_place to set
	 */
	public void setOther_place(String other_place) {
		if(other_place == "") this.other_place = null;
		else this.other_place = other_place;
	}

	/**
	 * @return the fatherID
	 */
	public String getFatherID() {
		return fatherId;
	}

	/**
	 * @param fatherID the fatherID to set
	 */
	public void setFatherID(String fatherID) {
		this.fatherId = fatherID;
	}

	/**
	 * @return the isMuti
	 */
	public boolean isMuti() {
		return isMuti;
	}

	/**
	 * @param isMuti the isMuti to set
	 */
	public void setMuti(boolean isMuti) {
		this.isMuti = isMuti;
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
	 * @return the watherHW
	 */
	public byte[] getWatherHW() {
		return watherHW;
	}

	/**
	 * @param watherHW the watherHW to set
	 */
	public void setWatherHW(byte[] watherHW) {
		this.watherHW = watherHW;
	}

	/**
	 * @return the doorHW
	 */
	public byte[] getDoorHW() {
		return doorHW;
	}

	/**
	 * @param doorHW the doorHW to set
	 */
	public void setDoorHW(byte[] doorHW) {
		this.doorHW = doorHW;
	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return the isDel
	 */
	public boolean isDel() {
		return isDel;
	}

	/**
	 * @param isDel the isDel to set
	 */
	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}  
}
