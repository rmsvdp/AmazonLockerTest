import java.util.Scanner;


/**
 * Implementación de un modelo simplificado de utilización de un contenedor de mensajería, similar al
 * servicio Amazon Locker.
 * Se han simplificado las funciones y el modelado de las entidades.
 * CUESTIONES PREVIAS PARA LA IMPLEMENTACIÓN
 * 
 * 0.- ES OBLIGATORIO SEGUIR LA IMPLEMENTACIÓN UTILIZADA.
 *     Otros esquemas de modelado de datos válidos no serán tenidos en cuenta en la revisión
 *     del trabajo.
 *     Podría argumentarse, que sólo es necesario disponer de un array de String para implementar
 *     todas las funciones y es cierto, pero se debe pensar que esta parte de programa se engloba
 *     dentro de otro desarrollo mayor y aunque parezca innecesario, el array de boolean, es necesario
 *     para otros módulos que está desarrollando otro equipo distinto del nuestro.
 * 1.- Lee atentamente los comentarios
 * 2.- Analiza y comprende la estructura de la aplicación, utiliza el depurador para
 * 	   verificar que el flujo de ejecución que piensas  es el real
 * 3.- Notas técnicas
 * 
 * 		POSICIÓN: Reflexiona sobre como se puede devolver la posición. Con la información
 * 		reciba hasta ahora, sólo podíamos devolver tipos primitivos, ¿cómo se te ocurre
 * 		devolver 'de una vez' la fila y la columna?
 * 
 * 		Genera todos los métodos de apoyo que estimes necesarios, es recomendable programar modularmente
 * 		(no va a afectar al rendimiento), ya que aumentará la legibilidad y claridad del código.
 * 
 * 		IdPedido: Como es de tipo String, vamos a suponer que el formato del mismo será:
 * 				  AAA : 3 caracteres para la provincia
 * 				  999 : 3 numeros para generar un identificador único
 * 			      Ejemplo : MAD007
 *      Elegiremos el valor "", para indicar que el cajón no tiene ningún paquete en su interior.
 * 
 * @author root
 *
 */
public class AppMain {

	public final int FILAS = 6;										// Número de cajones en una columna
	public final int COLUMNAS = 4;									// Número de columnas del locker
	public final int TOTAL = FILAS * COLUMNAS;						// Total de cajones en el locker
	// --- Aún manejando un valor conocido, a veces conviene utilizar constantes para dejar más claro
	// --- el significado
	public final boolean LIBRE = false;
	public final boolean OCUPADO = true;
	
	public static void main(String[] args) {
		
		System.out.println("Aplicación Iniciada.");
		AppMain app = new AppMain();
		app.run();
		System.out.println("\n\nAplicación Terminada.");
	}

	public void run() {
		
		String[][] lockerPack = new String[FILAS][COLUMNAS];		// Guardar el identificador único del paquete del cliente
		boolean[][] lockerStat = new boolean[FILAS][COLUMNAS];		// Guardar estado del loocker true : libre, false : ocupado
		int [] posicion = {0,0};									// Vamos a utilizar un array de dos posiciones
																	// para guardar fila en [0] y columna en [1]
		boolean salir = false;
		reset_locker(lockerPack,lockerStat);						// inicializar el locker
		Scanner sc = new Scanner(System.in);
		while (!salir) {
			
			// Pinta el menú de opciones
			System.out.println("\n\n------------------------------");
			System.out.println("-- REPARTIDOR --");
			System.out.println("1.- Ocupación del locker");
			System.out.println("2.- Dejar paquete en locker");
			// TODO incluir opción nueva
			System.out.println("-- CLIENTE --");
			System.out.println("3.- Estado envío");
			System.out.println("4.- Retirar paquete en locker");

			System.out.println("0.- Salir");
			System.out.println("------------------------------");
			
			// Solicita opción al usuario
			System.out.print("\nElija opcion : ");
			int opcion = sc.nextInt();
			
			switch (opcion) {
			
				case 1:												// Ocupación del locker
					mostrarOcupacion(lockerStat);					// resultado numérico
					showLocker(lockerStat);							// resultado visual
					break;
				case 2:
					dejarPaquete(lockerPack,lockerStat);			// Depositar paquete en locker
					break;
				case 3:
					estadoEnvio(lockerPack);						// Comprobar si ha llegado el pedido
					break;
				case 4:
					retirarPaquete(lockerPack,lockerStat);			// Retirar mi paquete
					break;
				case 0:
					salir = true;									// Abandonar aplicación
					break;
				default:
					System.out.print("\nOpción no válida!");
					break;
			} // endSwitch
		} // endWhile
	} // endRun

	/**
	 * Método para comprobar la ocupación del locker
	 * @param lock
	 */
	public void mostrarOcupacion(boolean[][] lock) {
		
		/*
		 * Recorrer el array de estado del locker y contar cuantos están a LIBRE
		 * Mostar el numero de 'cajones libres'
		 * Mostrar el mumero de cajones ocupados ( TOTAL - 'cajones libres')
		 */
		int libres=0;
		for (int i = 0; i< FILAS; i++) {
			for (int j =0 ; j < COLUMNAS; j++) {
				
				if (lock[i][j]==LIBRE) libres++;			// actualiza libres
			}
		}
		System.out.print("\nCajones libres : "+ libres);
		System.out.print("\nOcupación      : " + ((TOTAL-libres)*100)/TOTAL + "%");
	}
	
	/**
	 * Método para  dejar un pedido en el locker
	 * @param lock
	 */
	public void dejarPaquete(String[][] lockPack,boolean[][] lockStat ) {
		
		/*
		 * pedir codigo;
		 * POSICION = buscar cajon libre;
		 * asignar el codigo en el locker de paquetes en la POSICION
		 * actualizar a true el locker de estado en la POSICION
		 */
		int[] pos = findLibre(lockStat);							// Busco un cajón libre
		
		if (pos == null) {											// procede a la operación
			System.out.println("El locker está lleno, no se puede dejar paquetes");
		} else {
			String codigo = leerIdPedido();							// Pedir el código del pedido
			int fila = pos[0];
			int columna = pos[1];
			lockPack[fila][columna] = codigo;						// Deposito el paquete
			lockStat[fila][columna] = OCUPADO;						// Marcar el cajón como ocupado
			System.out.println("Paquete depositado en la posición ["+fila+"]["+columna+"]");
		}
	} // end dejarPaquete
	
	/**
	 * Método para comprobar si el paquete está ya en el locker
	 * @param lock
	 */
	
	public void estadoEnvio(String[][] lock) {
		
		/*
		 * pedir el codigo;
		 * recorre el locker buscando el codigo
		 * y retornar TRUE si lo he encontrado o FALSE si no está todavía
		 * Alternativamente puedo mostrar aquí el mensaje
		 */
		String pedido = leerIdPedido();
		int[] pos = findPedido(lock,pedido);
		if (pos== null) {
			System.out.print("\nSu pedido no se encuentra en el locker. ");
		}
		else {
			System.out.print("\nSu pedido está listo para recoger. ");
				
		} 
	}
	/**
	 * Método para recoger el paquete que está en el locker
	 * @param lock
	 */
	public void retirarPaquete(String[][] lockPack,boolean[][] lockStat) {
		
		
		/*
		 * pedir codigo;
		 * POSICION = buscar cajon donde esta el codigo;
		 * actualizar a false el locker de estado en la POSICION
		 * actualizo el estado 'sin paquete' el locker de paquetes
		 */
		int pos[];
		String pedido = leerIdPedido();
		pos = findPedido(lockPack,pedido);
		if (pos==null) {
			System.out.print("\nEl pedido : "+ pedido+ ", no se encuentra en el locker. ");
		}
		else {
			int fila = pos[0];
			int columna = pos[1];
			lockPack[fila][columna]="";					// Retiro el paquete del cajón
			lockStat[fila][columna]=LIBRE;				// Marco el cajón como libre
			System.out.print("\nPaquete RECOGIDO con éxito ");
		}
	}
	
	/** Inicializar locker
	 * 
	 */
	public void reset_locker(String[][] l1, boolean l2[][]) {
		
		for (int i = 0; i< FILAS; i++) {
			for (int j =0 ; j < COLUMNAS; j++) {
				
				l1[i][j]="";							// Borrar pedidos registrados
				l2[i][j]=LIBRE;							// Marcar estado a libre
			}
		}
		
	}  // end reset_locker
	
	/**
	 * Obtener la primera posición libre
	 */
	
	public int[] findLibre(boolean locker[][]) {
		
		int[] pos = new int[2];
		for (int i = 0; i< FILAS; i++) {
			for (int j =0 ; j < COLUMNAS; j++) {
				
				if (locker[i][j]==LIBRE) {
					pos[0]=i;						// Escribo fila
					pos[1]=j;						// Escribo olumna 
					return pos;						// encontrado, lo retorno
				};
			}
		}
		return null;								// no encontrado
		
	} // end findLibre 
	
	/**
	 * Obtener la posición del paquete
	 * @param locker
	 * @return posición o null si no se encuentra
	 */
	
	public int[] findPedido(String locker[][],String pedido) {
		
		int[] pos = new int[2];
		for (int i = 0; i< FILAS; i++) {
			for (int j =0 ; j < COLUMNAS; j++) {
				
				if (locker[i][j]==pedido) {
					pos[0]=i;						// Escribo fila
					pos[1]=j;						// Escribo olumna 
					return pos;						// encontrado, lo retorno
				};
			}
		}
		return null;								// no encontrado
		
	} // end findLibre 
	
	/**
	 * Función auxiliar para pedir al usuario el identificador del pedido.
	 * Vale tanto para el repartidor como para el cliente
	 * @return
	 */
	public String leerIdPedido() {
		Scanner sc = new Scanner(System.in);
		boolean salir = false;
		String codigo = "";
		while (!salir) {
			System.out.print("Introduce Identificador de pedido > ");
			String id = sc.nextLine();
			if (id.length()!=6) {
				System.out.println("Identificador no válido (introduce 6 caracteres).");	
			} 
			else {
				salir = true;
			}
		} // endWhile
		return codigo;
	} // endleerIdPedido
	
	/**
	 * Muestra por pantalla el estado detallado de la ocupación del locker
	 * 
	 */
	
	public void showLocker(boolean[][] locker) {
		
		System.out.println();											// Fila de separación
		for (int i = 0; i< FILAS; i++) {								// recorrer filas
			for (int j =0 ; j < COLUMNAS; j++) {						// recorrer columnas
		
				System.out.print("[");
				System.out.print((locker[i][j]==OCUPADO)?"*":" ");		// Pinto cajón en función de su estado
				System.out.print("] ");
			}
			System.out.println();										// siguiente linea
		}
		System.out.println();											// Fila de separación
	} // end showLocker
} // endClass

