package br.com.projetoFluxoCaixa.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpSession;
import static java.lang.Integer.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import br.com.projetoFluxoCaixa.model.Lancamento;
import br.com.projetoFluxoCaixa.model.Usuario;
import br.com.projetoFluxoCaixa.repository.LancamentoRepository;
import br.com.projetoFluxoCaixa.utils.ParseDate;

@Controller
public class LancamentoController {

	private boolean controlador = false;
	private boolean controladorDatas = false;

	@Autowired
	LancamentoRepository lr;

	@RequestMapping("/programarLancamento")
	public String programarLancamento() {
		return "programar-lancamento";
	}

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

	@RequestMapping(value = "/programarLan", method = RequestMethod.POST)
	public String programarLan(@RequestParam("valor") Double valor, @RequestParam("descricao") String descricao,
			@RequestParam("data") String dataString, @RequestParam("operacao") String operacao,
			@RequestParam("mesFinal") String mesFinal, RedirectAttributes ra, HttpSession session)
			throws ParseException {

		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
		String formato = "yyyy-MM-dd";

		int diaGeral = parseInt(dataString.substring(8, 10));
		int mesInicio = parseInt(dataString.substring(5, 7));
		int anoInicio = parseInt(dataString.substring(0, 4));
		int mesFim = parseInt(mesFinal.substring(5, 7));
		int anoFim = parseInt(mesFinal.substring(0, 4));

		while (anoInicio < anoFim) {
			while (mesInicio <= 12) {
				String dtString = anoInicio + "-" + mesInicio + "-" + diaGeral;
				Date dtDate = new SimpleDateFormat(formato).parse(dtString);

				Lancamento lancamento = new Lancamento();
				lancamento.setData(dtDate);
				lancamento.setDescricao(descricao);
				lancamento.setOperacao(operacao);
				lancamento.setValor(valor);
				lancamento.setUsuario(usuario);
				lr.save(lancamento);

				mesInicio = mesInicio + 1;
			}
			mesInicio = 1;
			anoInicio = anoInicio + 1;
		}
		if (anoInicio == anoFim) {
			while (mesInicio <= mesFim) {
				String dtString = anoInicio + "-" + mesInicio + "-" + diaGeral;
				Date dtDate = new SimpleDateFormat(formato).parse(dtString);

				Lancamento lancamento = new Lancamento();
				lancamento.setData(dtDate);
				lancamento.setDescricao(descricao);
				lancamento.setOperacao(operacao);
				lancamento.setValor(valor);
				lancamento.setUsuario(usuario);
				lr.save(lancamento);

				mesInicio = mesInicio + 1;
			}
		}
		return "redirect:/menu";
	}

	@RequestMapping(value = "/salvarLan", method = RequestMethod.POST)
	public String salvarLan(@RequestParam("valor") Double valor, @RequestParam("descricao") String descricao,
			@RequestParam("data") String dataString, @RequestParam("operacao") String operacao, RedirectAttributes ra,
			HttpSession session, @RequestParam("idLancamento") Integer idLancamento) throws ParseException {

		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

		String formato = "yyyy-MM-dd";
		Date dataDate = new SimpleDateFormat(formato).parse(dataString);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Lancamento lancamento = new Lancamento();
		lancamento.setData(dataDate);
		lancamento.setDescricao(descricao);
		lancamento.setOperacao(operacao);
		lancamento.setValor(valor);
		lancamento.setUsuario(usuario);
		lancamento.setDataFormatada(sdf.format(dataDate));

		if (idLancamento != null) {
			lancamento.setIdLancamento(idLancamento);
		}

		lr.save(lancamento);
		return "redirect:/menu";

	}

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

		System.out.println(dtInicial);

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

	@RequestMapping("/editarLancamento/{idlan}")
	private String editarLancamento(HttpSession session, RedirectAttributes ra, @PathVariable("idlan") Integer idlan)
			throws ParseException {

		Optional<Lancamento> lancamento = lr.findById(idlan);

		System.out.println(lancamento.get().getData());
		System.out.println(lancamento.get().getDescricao());
		System.out.println(lancamento.get().getValor());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		System.out.println(sdf.format(lancamento.get().getData()));

		ra.addFlashAttribute("lancamento", lancamento.get());
		ra.addFlashAttribute("operacao", lancamento.get().getOperacao());
		ra.addFlashAttribute("dataFormatada", sdf.format(lancamento.get().getData()));

		return "redirect:/newLan";
	}

	@RequestMapping("/extratos")
	private String extratos(HttpSession session, RedirectAttributes ra) {

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

			return "extratos";
		}
	}

	@RequestMapping("/filtrarLancamentosImpressao")
	public String filtrarLancamentosImpressao(HttpSession session, @RequestParam("inicial") String dtInicial,
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

		return "redirect:/extratos";

	}

	@RequestMapping("/menuPrincipal")
	public String menuPrincipal(HttpSession session, RedirectAttributes ra) {

		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

		if (usuario == null) {
			ra.addFlashAttribute("mensagem", "É necessário logar para essa ação.");
			return "redirect:/login";
		} else {
			return "menu";
		}

	}

	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	public String menu(HttpSession session, RedirectAttributes ra) throws ParseException {

		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (usuario == null) {
			ra.addFlashAttribute("mensagem", "É necessário logar para essa ação.");
			return "redirect:/login";
		} else {

			if (session.getAttribute("controladorDatas") != null) {
				if (session.getAttribute("controladorDatas").equals(true)) {
					controladorDatas = true;
				}
			}

			if (controladorDatas) {

				String di = sdf.format(session.getAttribute("periodoInicial"));
				String df = sdf.format(session.getAttribute("periodoFinal"));

				String mesFiltro = di.substring(5, 7);
				String mesFiltroNome = "";
				String anoFiltro = di.substring(0, 4);

				switch (mesFiltro) {
				case "01":
					mesFiltroNome = "Janeiro";
					break;
				case "02":
					mesFiltroNome = "Fevereiro";
					break;
				case "03":
					mesFiltroNome = "Março";
					break;
				case "04":
					mesFiltroNome = "Abril";
					break;
				case "05":
					mesFiltroNome = "Maio";
					break;
				case "06":
					mesFiltroNome = "Junho";
					break;
				case "07":
					mesFiltroNome = "Julho";
					break;
				case "08":
					mesFiltroNome = "Agosto";
					break;
				case "09":
					mesFiltroNome = "Setembro";
					break;
				case "10":
					mesFiltroNome = "Outubro";
					break;
				case "11":
					mesFiltroNome = "Novembro";
					break;
				case "12":
					mesFiltroNome = "Dezembro";
					break;
				default:
					break;
				}

				Date periodoInicial = sdf.parse(di);
				Date periodoFinal = sdf.parse(df);

				List<Lancamento> lancamentos = lr.findLancamentosPorMes(periodoInicial, periodoFinal,
						usuario.getIdUsuario());
				Double saldoInicial = lr.findSaldoInicial(usuario.getIdUsuario(), periodoInicial);
				Double saldoFinal = (saldoInicial == null ? 0 : saldoInicial) + lr.findSaldoFinal(usuario.getIdUsuario(), periodoInicial, periodoFinal);
			    
				session.setAttribute("lancamentosMenu", lancamentos);
				session.setAttribute("controladorDatas", false);
				session.setAttribute("filtroMes", "Lançamentos do mês de " + mesFiltroNome + " de "+anoFiltro);
				session.setAttribute("saldoInicialMenu", "Saldo Inicial do Mês: R$"+(saldoInicial == null ? 0 : saldoInicial));
				session.setAttribute("saldoFinalMenu", "Saldo Final do Mês: R$"+(saldoFinal == null ? 0 : saldoFinal));
				return "redirect:/menuPrincipal";
			} else {
				Date periodoInicial = new ParseDate().getLimiteInicial(new Date(), Calendar.MONTH);
				Date periodoFinal = new ParseDate().getLimiteFinal(new Date(), Calendar.MONTH);

				List<Lancamento> lancamentos = lr.findLancamentosPorMes(periodoInicial, periodoFinal,
						usuario.getIdUsuario());
				Double saldoInicial = lr.findSaldoInicial(usuario.getIdUsuario(), periodoInicial);
				Double saldoFinal = (saldoInicial == null ? 0 : saldoInicial) + (lr.findSaldoFinal(usuario.getIdUsuario(), periodoInicial, periodoFinal) == null ? 
						0.0 : lr.findSaldoFinal(usuario.getIdUsuario(), periodoInicial, periodoFinal));
			    
				session.setAttribute("lancamentosMenu", lancamentos);
				session.setAttribute("filtroMes", "Lançamentos do mês corrente");
				session.setAttribute("saldoInicialMenu", "Saldo Inicial do Mês: R$"+(saldoInicial == null ? 0 : saldoInicial));
				session.setAttribute("saldoFinalMenu", "Saldo Final do Mês: R$"+(saldoFinal == null ? 0 : saldoFinal));
				return "redirect:/menuPrincipal";
			}

		}

	}

	@RequestMapping("/filtrarLanPorMes")
	private String filtrarLanPorMes(HttpSession session, RedirectAttributes ra, @RequestParam("mes") String mes,
			@RequestParam("ano") String ano) {

		Integer mesSolicitado = new Integer(mes);
		Integer anoSelecionado = new Integer(ano);

		ParseDate pd = new ParseDate();

		Calendar cal = new GregorianCalendar(anoSelecionado, mesSolicitado, 1);

		Date periodoInicial = pd.getLimiteInicial(cal.getTime(), Calendar.MONTH);
		Date periodoFinal = pd.getLimiteFinal(cal.getTime(), Calendar.MONTH);

		session.setAttribute("periodoInicial", periodoInicial);
		session.setAttribute("periodoFinal", periodoFinal);
		session.setAttribute("controladorDatas", true);

		return "redirect:/menu";
	}

	@RequestMapping("/imprimirExtratos")
	private String imprimir(HttpSession session, RedirectAttributes ra)
			throws DocumentException, IOException, ParseException {

		String dtInicial = session.getAttribute("inicial") == null ? null : session.getAttribute("inicial").toString();
		String dtFinal = session.getAttribute("final") == null ? null : session.getAttribute("final").toString();
		String operacao = session.getAttribute("operacao") == null ? null : session.getAttribute("operacao").toString();

		Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

		List<Lancamento> lancamentosFiltrados = (List<Lancamento>) session.getAttribute("lancamentosFiltrados");

		String nomeFile = "H:\\Meus Documentos\\Área de Trabalho\\Programação\\ExtratoLancamentos.pdf";

		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(nomeFile));

		document.open();

		Paragraph para = new Paragraph("Extrato dos Lançamentos");
		para.setAlignment(1);
		
		Paragraph para1 = new Paragraph(".");

		PdfPTable table = new PdfPTable(4);
		table.setPaddingTop(10);
		addTableHeader(table);

		for (Lancamento lancamento : lancamentosFiltrados) {
			addRows(table, lancamento);
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Double saldoInicial = lr.findSaldoInicial(usuario.getIdUsuario(), sdf.parse(dtInicial));
		Double saldoFinal = (saldoInicial == null ? 0 : saldoInicial) + (lr.findSaldoFinal(usuario.getIdUsuario(), sdf.parse(dtInicial), sdf.parse(dtFinal)) == null ? 
				0.0 : lr.findSaldoFinal(usuario.getIdUsuario(), sdf.parse(dtInicial), sdf.parse(dtFinal)));

		Paragraph saldoInicialPara = new Paragraph("Saldo Inicial: " + (saldoInicial == null ? 0 : saldoInicial));
		Paragraph saldoFinalPara = new Paragraph("Saldo Final: " + saldoFinal);

		document.add(para);
		document.add(para1);
		document.add(table);
		document.add(saldoInicialPara);
		document.add(saldoFinalPara);
		document.close();

		ra.addFlashAttribute("inicial", dtInicial);
		ra.addFlashAttribute("final", dtFinal);
		ra.addFlashAttribute("operacao", operacao);

		return "redirect:/extratos";
	}

	private void addTableHeader(PdfPTable table) {
		Stream.of("Data", "Descrição", "Valor", "Operação").forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(1);
			header.setPhrase(new Phrase(columnTitle));
			header.setHorizontalAlignment(1);
			table.addCell(header);
		});
	}

	private void addRows(PdfPTable table, Lancamento lancamento) {
		table.addCell(lancamento.getDataFormatada());
		table.addCell(lancamento.getDescricao());
		table.addCell(lancamento.getValor().toString());
		table.addCell(lancamento.getOperacao());
	}

}
