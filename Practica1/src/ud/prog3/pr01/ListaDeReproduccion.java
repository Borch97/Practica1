package ud.prog3.pr01;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


/** Clase para crear instancias como listas de reproducción,
 * que permite almacenar listas de ficheros con posición de índice
 * (al estilo de un array / arraylist)
 * con marcas de error en los ficheros y con métodos para cambiar la posición
 * de los elementos en la lista, borrar elementos y ańadir nuevos.
 */
public class ListaDeReproduccion implements ListModel<String> {
	ArrayList<File> ficherosLista;     // ficheros de la lista de reproducción
	int ficheroEnCurso = -1;     // Fichero seleccionado (-1 si no hay ninguno seleccionado)
	private static Logger logger = Logger.getLogger(ListaDeReproduccion.class.getName());
	private static final boolean ANYADIR_A_FIC_LOG = false;
			static{
		try{
			logger.addHandler(new FileHandler(ListaDeReproduccion.class.getName() + ".log.xml", ANYADIR_A_FIC_LOG));
		}	catch(SecurityException | IOException e){
			logger.log(Level.SEVERE, "Error en creación del log");
		}
		
	}
	
	
	/**Constructor que inicializa una lista de reproducción vacia
	 * 
	 */
	public ListaDeReproduccion(){
		ficherosLista = new ArrayList<File>();
	}
	
	
	/**
	 * Método que, dada dos posiciones de una lista de reproducción, las intercambia
	 * @param pos1
	 * @param pos2
	 */
	public void intercambia(int pos1, int pos2){
		while((ficherosLista.size()-1 >= pos1 && ficherosLista.size()-1 >= pos2)){
			ficherosLista.add(getFic(pos1));
			ficherosLista.set(pos1, getFic(pos2));
			ficherosLista.set(pos2,getFic(ficherosLista.size()-1));
			ficherosLista.remove(ficherosLista.size()-1);
		}
	}
	
	/**
	 * @return Devuelve el tamaño de una lista de reproducción
	 */
	public int size(){
		return ficherosLista.size();
	}
	
	/**
	 * Añade un archivo nuevo a una lista de reproducción
	 * @param f
	 */
	public void add(File f){
		ficherosLista.add(f);
	}
	
	/**
	 * Elimina un archivo de la lista de reproducción
	 * @param pos
	 */
	public void removeFic(int pos){
		ficherosLista.remove(pos);
	}
	
	/**
	 * Vacia una lista de reproducción
	 */
	public void clear(){
		ficherosLista.clear();
	}
	/** Devuelve uno de los ficheros de la lista
	 * @param posi	Posición del fichero en la lista (de 0 a size()-1)
	 * @return	Devuelve el fichero en esa posición
	 * @throws IndexOutOfBoundsException	Si el índice no es válido
	 */
	public File getFic( int posi ) throws IndexOutOfBoundsException {
		return ficherosLista.get( posi );
	}	

	/** Ańade a la lista de reproducción todos los ficheros que haya en la 
	 * carpeta indicada, que cumplan el filtro indicado.
	 * Si hay cualquier error, la lista de reproducción queda solo con los ficheros
	 * que hayan podido ser cargados de forma correcta.
	 * @param carpetaFicheros	Path de la carpeta donde buscar los ficheros
	 * @param filtroFicheros	Filtro del formato que tienen que tener los nombres de
	 * 							los ficheros para ser cargados.
	 * 							String con cualquier letra o dígito. Si tiene un asterisco
	 * 							hace referencia a cualquier conjunto de letras o dígitos.
	 * 							Por ejemplo p*.* hace referencia a cualquier fichero de nombre
	 * 							que empiece por p y tenga cualquier extensión.
	 * @return	Número de ficheros que han sido ańadidos a la lista
	 */
	public int add(String carpetaFicheros, String filtroFicheros) {
		// TODO: Codificar este método de acuerdo a la práctica (pasos 3 y sucesivos)
		logger.log(Level.INFO, "Añadiendo ficheros con filtro " + filtroFicheros);
		filtroFicheros = filtroFicheros.replaceAll( "\\.", "\\\\." );  // Pone el símbolo de la expresión regular \. donde figure un .
		logger.log(Level.INFO, "Añadiendo ficheros con filtro " + filtroFicheros);
		filtroFicheros = filtroFicheros.replaceAll("\\*", "\\.*");
		logger.log(Level.INFO, "Añadiendo ficheros con filtro " + filtroFicheros);
		ArrayList<File> fTest = new ArrayList<File>();
		File fInic = new File(carpetaFicheros);
		Pattern patron = Pattern.compile(filtroFicheros);
		//logger.log(Level.SEVERE, "Fallo de compilación del patrón ");
		if(fInic.isDirectory()){
			for (File f: fInic.listFiles()) {
				logger.log(Level.FINE, "Procesando fichero " + f.getName());
				Matcher concuerda = patron.matcher(f.getName());
				if(concuerda.find()){
					fTest.add(f);
				}
			}
		}
		return fTest.size();
	}
	
	
	//
	// Métodos de selección
	//
	
	/** Seleciona el primer fichero de la lista de reproducción
	 * @return	true si la selección es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irAPrimero() {
		ficheroEnCurso = 0;  // Inicia
		if (ficheroEnCurso>=ficherosLista.size()) {
			ficheroEnCurso = -1;  // Si no se encuentra, no hay selección
			return false;  // Y devuelve error
		}
		return true;
	}
	
	/** Seleciona el último fichero de la lista de reproducción
	 * @return	true si la selección es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irAUltimo() {
		ficheroEnCurso = ficherosLista.size()-1;  // Inicia al final
		if (ficheroEnCurso==-1) {  // Si no se encuentra, no hay selección
			return false;  // Y devuelve error
		}
		return true;
	}

	/** Seleciona el anterior fichero de la lista de reproducción
	 * @return	true si la selección es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irAAnterior() {
		if (ficheroEnCurso>=0) ficheroEnCurso--;
		if (ficheroEnCurso==-1) {  // Si no se encuentra, no hay selección
			return false;  // Y devuelve error
		}
		return true;
	}

	/** Seleciona el siguiente fichero de la lista de reproducción
	 * @return	true si la selección es correcta, false si hay error y no se puede seleccionar
	 */
	public boolean irASiguiente() {
		ficheroEnCurso++;
		if (ficheroEnCurso>=ficherosLista.size()) {
			ficheroEnCurso = -1;  // Si no se encuentra, no hay selección
			return false;  // Y devuelve error
		}
		return true;
	}

	/** Devuelve el fichero seleccionado de la lista
	 * @return	Posición del fichero seleccionado en la lista de reproducción (0 a n-1), -1 si no lo hay
	 */
	public int getFicSeleccionado() {
		return ficheroEnCurso;
	}

	//
	// Métodos de DefaultListModel
	//
	
	@Override
	public int getSize() {
		return ficherosLista.size();
	}

	@Override
	public String getElementAt(int index) {
		return ficherosLista.get(index).getName();
	}

		// Escuchadores de datos de la lista
		ArrayList<ListDataListener> misEscuchadores = new ArrayList<>();
	@Override
	public void addListDataListener(ListDataListener l) {
		misEscuchadores.add( l );
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		misEscuchadores.remove( l );
	}
	
	// Llamar a este método cuando se ańada un elemento a la lista
	// (Utilizado para avisar a los escuchadores de cambio de datos de la lista)
	private void avisarAnyadido( int posi ) {
		for (ListDataListener ldl : misEscuchadores) {
			ldl.intervalAdded( new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, posi, posi ));
		}
	}
}
