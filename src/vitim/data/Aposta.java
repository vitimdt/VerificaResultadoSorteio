package vitim.data;

import vitim.helper.Validacao;

/**
 * Classe Aposta
 * @author Victor Araujo
 *
 */
public class Aposta {

	private String nome;
	private int quantidade = 0;
	private int[] aposta;
	
	/**
	 * Construtor da classe Aposta
	 * @param nome
	 * @param quantMaxJogo
	 * @param quantApostado
	 * @throws Exception 
	 */
	public Aposta(String nome, int quantMaxJogo, int quantApostado) throws Exception{
		if(quantApostado <  quantMaxJogo){
			this.nome = (nome != null ? nome : "");
			this.quantidade = quantApostado;
			this.aposta = new int[this.quantidade];
		}else{
			throw new Exception("Não é permitido apostar em uma quantidade de números maior do que o jogo possui.");
		}
	}
	
	/**
	 * Carrega uma aposta por uma String passada como par�metro
	 * @param strAposta
	 * @throws Exception
	 */
	public void carregarAposta(String strAposta, int maiorNumJogo) throws Exception{
		if(strAposta == null || strAposta.length() == 0)
			throw new Exception("Aposta não informada.");
		
		String[] arrApostaStr = strAposta.split(",");
		
		for(int incr = 0; incr < arrApostaStr.length; incr++){
			this.aposta[incr] = Integer.parseInt(arrApostaStr[incr].trim());
			if(this.aposta[incr] > maiorNumJogo)
				throw new Exception("A aposta " + this.nome + " possui número maior que o maior número do jogo.");
		}
		
		if(Validacao.existeNumerosRepetidos(this.aposta))
			throw new Exception("A aposta " + this.nome + " possui números repetidos e não é permitido.");
	}
	
	/**
	 * Retorna a quantidade de acertos passando um resultado
	 * @param resultado
	 * @return
	 * @throws Exception
	 */
	public int verificarResultado(int[] resultado) throws Exception{
		if(this.aposta == null || this.aposta.length == 0)
			throw new Exception("Aposta não carregada. Favor configurar uma ou mais apostas.");
		if(resultado == null || resultado.length == 0)
			throw new Exception("Resultado está vazio.");
		
		int acertos = 0;
		for(int numAposta = 0; numAposta < this.aposta.length; numAposta++){
			for(int incrResultado = 0; incrResultado < resultado.length; incrResultado++){
				if(this.aposta[numAposta] == resultado[incrResultado])
					acertos++;
			}
		}
		
		return acertos;
	}

		
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public int[] getAposta() {
		return aposta;
	}

	public void setAposta(int[] aposta) {
		this.aposta = aposta;
	}
}
