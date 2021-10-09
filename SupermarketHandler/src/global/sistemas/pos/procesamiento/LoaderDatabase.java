package global.sistemas.pos.procesamiento;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import global.modelo.*;



public class LoaderDatabase {
	
	private SupermarketModeler modeladorSupermercado;
	
	public void loadDatabaseCSV(SupermarketModeler modeladorSupermercado) {
		
		this.modeladorSupermercado = modeladorSupermercado;
		
		//Orden de lo general a lo particular
		loadSupermercadoCSV(); //Listo
		loadSubcategoriasCSV(); //Listo
		loadCategoriasCSV(); //Listo
		loadLotesCSV(); //Listo
		loadInventarioCSV(); //Listo
		loadUnidadesDeAlmacenamientoCSV();
		loadEncargadosCSV();
		loadCajerosCSV();
		loadClientesCSV();
		
	}

	private void loadClientesCSV() {
		
		
	}

	private void loadCajerosCSV() {

		
	}

	private void loadEncargadosCSV() {

	}

	private void loadUnidadesDeAlmacenamientoCSV() {

		
	}

	private void loadInventarioCSV() {
		
		//Se empieza modelando un nuevo inventario desde 0
		modeladorSupermercado.modelarInventario();
		
		String[] fila = null;
		
		ArrayList<String[]> filas = readCSV("./data/inventario.csv");
		Iterator<String[]> filas_iterator = filas.iterator();
		
		while(filas_iterator.hasNext()) {
			fila = filas_iterator.next();
			
			//Cada fila del inventario representa un producto
			loadProductoCSV(fila);
			
		}

	}

	private void loadSupermercadoCSV() {
		
		String[] fila = null;

		ArrayList<String[]> filas = readCSV("./data/supermercado.csv");
		Iterator<String[]> filas_iterator = filas.iterator();
		
		while(filas_iterator.hasNext()) {
			fila = filas_iterator.next();
		}
		
		String nombreSupermercado = fila[0];
		
		modeladorSupermercado.modelarSupermercado(nombreSupermercado);
		
		
		
	
	}
	
	
	
	private void loadProductoCSV(String[] infoProducto) {
		
		String nombre = infoProducto[0];
		String marca = infoProducto[1];
		double precio = Double.parseDouble(infoProducto[2]);
		double precioPuntos = Double.parseDouble(infoProducto[3]);
		
		//Obtener categoria
		String nombreCategoria = infoProducto[4];
		
		
		boolean refrigeracion = Boolean.parseBoolean(infoProducto[5]);
		boolean congelacion = Boolean.parseBoolean(infoProducto[6]);
		
		//Obtener lote 
		String idLoteDeOrigen = infoProducto[7];
		
		
		//Obtener codigo
		String numeroCodigo = infoProducto[8];

		
		modeladorSupermercado.modelarProducto(nombre, marca, precio, precioPuntos, nombreCategoria, refrigeracion, congelacion, idLoteDeOrigen, numeroCodigo);
	}
	
	private Codigo loadCodigoCSV(String numeroCodigo) {
		return null;
	}
	
	
	private void loadSubcategoriasCSV() {
		
	String[] fila = null;

	ArrayList<String[]> filas = readCSV("./data/subcategorias.csv");
	Iterator<String[]> filas_iterator = filas.iterator();
	
	while(filas_iterator.hasNext()) {
		fila = filas_iterator.next();
		
		String nombre = fila[0];
		int numeroEstante = Integer.parseInt(fila[1]);
		int nivelEstante = Integer.parseInt(fila[2]);
		
		modeladorSupermercado.modelarSubcategoria(nombre, numeroEstante, nivelEstante);
	}
	}
		

	private void loadCategoriasCSV() {
	
		String[] fila = null;

		ArrayList<String[]> filas = readCSV("./data/categorias.csv");
		Iterator<String[]> filas_iterator = filas.iterator();
		
		while(filas_iterator.hasNext()) {
			fila = filas_iterator.next();
			
			String nombre = fila[0];
			int pasillo = Integer.parseInt(fila[1]);
			String nombreSubcategoria = fila[2];
			
			modeladorSupermercado.modelarCategoria(nombre, pasillo, nombreSubcategoria);
		}
		
			}
	
	
	private void loadLotesCSV() {
		String[] fila = null;
		Date fechaVencimiento = null;

		ArrayList<String[]> filas = readCSV("./data/lotes.csv");
		Iterator<String[]> filas_iterator = filas.iterator();
		
		while(filas_iterator.hasNext()) {
			fila = filas_iterator.next();
			
			String identificadorLote = fila[0]; 
			try {
				fechaVencimiento = new SimpleDateFormat("dd/MM/yyyy").parse(fila[1]);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			int numeroProductosBase = Integer.parseInt(fila[2]);
			int numeroProductosRestantes = Integer.parseInt(fila[3]);
			double precioCompraUnidad = Double.parseDouble(fila[4]);
			double precioVentaUnidad = Double.parseDouble(fila[5]);
			String idProducto = fila[6];
			
			modeladorSupermercado.modelarLote(identificadorLote, fechaVencimiento, numeroProductosBase, numeroProductosRestantes, precioCompraUnidad, precioVentaUnidad, idProducto);
			
		}
	}
	
		
	
	
	
	public ArrayList<String[]> readCSV(String pathArchivo){
		
			//ArrayList con las filas analizadas
			ArrayList<String[]> filas = new ArrayList<String[]>();
		
			// Abrir el archivo y leerlo línea por línea usando un BufferedReader
			try(BufferedReader br = new BufferedReader(new FileReader(pathArchivo));) {
			String linea = br.readLine(); // La primera línea del archivo se ignora porque únicamente tiene los títulos de
											// las columnas
			linea = br.readLine();
			while (linea != null) // Cuando se llegue al final del archivo, linea tendrá el valor null
			{
				// Separar los valores que estaban en una línea
				String[] partes = linea.split(",");
				filas.add(partes);
				
				linea = br.readLine(); // Leer la siguiente línea
		}
			br.close();}
			
			catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
			
			return filas;

	
	
	

}}
