/**
 * 
 */
package webApp.Weather.controller;

/**
 * @author smanish
 *
 */
public class WeatherHealth {
	
	private int id;
	private String status;
	
	
	public WeatherHealth(int id, String status) {
		super();
		this.id = id;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "WeatherHealth [id=" + id + ", status=" + status + "]";
	}
	
	
}
