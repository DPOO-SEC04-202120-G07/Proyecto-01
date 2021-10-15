package global.sistemas.inventario.procesamiento;
import java.util.Date;
import java.util.Iterator;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import global.modelo.*;

public class HandlerSI {
	
	private LoaderDatabase databaseLoader = new LoaderDatabase();
	private SupermarketModeler supermarketModeler = new SupermarketModeler();
	private SaverDatabase databaseSaver = new SaverDatabase();
	
	public void commandLoadCSVDatabase() throws FileNotFoundException{
		
		databaseLoader.loadDatabaseCSV(supermarketModeler);
		
	}
	
	public boolean encargadoRegistrado (String id) {
		return supermarketModeler.getSupermercado().getEncargados().containsKey(id);
	}
	
	public void registrarEncargado(String id, String nombre) {
		supermarketModeler.modelarEncargado(nombre, id);
	}
	
	public void cargarLote(String idDeLote) throws FileNotFoundException, HandledException{
		databaseLoader.loadNuevoLote(idDeLote);
	}

	public double[] consultarDesempenoProducto(String idProducto) throws HandledException {
		
		int productos_perdidos = 0;
		int productos_vendidos = 0;
		
		double gananciaProducto = 0;
		double perdidaProducto = 0;
		
		Producto productoConsultado = null;
		
		try {
		productoConsultado = supermarketModeler.getSupermercado().getBodega().getProductos().get(idProducto);
		
		Iterator<Lote> iterLotesConProducto = productoConsultado.getLotesDeOrigen().iterator();
		
		while(iterLotesConProducto.hasNext()) {
			
			Lote lote = iterLotesConProducto.next();
			
			if(lote.isVencido()) {
				productos_perdidos += lote.getNumeroProductosRestantes();
				productos_vendidos += lote.getNumeroProductosBase() - lote.getNumeroProductosRestantes();
				
				perdidaProducto += lote.getNumeroProductosRestantes() * lote.getPrecioCompraUnidad();
				gananciaProducto += ((lote.getNumeroProductosBase() - lote.getNumeroProductosRestantes()) * lote.getPrecioVentaUnidad()) - ((lote.getNumeroProductosBase() - lote.getNumeroProductosRestantes()) * lote.getPrecioCompraUnidad()) ;
				
			}
			
			else {
				productos_vendidos += lote.getNumeroProductosBase() - lote.getNumeroProductosRestantes();
				gananciaProducto += ((lote.getNumeroProductosBase() - lote.getNumeroProductosRestantes()) * lote.getPrecioVentaUnidad()) - ((lote.getNumeroProductosBase() - lote.getNumeroProductosRestantes()) * lote.getPrecioCompraUnidad()) ;
				
			}
			
		}
		
		
		}
		catch(NullPointerException e) {
			throw new HandledException("null-producto");
		}
		
		double[] desempenoProducto = {productos_perdidos, productos_vendidos, perdidaProducto, gananciaProducto};
		return desempenoProducto;
	
			
		}
	
	public int cantidadProductosInventario() {
		return supermarketModeler.getSupermercado().getBodega().getProductos().size();
	}

	public Object[] consultarInfoProducto(String idProductoInteres) {
		Producto productoConsultado = supermarketModeler.getSupermercado().getBodega().getProductos().get(idProductoInteres);
		
		String nombreProducto = productoConsultado.getNombre();
		String marcaProducto = productoConsultado.getMarca();
		String categoriaProducto = productoConsultado.getCategoria().getNombre();
		double precioProducto = productoConsultado.getPrecio();
		int cantidadDisponible = 0;
		
		Iterator<Lote> iterLotesConProducto = productoConsultado.getLotesDeOrigen().iterator();
		while(iterLotesConProducto.hasNext()) {
			
			Lote lote = iterLotesConProducto.next();
			
			if(!lote.isVencido()) {
				cantidadDisponible += lote.getNumeroProductosRestantes();
			}
		
		}
		
		Object[] infoProducto = {nombreProducto, marcaProducto, categoriaProducto, precioProducto, cantidadDisponible};
		return infoProducto;
	}

	public void eliminarProductosVencidos(String fechaActual) throws HandledException {
		
		Date fechaHoy=null;
		
		try {
			fechaHoy = new SimpleDateFormat("dd/MM/yyyy").parse(fechaActual);
			supermarketModeler.getSupermercado().setFechaActual(fechaHoy);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new HandledException("parse-date");
		};
		
		Iterator<Producto> iterProductos = supermarketModeler.getSupermercado().getBodega().getProductos().values().iterator();
		while(iterProductos.hasNext()) {
			Producto productoActual = iterProductos.next();
			
			Iterator<Lote> iterLotesConProducto = productoActual.getLotesDeOrigen().iterator();
			while(iterLotesConProducto.hasNext()) {
				Lote lote = iterLotesConProducto.next();
				Date fechaVencimiento = lote.getfechaVencimiento();
				
				if (fechaVencimiento.before(fechaHoy)){
					lote.setVencido(true);
				}
		}}
			
		}

	public void commandSaveCSVDatabase() {
		
		databaseSaver.saveDatabaseCSV(supermarketModeler);
	}
		


		
	
}
