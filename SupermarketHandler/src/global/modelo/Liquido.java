package global.modelo;

public class Liquido extends Producto{
	
	private double volumen;
	private double precioPormL;
	
	public Liquido(Lote loteDeOrigen, String nombre, String marca, String codigoProducto) {
		super(loteDeOrigen, nombre, marca, codigoProducto);
		// TODO Auto-generated constructor stub
	}

	public double getVolumen() {
		return this.volumen;
	}

	public void setVolumen(double volumen) {
		this.volumen = volumen;
	}

	public double getPrecioPormL() {
		return this.precioPormL;
	}

	public void setPrecioPormL(double precioPormL) {
		this.precioPormL = precioPormL;
	}

}
