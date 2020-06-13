package br.com.projetoFluxoCaixa.controller;

import java.util.Date;
import java.util.List;

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


    /* Cadastrar um lançamento no banco
	
     */
    @RequestMapping(value = "/salvarLan", method = RequestMethod.POST)
    public String salvarLan(@RequestParam("valor") Double valor,
            @RequestParam("descricao") String descricao,
            @RequestParam("data") String data,
            //@RequestParam("operacao") String operacao,
            RedirectAttributes ra, HttpSession session) {
        Lancamento lancamento = new Lancamento();
        lancamento.setData(new Date());
        lancamento.setDescricao(descricao);
        lancamento.setOperacao("ENTRADA");
        lancamento.setValor(valor);
        
        Usuario usuario = (Usuario)session.getAttribute("usuarioLogado");
        
        lancamento.setUsuario(usuario);

        lr.save(lancamento);

        System.out.println(lancamento.getValor());

        List<Lancamento> lancamentoPesquisa = lr.findLancamentoPorUsuario(usuario.getIdUsuario());

        ra.addFlashAttribute("lan", lancamentoPesquisa);
        return "menu";

    }
}
