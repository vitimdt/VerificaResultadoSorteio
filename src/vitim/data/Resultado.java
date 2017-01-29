package vitim.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import vitim.helper.Validacao;

/**
 * Classe Resultado
 * @author Victor Araujo
 *
 */
public class Resultado {
	
	private ArrayList<Aposta> listaApostas;
	private int[] resultadoJogo;
	
	public Resultado(String resultado, ArrayList<Aposta> listaApostas, int quantidadeSorteio, int maiorNumJogo) throws Exception{
		if(resultado == null || resultado.length() == 0)
			throw new Exception("Resultado não informado.");
		
		this.listaApostas = listaApostas;
		String[] arrResultadoStr = resultado.split(",");
		if(arrResultadoStr.length != quantidadeSorteio)
			throw new Exception("A quantidade de números informado está diferente da quantidade de números do sorteio.");
				
		this.resultadoJogo = new int[quantidadeSorteio];
		
		for(int incr = 0; incr < arrResultadoStr.length; incr++){
			this.resultadoJogo[incr] = Integer.parseInt(arrResultadoStr[incr].trim());
			if(this.resultadoJogo[incr] > maiorNumJogo)
				throw new Exception("O resultado informado possui número maior que o maior numero do jogo.");
		}
		
		if(Validacao.existeNumerosRepetidos(this.resultadoJogo))
			throw new Exception("O resultado informado possui números repetidos e não é permitido.");		
	}
	
	public void verificarResultadoSorteio() throws Exception{
		if(this.listaApostas == null || this.listaApostas.isEmpty())
			throw new Exception("Não foram carregadas apostas.");
		
		for(Aposta aposta : this.listaApostas){
			int numAcertos = aposta.verificarResultado(this.resultadoJogo);
			System.out.println("A " + aposta.getNome() + " teve " + numAcertos + " acertos.");
		}
	}
	
	public static String retornaResultado(BufferedReader bufferReader){
		StringBuffer resultado = new StringBuffer(10000);
		String str = "";
		try {
			while((str = bufferReader.readLine()) != null){
				if(str.contains("<td  width='25' height='23' align='center' valign='middle' background='http://www.gerasorteonline.com.br/img/bola_sorteio.gif'><font color='#000000' face='Verdana' size='2' ><strong><font color=\"#841873\" size=\"2\">")){
					str = str.replaceAll("<td  width='25' height='23' align='center' valign='middle' background='http://www.gerasorteonline.com.br/img/bola_sorteio.gif'><font color='#000000' face='Verdana' size='2' ><strong><font color=\"#841873\" size=\"2\">", "");
					str = str.replaceAll(" ", "");
					str = str.replaceAll("</font></strong></font></td>", "");
					if(resultado.length() == 0)
						resultado.append(Integer.parseInt(str));
					else
						resultado.append("," + Integer.parseInt(str));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			str = "ERROR";
		} catch (NumberFormatException nex){
			if(!str.trim().equals("")){				
				nex.printStackTrace();
				str = "ERROR";
			}
		}
		
		return new String(resultado);
	}

	public ArrayList<Aposta> getListaApostas() {
		return listaApostas;
	}

	public void setListaApostas(ArrayList<Aposta> listaApostas) {
		this.listaApostas = listaApostas;
	}

	public int[] getResultadoJogo() {
		return resultadoJogo;
	}

	public void setResultadoJogo(int[] resultadoJogo) {
		this.resultadoJogo = resultadoJogo;
	}
	
}
