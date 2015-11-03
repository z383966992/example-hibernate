package datasource;

public enum DataSourceType {
	
	MASTER("master",1,1),
	SLAVE("slave",1,1);
	
	private String name;
	private int priority;
	private int available;
	
	private DataSourceType(String name, int priority, int available) {
		this.name = name;
		this.priority = priority;
		this.available = available;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}
}
