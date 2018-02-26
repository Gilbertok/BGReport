package com.bograntex.relatorio;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bograntex.model.ReportObject;
import com.bograntex.relatorio.calc.CalcMetas;
import com.bograntex.relatorio.calc.estoque.CalcEstoqueFios;
import com.bograntex.relatorio.calc.estoque.CalcEstoqueMalhaCrua;
import com.bograntex.relatorio.calc.estoque.CalcEstoqueMalhaTinta;
import com.bograntex.relatorio.calc.estoque.CalcEstoqueTecidoPlano;
import com.bograntex.relatorio.calc.estoque.CalcEstoqueTinturaria;
import com.bograntex.relatorio.calc.estoque.CalculaEstoqueTotal;
import com.bograntex.relatorio.calc.producao.CalcProducaoCorte;
import com.bograntex.relatorio.calc.producao.CalcProducaoCostura;
import com.bograntex.relatorio.calc.producao.CalcProducaoDobracao;
import com.bograntex.relatorio.calc.producao.CalcProducaoExpedicao;
import com.bograntex.relatorio.calc.producao.CalcProducaoMalharia;
import com.bograntex.relatorio.calc.producao.CalcProducaoRevisao;
import com.bograntex.relatorio.calc.producao.CalcRetornoEstamparia;
import com.bograntex.utils.DateUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class SigiRelatorio {
	
	private String pathToReportPackage;
	private String pathToReportPackageReuniao;
	private String pathToReportPackageSetor;
	private String pathToReportPackageSetor2;
	
	private InputStream pathToReportStream;
	private InputStream pathToReportReuniaoStream;
	private InputStream pathToReportSetorStream;
	private InputStream pathToReportSetor2Stream;
	private int current = 0;
    private String statusMessage;
	
	public SigiRelatorio() {
		if (this.isJar()) {
			this.pathToReportStream = this.getClass().getResourceAsStream("/reports/SigiReport.jrxml");
			this.pathToReportReuniaoStream = this.getClass().getResourceAsStream("/reports/SigiReuniaoReport.jrxml");
			this.pathToReportSetorStream = this.getClass().getResourceAsStream("/reports/SigiSetoresReport.jrxml");
			this.pathToReportSetor2Stream = this.getClass().getResourceAsStream("/reports/SigiSetoresReport2.jrxml");
			System.out.println(this.getPathToReportStream());
		} else {
			this.pathToReportPackage = "resources/reports/SigiReport.jrxml";
			this.pathToReportPackageReuniao = "resources/reports/SigiReuniaoReport.jrxml";
			this.pathToReportPackageSetor = "resources/reports/SigiSetoresReport.jrxml";
			this.pathToReportPackageSetor2 = "resources/reports/SigiSetoresReport2.jrxml";
			System.out.println(this.getPathToReportPackage());
		}
	}
	
	public Boolean isJar() {
		String path = this.getClass().getClassLoader().getResource("").getPath();
		System.out.println(path);
		if (!path.contains("bin/")) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getCurrent() {
        return current;
    }
	
	public void setCurrent(int current) {
        this.current = current;
    }
	
	public String getMessage() {
        return statusMessage;
    }
	
	public void setMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
	
	public Map<String, Object> getReportParameter() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		this.setMessage(" --- Verificando Parâmetros --- ");
		this.setCurrent(1);
		Date dataRelatorio = new Date();
		DateUtils data = new DateUtils(dataRelatorio);
		parameters.put("dataRelatorio", data.getDia()+" "+data.getMes() +", "+data.getAno());
		parameters.put("mesRelatorio", data.getMes()+"/"+data.getAno());
		parameters.put("diasRelatorio", data.getWorkingDays());
		parameters.put("diasRelatorioMes", data.getWorkingDaysMonth());
		return parameters;
	}
	
	private List<Object> findReportData() {
		List<Object> reports = new LinkedList<Object>();
		reports.add((Object) new ReportObject());
		return reports;
	}
	
	public JRBeanCollectionDataSource createDataSouce() {
		JRBeanCollectionDataSource dataSouce = new JRBeanCollectionDataSource(this.findReportData());
		return dataSouce;
	}
	
	public JasperPrint createReport(Map<String, Object> parameters) {
		JasperPrint jasperPrint = null;
		try {
			JasperReport jasperReport = JasperCompileManager.compileReport(this.getReportSigi());
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, createDataSouce());
		} catch (JRException e) {
			e.printStackTrace();
		}
		return jasperPrint;
	}
	
	public JasperPrint createReportReuniao(Map<String, Object> parameters) {
		JasperPrint jasperPrint = null;
		try {
			JasperReport jasperReport = JasperCompileManager.compileReport(this.getReportReuniao());
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, createDataSouce());
		} catch (JRException e) {
			e.printStackTrace();
		}
		return jasperPrint;
	}
	
	public JasperPrint createReportSetor(Map<String, Object> parameters) {
		JasperPrint jasperPrint = null;
		try {
			JasperReport jasperReport = JasperCompileManager.compileReport(this.getReportSetor());
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, createDataSouce());
		} catch (JRException e) {
			e.printStackTrace();
		}
		return jasperPrint;
	}
	
	public JasperPrint createReportSetor2(Map<String, Object> parameters) {
		JasperPrint jasperPrint = null;
		try {
			JasperReport jasperReport = JasperCompileManager.compileReport(this.getReportSetor2());
			jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, createDataSouce());
		} catch (JRException e) {
			e.printStackTrace();
		}
		return jasperPrint;
	}

	public Map<String, Object> getCalcRelatorioEstoque(Map<String, Object> paramsRelatorio) {
		try {
			switch (this.getCurrent()) {
			case 1:
				this.setMessage(" --- Calculando Estoque --- ");
				this.setMessage("Calculando Estoque Tinturaria...");
				this.setCurrent(5);
				paramsRelatorio.put("estoqueTinturaria", CalcEstoqueTinturaria.calcularToString(1));
				break;
				
			case 5:
				this.setMessage("Calculando Estoque Malha tinta...");
				this.setCurrent(10);
				paramsRelatorio.put("estoqueMalhaTinta", CalcEstoqueMalhaTinta.calcularToString());
				break;
				
			case 10:
				this.setMessage("Calculando Estoque Tecido Plano...");
				this.setCurrent(15);
				paramsRelatorio.put("estoqueTecidoPlano", CalcEstoqueTecidoPlano.calcularToString());
				break;
			
			case 15:
				this.setMessage("Calculando Estoque Tecido Plano Terceiros...");
				this.setCurrent(20);
				paramsRelatorio.put("estoqueTecidoPlanoTerc", CalcEstoqueTinturaria.calcularToString(2));
				break;
				
			case 20:
				this.setMessage("Calculando Estoque Fios Crú...");
				this.setCurrent(25);
				paramsRelatorio.put("estoqueFiosCru", CalcEstoqueFios.calcularToString(1));
				break;
				
			case 25:
				this.setMessage("Calculando Estoque Fios Tinto...");
				this.setCurrent(30);
				paramsRelatorio.put("estoqueFiosTinto", CalcEstoqueFios.calcularToString(2));
				break;
				
			case 30:
				this.setMessage("Calculando Estoque Malha Crua...");
				this.setCurrent(35);
				paramsRelatorio.put("estoqueMalhaCrua", CalcEstoqueMalhaCrua.calcularToString());
				break;
				
			case 35:
				this.setMessage("Calculando Estoque Total...");
				this.setCurrent(40);
				paramsRelatorio.put("estoqueTotal", CalculaEstoqueTotal.calcularToString(paramsRelatorio));
				break;
			
			case 40:
				this.setMessage("Calculando Metas...");
				this.setCurrent(45);
				paramsRelatorio = CalcMetas.calcular(paramsRelatorio);
				break;
				
			case 45:
				this.setMessage("Calculando Produção Malharia...");
				this.setCurrent(50);
				paramsRelatorio = CalcProducaoMalharia.calcular(paramsRelatorio);
				break;
			
			case 50:
				this.setMessage("Calculando Produção Corte...");
				this.setCurrent(55);
				paramsRelatorio = CalcProducaoCorte.calcular(paramsRelatorio);
				break;
				
			case 55:
				this.setMessage("Calculando Retorno Estamparia/Bordados...");
				this.setCurrent(60);
				paramsRelatorio = CalcRetornoEstamparia.calcular(paramsRelatorio);
				break;
				
			case 60:
				this.setMessage("Calculando Produção Facção...");
				this.setCurrent(65);
				paramsRelatorio = CalcProducaoCostura.calcular(paramsRelatorio);
				break;
				
			case 65:
				this.setMessage("Calculando Produção Facção Interna...");
				this.setCurrent(70);
				paramsRelatorio = CalcProducaoCostura.calcularInterno(paramsRelatorio);
				break;
				
			case 70:
				this.setMessage("Calculando Produção Revisão...");
				this.setCurrent(75);
				paramsRelatorio = CalcProducaoRevisao.calcular(paramsRelatorio);
				break;
				
			case 75:
				this.setMessage("Calculando Produção Dobração...");
				this.setCurrent(80);
				paramsRelatorio = CalcProducaoDobracao.calcular(paramsRelatorio);
				break;
				
			case 80:
				this.setMessage("Calculando Produção Expedição...");
				this.setCurrent(85);
				paramsRelatorio = CalcProducaoExpedicao.calcular(paramsRelatorio);
				break;
	
			default:
				this.setMessage("Finalizando Calculos...");
				this.setCurrent(100);
				Thread.sleep(1000);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramsRelatorio;
	}


	public InputStream getPathToReportStream() {
		return pathToReportStream;
	}


	public void setPathToReportStream(InputStream pathToReportStream) {
		this.pathToReportStream = pathToReportStream;
	}


	public InputStream getPathToReportReuniaoStream() {
		return pathToReportReuniaoStream;
	}


	public void setPathToReportReuniaoStream(InputStream pathToReportReuniaoStream) {
		this.pathToReportReuniaoStream = pathToReportReuniaoStream;
	}


	public InputStream getPathToReportSetorStream() {
		return pathToReportSetorStream;
	}


	public void setPathToReportSetorStream(InputStream pathToReportSetorStream) {
		this.pathToReportSetorStream = pathToReportSetorStream;
	}


	public InputStream getPathToReportSetor2Stream() {
		return pathToReportSetor2Stream;
	}


	public void setPathToReportSetor2Stream(InputStream pathToReportSetor2Stream) {
		this.pathToReportSetor2Stream = pathToReportSetor2Stream;
	}


	public String getPathToReportPackage() {
		return pathToReportPackage;
	}


	public void setPathToReportPackage(String pathToReportPackage) {
		this.pathToReportPackage = pathToReportPackage;
	}


	public String getPathToReportPackageReuniao() {
		return pathToReportPackageReuniao;
	}


	public void setPathToReportPackageReuniao(String pathToReportPackageReuniao) {
		this.pathToReportPackageReuniao = pathToReportPackageReuniao;
	}


	public String getPathToReportPackageSetor() {
		return pathToReportPackageSetor;
	}


	public void setPathToReportPackageSetor(String pathToReportPackageSetor) {
		this.pathToReportPackageSetor = pathToReportPackageSetor;
	}


	public String getPathToReportPackageSetor2() {
		return pathToReportPackageSetor2;
	}


	public void setPathToReportPackageSetor2(String pathToReportPackageSetor2) {
		this.pathToReportPackageSetor2 = pathToReportPackageSetor2;
	}
	
	private JasperDesign getReportSigi() throws JRException {
		if (this.isJar()) {
			return JRXmlLoader.load(this.getPathToReportStream());
		} else {
			return JRXmlLoader.load(this.getPathToReportPackage());
		}
	}
	
	private JasperDesign getReportReuniao() throws JRException {
		if (this.isJar()) {
			return JRXmlLoader.load(this.getPathToReportReuniaoStream());
		} else {
			return JRXmlLoader.load(this.getPathToReportPackageReuniao());
		}
	}
	
	private JasperDesign getReportSetor() throws JRException {
		if (this.isJar()) {
			return JRXmlLoader.load(this.getPathToReportSetorStream());
		} else {
			return JRXmlLoader.load(this.getPathToReportPackageSetor());
		}
	}
	
	private JasperDesign getReportSetor2() throws JRException {
		if (this.isJar()) {
			return JRXmlLoader.load(this.getPathToReportSetor2Stream());
		} else {
			return JRXmlLoader.load(this.getPathToReportPackageSetor2());
		}
	}

}
