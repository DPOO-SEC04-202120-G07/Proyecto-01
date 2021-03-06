package global.sistemas.pos.procesamiento;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;


import java.util.stream.Collectors;


import global.modelo.*;

public class SaverDatabase {
	
	Supermercado supermercadoVolatil;

	public void saveDatabaseCSV(SupermarketModeler supermarketModeler) {
	
		this.supermercadoVolatil = supermarketModeler.getSupermercado();
	
		//Guardar lotes/productos cargados en inventario.csv
		savePosCSV();
		
		
	}

	private void savePosCSV() {
		
		ArrayList<String[]> lineasClientes = new ArrayList<String[]>();
		lineasClientes.add(new String[]{"nombre", "edad", "sexo", "cedula", "estadoCivil", "situacionLaboral"});
		
		ArrayList<String[]> lineasCajeros= new ArrayList<String[]>();
		lineasCajeros.add(new String[]{"nombre", "codigoC"});
		
	    File csvClientesFile = new File("./data/clientes.csv");
	    File csvCajerosFile = new File("./data/cajeros.csv");
	    
	    
		
	    Iterator<Cliente> iterClientes = supermercadoVolatil.getClientes().values().iterator();
	    Iterator<Cajero> iterCajeros = supermercadoVolatil.getCajeros().values().iterator();
	    
	    while(iterClientes.hasNext()) {
	    	Cliente clienteActual=iterClientes.next();
	    	
	    	String nombre=clienteActual.getNombre();
	    	String edad=""+clienteActual.getEdad();
	    	String sexo=""+clienteActual.getSexo();
	    	String cedula=clienteActual.getCedula();
	    	String estadoCivil=clienteActual.getEstadoCivil();
	    	String situacionLaboral=clienteActual.getSituacionLaboral();
	    	
	    	String[] lineaCliente= {nombre,edad,sexo,cedula,estadoCivil,situacionLaboral};
	    	lineasClientes.add(lineaCliente);
	    }
	    
	    while(iterCajeros.hasNext()) {
	    	Cajero cajeroActual=iterCajeros.next();
	    	
	    	String nombre=cajeroActual.getNombre();
	    	String codigoC=cajeroActual.getCodigoCajero();
	    	
	    	String[] lineaCajero= {nombre,codigoC};
	    	lineasCajeros.add(lineaCajero);
	    }
	    		
		//Escribir sobre clientes.csv
	    try (PrintWriter pw = new PrintWriter(csvClientesFile)) {
	    	lineasClientes.stream()
	          .map(this::convertToCSV)
	          .forEach(pw::println);
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
	  //Escribir sobre cajeros.csv
	    try (PrintWriter pw = new PrintWriter(csvCajerosFile)) {
	    	lineasCajeros.stream()
	          .map(this::convertToCSV)
	          .forEach(pw::println);
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
		Inventario inventarioVolatil = supermercadoVolatil.getBodega();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");  
		
		ArrayList<String[]> lineasInventario = new ArrayList<String[]>();
		lineasInventario.add(new String[]{"nombreProducto", "marcaProducto", "precioVentaUnidad", "precioPuntos", "nombreCategoria", "refrigeracion", 
				"congelacion", "identificadorLote", "idProducto", "fresco", "volumen", "peso", "empacado", "unidadesIncluidas"});
		
		ArrayList<String[]> lineasLotes = new ArrayList<String[]>();
		lineasLotes.add(new String[]{"identificadorLote", "fechaVencimiento", "numeroProductosBase", "numeroProductosRestantes", "precioCompraUnidad", "precioVentaUnidad", "idProducto", "vencido"});
		
	    File csvInventarioFile = new File("./data/inventario.csv");
	    File csvLotesFile = new File("./data/lotes.csv");
	    
		
		Iterator<Producto> iterProductos = inventarioVolatil.getProductos().values().iterator();
		while(iterProductos.hasNext()) {
			Producto productoActual = iterProductos.next();
			
			Iterator<Lote> iterLotesConProducto = productoActual.getLotesDeOrigen().iterator();
			while(iterLotesConProducto.hasNext()) {
				Lote lote = iterLotesConProducto.next();

				String identificadorLote = lote.getIdentificadorLote();
				String fechaVencimiento = dateFormat.format(lote.getfechaVencimiento());
				String numeroProductosBase = "" + lote.getNumeroProductosBase();
				String numeroProductosRestantes = "" + lote.getNumeroProductosRestantes();
				String precioCompraUnidad = "" + lote.getPrecioCompraUnidad();
				String precioVentaUnidad = "" + lote.getPrecioVentaUnidad(); 
				String precioPuntos = "" +lote.getProducto().getPrecioPuntos();
				
				String codigoProducto = lote.getProducto().getCodigoProducto().getCodigo();
				String nombreProducto = lote.getProducto().getNombre();
				String marca = lote.getProducto().getMarca();
				String categoria = lote.getProducto().getCategoria().getNombre();
				String refrigeracion = ""+lote.getProducto().getRefrigeracion();
				String congelacion = ""+lote.getProducto().getRefrigeracion();
				String vencido = "" +lote.isVencido();
				
				String fresco = "" + lote.getProducto().isFresco();
				String empacado = "true";
				String volumen = "None";
				String peso = "None";
				String unidadesIncluidas = "None";
				
				
				volumen = ""+ lote.getProducto().getVolumen();
				if(volumen == "0") {
					volumen = "None";
					empacado = "false";}
		
				
				 peso = "" + lote.getProducto().getPeso();
				if(peso == "0") {
					 peso = "None";
					empacado = "false";}
	
				

				unidadesIncluidas = "" + lote.getProducto().getUnidades();
				if (unidadesIncluidas == "0") {
				 unidadesIncluidas = "None";
				empacado = "false";}
				
				
				
				
				
				String[] lineaInventario = {nombreProducto, marca, precioVentaUnidad, precioPuntos, categoria, refrigeracion, congelacion, identificadorLote, codigoProducto, fresco, volumen, peso, empacado, unidadesIncluidas};
				String[] lineaLote = {identificadorLote, fechaVencimiento, numeroProductosBase, numeroProductosRestantes, precioCompraUnidad, precioVentaUnidad, codigoProducto, vencido};
		
				lineasInventario.add(lineaInventario);
				lineasLotes.add(lineaLote);					
			}}
		
		//Escribir sobre inventario.csv
	    try (PrintWriter pw = new PrintWriter(csvInventarioFile)) {
	    	lineasInventario.stream()
	          .map(this::convertToCSV)
	          .forEach(pw::println);
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    //Escribir sobre lotes.csv
	    try (PrintWriter pw = new PrintWriter(csvLotesFile)) {
	    	lineasLotes.stream()
	          .map(this::convertToCSV)
	          .forEach(pw::println);
	    } catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
		
		
		//M??todos para cargar al CSV tomados de https://www.baeldung.com/java-csv
		
		public String convertToCSV(String[] data) {
		    return Stream.of(data)
		      .collect(Collectors.joining(","));
		}
		
	}


