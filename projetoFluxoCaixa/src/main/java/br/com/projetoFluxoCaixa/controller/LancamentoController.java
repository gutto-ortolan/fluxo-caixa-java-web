package br.com.projetoFluxoCaixa.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.projetoFluxoCaixa.model.Lancamento;
import br.com.projetoFluxoCaixa.model.Usuario;
import br.com.projetoFluxoCaixa.repository.LancamentoRepository;

@Controller
public class LancamentoController {

	private boolean controlador = false;
	
	@Autowired
	LancamentoRepository lr;

	@RequestMapping("/newLan")
	public String newLan(HttpSession session, RedirectAttributes ra) {

		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

		if (usuario == null) {
			ra.addFlashAttribute("mensagem", "É necessário logar para essa ação.");
			return "redirect:/login";
		} else {
			return "newLan";
		}

	}

	@RequestMapping(value = "/salvarLan", method = RequestMethod.POST)
	public String salvarLan(@RequestParam("valor") Double valor, @RequestParam("descricao") String descricao,
			@RequestParam("data") String dataString, @RequestParam("operacao") String operacao, RedirectAttributes ra,
			HttpSession session) throws ParseException {

		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

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
	 * Falha ao coletar dados do menu.html - metodo chamado para filtrar por mes e
	 * ano escolhido
	 * 
	 * @RequestMapping(value = "/filtrarLanPorMes", method = RequestMethod.POST)
	 * public String filtrarLanPorMes(@RequestParam("saldoFinal") String saldoFinal,
	 * 
	 * @RequestParam("saldoInicial") String saldoInicial,
	 * 
	 * @RequestParam("mes") String mes,
	 * 
	 * @RequestParam("ano") String ano, RedirectAttributes ra, HttpSession session)
	 * {
	 * 
	 * System.out.println("teste filtro"); return "newLan";
	 * 
	 * 
	 * }
	 */

	@RequestMapping("/manutencao-lancamento")
	public String manutencaoLancamento(HttpSession session, RedirectAttributes ra) {
		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

		if (usuario == null) {
			ra.addFlashAttribute("mensagem", "É necessário logar para essa ação.");
			return "redirect:/login";
		} else {

			List<Lancamento> lancamentosFiltrados = (List<Lancamento>) session.getAttribute("lancamentosFiltrados");

			if (session.getAttribute("controlador") != null) {
				if (session.getAttribute("controlador").equals(true)) {
					controlador = true;
				}
			}

			if (controlador) {
				session.setAttribute("lancamentos", lancamentosFiltrados);
				session.setAttribute("controlador", false);
			} else {
				session.setAttribute("lancamentos", null);
			}

			return "manutencao-lancamento";
		}

	}

	@GetMapping("/excluirLancamento/{idlan}")
	public String excluirLancamento(HttpSession session, RedirectAttributes ra, @PathVariable("idlan") Integer idlan)
			throws ParseException {
		lr.deleteById(idlan);

		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String dtInicial = session.getAttribute("inicial") == null ? null : session.getAttribute("inicial").toString();
		String dtFinal = session.getAttribute("final") == null ? null : session.getAttribute("final").toString();
		String operacao = session.getAttribute("operacao") == null ? null : session.getAttribute("operacao").toString();

		List<Lancamento> listLancamentos = new ArrayList();
		if (!operacao.equals("AMBOS")) {
			listLancamentos = lr.findLancamentosPorPeriodoComOperacao(sdf.parse(dtInicial), sdf.parse(dtFinal),
					usuario.getIdUsuario(), operacao);
			session.setAttribute("lancamentosFiltrados", listLancamentos);
			session.setAttribute("controlador", true);
		} else {
			listLancamentos = lr.findLancamentosPorPeriodoSemOperacao(sdf.parse(dtInicial), sdf.parse(dtFinal),
					usuario.getIdUsuario());
			session.setAttribute("lancamentosFiltrados", listLancamentos);
			session.setAttribute("controlador", true);
		}

		ra.addFlashAttribute("inicial", dtInicial);
		ra.addFlashAttribute("final", dtFinal);
		ra.addFlashAttribute("operacao", operacao);

		return "redirect:/manutencao-lancamento";
	}

	@RequestMapping("/filtrarLancamentos")
	public String filtrarLancamentos(HttpSession session, @RequestParam("inicial") String dtInicial,
			@RequestParam("final") String dtFinal, @RequestParam("operacao") String operacao, RedirectAttributes ra)
			throws ParseException {

		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		List<Lancamento> listLancamentos = new ArrayList();
		if (!operacao.equals("AMBOS")) {
			listLancamentos = lr.findLancamentosPorPeriodoComOperacao(sdf.parse(dtInicial), sdf.parse(dtFinal),
					usuario.getIdUsuario(), operacao);
			session.setAttribute("lancamentosFiltrados", listLancamentos);
			session.setAttribute("controlador", true);
			session.setAttribute("inicial", dtInicial);
			session.setAttribute("final", dtFinal);
			session.setAttribute("operacao", operacao);
		} else {
			listLancamentos = lr.findLancamentosPorPeriodoSemOperacao(sdf.parse(dtInicial), sdf.parse(dtFinal),
					usuario.getIdUsuario());
			session.setAttribute("lancamentosFiltrados", listLancamentos);
			session.setAttribute("controlador", true);
			session.setAttribute("inicial", dtInicial);
			session.setAttribute("final", dtFinal);
			session.setAttribute("operacao", operacao);
		}

		ra.addFlashAttribute("inicial", dtInicial);
		ra.addFlashAttribute("final", dtFinal);
		ra.addFlashAttribute("operacao", operacao);

		return "redirect:/manutencao-lancamento";

	}
}
