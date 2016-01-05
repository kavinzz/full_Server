package data;

import java.io.Serializable;
import java.util.Vector;

public class DataPackage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8931294548772982666L;
	private int cmd;
	private int type;
	private int clientNum = 0;
	private Vector<?> records;
	private int dataSize = 0;
	private String version = "";
	
	public DataPackage(String v,int command,int tp,int cNum,Vector<?> cRecords){
		version = v;
		cmd = command;
		type = tp;
		clientNum = cNum;
		records = cRecords;
		if(cRecords != null) dataSize = cRecords.size();
	}
	
	
	
	public int getCmd(){
		return cmd;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<CriminalRecord> getCriminalData(){
		return (Vector<CriminalRecord>) records;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<VisitorRecord> getVisitorData(){
		return (Vector<VisitorRecord>) records;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<CarRecord> getCarData(){
		return (Vector<CarRecord>) records;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<JmglDutyRecord> getJmDutyData(){
		return (Vector<JmglDutyRecord>) records;
	}
	
	@SuppressWarnings("unchecked")
	public Vector<TelRecord> getTelData() {
		// TODO Auto-generated method stub
		return (Vector<TelRecord>) records;
	}
	
	public int getClientNum(){
		return clientNum;
	}
	
	public void setSize(int size){
		dataSize = size;
	}
	
	public int getDataSize(){
		return dataSize;
	}
	
	public String getVersion(){
		return version;
	}
	
	public int getType(){
		return type;
	}



	
}
