package data;

import java.io.Serializable;


public class VisitorRecord implements Serializable{  
    /**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	private String uuid;
	private String name;  
    private String company;
    private String reason;
    private int starter;
    private String watcher;
    private String door_watcher;
    private String in_time;
    private String out_time;
    private boolean isMuti = false;
    private boolean isFinish = false;
    private byte[] watherHW;
    private byte[] doorHW;
    private byte[] out_doorHW;
    private int num;
    private String idType;
    private String idNum;
    private String tools;
    
    
  
    public VisitorRecord() {  
       
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
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the st_reason
	 */
	public String getSt_reason() {
		return reason;
	}

	/**
	 * @param st_reason the st_reason to set
	 */
	public void setSt_reason(String st_reason) {
		this.reason = st_reason;
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
	 * @return the idType
	 */
	public String getIdType() {
		return idType;
	}

	/**
	 * @param idType the idType to set
	 */
	public void setIdType(String idType) {
		this.idType = idType;
	}

	/**
	 * @return the idNum
	 */
	public String getIdNum() {
		return idNum;
	}

	/**
	 * @param idNum the idNum to set
	 */
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	/**
	 * @return the out_time
	 */
	public String getOut_time() {
		return out_time;
	}

	/**
	 * @param out_time the out_time to set
	 */
	public void setOut_time(String out_time) {
		this.out_time = out_time;
	}

	/**
	 * @return the in_time
	 */
	public String getIn_time() {
		return in_time;
	}

	/**
	 * @param in_time the in_time to set
	 */
	public void setIn_time(String in_time) {
		this.in_time = in_time;
	}

	/**
	 * @return the out_doorHW
	 */
	public byte[] getOut_doorHW() {
		return out_doorHW;
	}

	/**
	 * @param out_doorHW the out_doorHW to set
	 */
	public void setOut_doorHW(byte[] out_doorHW) {
		this.out_doorHW = out_doorHW;
	}

	/**
	 * @return the tools
	 */
	public String getTools() {
		return tools;
	}

	/**
	 * @param tools the tools to set
	 */
	public void setTools(String tools) {
		this.tools = tools;
	} 
}
