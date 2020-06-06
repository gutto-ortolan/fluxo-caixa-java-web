package br.com.projetoFluxoCaixa.model;

public enum EntradaSaida {

	E("ENTRADA"), S("SAIDA");
    
    private String operacao;  
      
    private EntradaSaida(String operacao){
       if(operacao.startsWith("E")) {
    	   this.operacao = "ENTRADA";
       }else {
    	   this.operacao = "SAIDA";
       }
    }
    
    
    @Override
    public String toString() {  
        return operacao;  
    }
}
