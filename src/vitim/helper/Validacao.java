package vitim.helper;

public class Validacao {

	public static boolean existeNumerosRepetidos(int[] array){
		boolean existe = false;
		
		for(int pri = 0; pri < array.length; pri++){
			for(int sec = 0; sec < array.length; sec++)
				if(pri != sec)	
					if(array[pri] == array[sec]){
						existe = true;
						break;
					}
			if(existe)
				break;
		}
		
		return existe;
	}
}
