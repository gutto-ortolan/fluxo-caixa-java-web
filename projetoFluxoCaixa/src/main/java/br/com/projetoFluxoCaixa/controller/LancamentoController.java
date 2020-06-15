package br.com.projetoFluxoCaixa.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.projetoFluxoCaixa.model.Lancamento;
import br.com.projetoFluxoCaixa.model.Usuario;
import br.com.projetoFluxoCaixa.repository.LancamentoRepository;

@Controller
public class LancamentoController {

    @Autowired
    LancamentoRepository lr;

    @RequestMapping("/newLan")
    public String newLan(HttpSession session, RedirectAttributes ra) {
        return "newLan";
    }
    
    @RequestMapping(value = "/salvarLan", method = RequestMethod.POST)
    public String salvarLan(@RequestParam("valor") Double valor,
            @RequestParam("descricao") String descricao,
            @RequestParam("data") String dataString,
            @RequestParam("operacao") String operacao,
            RedirectAttributes ra, HttpSession session) throws ParseException {
    	
    	
        Usuario usuario = (Usuario)session.getAttribute("usuarioLogado");

    	String formato = "yyyy-MM-dd";
    	Date dataDate = new SimpleDateFormat(formato).parse(dataString);    	

        Lancamento lancamento = new Lancamento();
        lancamento.setData(dataDate);
        lancamento.setDescricao(descricao);
        lancamento.setOperacao(operacao);
        lancamento.setValor(valor);
        lancamento.setUsuario(usuario);

        lr.save(lancamento);        
        return "redirect:/menu";

    }
    /*
     * Falha ao coletar dados do menu.html - metodo chamado para filtrar por mes e ano escolhido
     
    @RequestMapping(value = "/filtrarLanPorMes", method = RequestMethod.POST)
    public String filtrarLanPorMes(@RequestParam("saldoFinal") String saldoFinal,
            @RequestParam("saldoInicial") String saldoInicial,
            @RequestParam("mes") String mes,
            @RequestParam("ano") String ano,
            RedirectAttributes ra, HttpSession session) {
    	
    	System.out.println("teste filtro");
    	return "newLan";
    	    	
    	
    }*/
    
}
