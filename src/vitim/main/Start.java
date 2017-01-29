package vitim.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Properties;
import java.util.Scanner;

import dt.http.HttpConnection;
import vitim.data.Aposta;
import vitim.data.Resultado;

public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("Iniciando programa para verificacao de apostas ...");
		
		System.out.println("Carregando configurações do jogo ...");
		Scanner scan = new Scanner(System.in);
		try{			
			FileInputStream file = new FileInputStream("config/jogos.properties");
			Properties config = new Properties();
			config.load(file);
			
			int quantidadeJogo = Integer.parseInt(config.get("Quantidade_Jogo").toString());
			int quantidadeSorteio = Integer.parseInt(config.get("Quantidade_Sorteio").toString());
			int quantidadeMaximaAposta = Integer.parseInt(config.get("Numero_Maximo_Aposta").toString());
			int quantidadeApostas = Integer.parseInt(config.get("Quantidade_Apostas").toString());
			
			System.out.println("Carregando apostas ...");
			ArrayList<Aposta> apostas = new ArrayList<Aposta>();			
			for(int numAposta = 1; numAposta <= quantidadeApostas; numAposta++){
				System.out.println("Carregando Aposta_" + numAposta + " ...");
				Aposta aposta = new Aposta("Aposta_" + numAposta, quantidadeJogo, quantidadeMaximaAposta);
				aposta.carregarAposta(config.get("Aposta_" + numAposta).toString(), quantidadeJogo);
				apostas.add(aposta);
			}
			
			System.out.println("Apostas carregadas!\n");
			String modoConferencia = "";
			System.out.print("Como deseja realizar o batimento dos resultados?\nBuscando pelo número do sorteio (digite 'S')" + 
							   " ou digitando os números sorteados (digite 'D'): ");
			do{
				modoConferencia = scan.useDelimiter("\\p{javaWhitespace}+")
				          .useLocale(Locale.getDefault(Locale.Category.FORMAT))
				          .useRadix(10).next();
			}while(!modoConferencia.toUpperCase().equals("D") && !modoConferencia.toUpperCase().equals("S"));
			String saida = "S";
			do{
				if(saida.toUpperCase().equals("S")){
					if(modoConferencia.toUpperCase().equals("D")){
						try{
							System.out.println();
							System.out.println("Favor entrar com o resultado (separar os número por \",\"): ");
							String resultadoStr = scan.useDelimiter("\\p{javaWhitespace}+")
							          .useLocale(Locale.getDefault(Locale.Category.FORMAT))
							          .useRadix(10).next();
							Resultado resultado = new Resultado(resultadoStr, apostas, quantidadeSorteio, quantidadeJogo);
							resultado.verificarResultadoSorteio();
							System.out.println();
						}catch(Exception ex){
							System.out.println(ex.getMessage());
						}
					}else
						if((modoConferencia.toUpperCase().equals("S"))){
							boolean numSorteioValido = true;
							String numSorteio = "";
							do{
								numSorteioValido = true;
								System.out.println();
								System.out.println("Favor entrar com o número do sorteio: ");
								numSorteio = scan.useDelimiter("\\p{javaWhitespace}+")
								          .useLocale(Locale.getDefault(Locale.Category.FORMAT))
								          .useRadix(10).next();
								try{									
									Integer.parseInt(numSorteio);
									System.out.print("Buscando resultado na Web... ");
								}catch(NumberFormatException nex){
									System.out.println("ERRO: O valor digitado não é um número válido.");
									numSorteioValido = false;
								}
							}while(!numSorteioValido);
							
							HttpConnection httpConn = new HttpConnection(config.get("url_http").toString());
							httpConn.addFirstQueryStringParameter("sorteio", numSorteio, false);
							
							if(httpConn.createConnection()){
								BufferedReader buffReader = httpConn.doGET();
								String resultadoStr = Resultado.retornaResultado(buffReader);
								buffReader.close();
								if(!resultadoStr.trim().equals("") && !resultadoStr.equals("ERROR")){
									System.out.println(resultadoStr);
									Resultado resultado = new Resultado(resultadoStr, apostas, quantidadeSorteio, quantidadeJogo);
									resultado.verificarResultadoSorteio();
									System.out.println();
								}else
									if(resultadoStr.trim().equals(""))
										System.out.println("\nERRO: Número do sorteio não existe ainda.");
									else
										System.out.println("\nERRO: Ocorreu um problema para buscar o resultado do sorteio.");
							}else{
								System.out.println("\nERRO: Não foi possivel realizar a conexão com o repositório de dados.");
							}							
						}
				}
				System.out.print("Deseja continuar verificando as apostas? (S = sim / N = nao): ");
				saida = scan.useDelimiter("\\p{javaWhitespace}+")
				          .useLocale(Locale.getDefault(Locale.Category.FORMAT))
				          .useRadix(10).next();
			}while(!saida.toUpperCase().equals("N"));
		}catch(Exception ex){
			System.out.println(ex.getMessage());
		}finally{
			scan.close();
		}
		
		System.out.println("Programa finalizado!");
	}

}
