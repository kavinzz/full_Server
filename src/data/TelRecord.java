package data;

import java.io.Serializable;
import java.sql.Timestamp;


public class TelRecord implements Serializable{  
    /**
	 * 
	 */
	private static final long serialVersionUID = 5L;
	private String name;  
    private String company;
    private String reason;
    private int starter;
    private String number;
    private int duty_index;
    private String time;
    private int lines;
    private String next_info;
    private String back_info;
    private Timestamp st_time;
    
  
    public TelRecord() {  
       
    }  
  
    public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
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
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the duty_index
	 */
	public int getDuty_index() {
		return duty_index;
	}

	/**
	 * @param duty_index the duty_index to set
	 */
	public void setDuty_index(int duty_index) {
		this.duty_index = duty_index;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * @return the lines
	 */
	public int getLines() {
		return lines;
	}

	/**
	 * @param lines the lines to set
	 */
	public void setLines(int lines) {
		this.lines = lines;
	}

	/**
	 * @return the next_info
	 */
	public String getNext_info() {
		return next_info;
	}

	/**
	 * @param next_info the next_info to set
	 */
	public void setNext_info(String next_info) {
		this.next_info = next_info;
	}

	/**
	 * @return the back_info
	 */
	public String getBack_info() {
		return back_info;
	}

	/**
	 * @param back_info the back_info to set
	 */
	public void setBack_info(String back_info) {
		this.back_info = back_info;
	}

	/**
	 * @return the st_time
	 */
	public Timestamp getSt_time() {
		return st_time;
	}

	/**
	 * @param st_time the st_time to set
	 */
	public void setSt_time(Timestamp st_time) {
		this.st_time = st_time;
	}
}
