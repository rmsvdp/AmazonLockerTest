
public class Cajon {

	private int alto;
	private int ancho;
	private int estado;
	private String sn_item;
	
	
	public Cajon(int alto, int ancho, int estado, String sn_item) {
		super();
		this.alto = alto;
		this.ancho = ancho;
		this.estado = estado;
		this.sn_item = sn_item;
	}


	public int getAlto() {		return alto;	}
	public void setAlto(int alto) {		this.alto = alto;	}
	public int getAncho() {		return ancho;	}
	public void setAncho(int ancho) {		this.ancho = ancho;	}
	public int getEstado() {		return estado;	}
	public void setEstado(int estado) {		this.estado = estado;	}
	public String getSn_item() {		return sn_item;	}
	public void setSn_item(String sn_item) {		this.sn_item = sn_item;	}
	
	
	
}
