package com.gps.atuacao.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.gps.atuacao.enums.Domain;
import com.gps.atuacao.model.Defeito;
import com.gps.atuacao.model.Integrante;
import com.gps.atuacao.service.DefeitoService;

@SessionScoped
@ManagedBean(name = "uploadArquivoBean")
public class UploadArquivoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String MENSAGEM_SUCESSO = "Operação realizada com sucesso!";
	public static String WELCOME = "http://10.41.252.111:9010/controle-demanda/defectsComments.jsf?";
	public static String DOMINIO_QA = "dominio=QA_QA";
	public static String DOMINIO_DEV = "dominio=DEV";
	public static String PROJECT = "&project=";
	public static String DB_NAME = "&dbname=";
	public static String DEFECT_ID = "&defectId=";
	public static String ANO = "2020_";
	public static String PROJECT_QA = "&projectQA=";
	
	@EJB
	private DefeitoService defeitoService;
	private Part arquivo;
	private Collection<Defeito> defeitos;
	private Collection<Integrante> integrantes;
	private UploadedFile fileUpload;
	private StreamedContent fileDownload;

	public void importarIntegrantes(final FileUploadEvent event) {
		
		try {
			this.fileUpload = event.getFile();
			this.integrantes = this.defeitoService.importarIntegrantes(event.getFile().getInputstream());
			if (CollectionUtils.isNotEmpty(this.integrantes)) {
				this.buscarDefeitos();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso!", MENSAGEM_SUCESSO));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Não foi possível encontrar os dados informados na Planilha!", MENSAGEM_SUCESSO));
			}
			
		} catch (final Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", e.getMessage()));
		}
	}
	
	public void buscarDefeitos() throws Exception {
		
		this.defeitos = this.defeitoService.buscarDefeitos();
		if(CollectionUtils.isNotEmpty(this.integrantes) && CollectionUtils.isNotEmpty(this.defeitos)) {
			
			this.verificarAtuacao();
		}
	}
	
	public void verificarAtuacao() throws Exception {
		
		for (Defeito defeito : this.defeitos) {	
			
			String parametros = this.dePara(defeito);
			if (parametros != null) {
				System.out.println(defeito.getBug().concat(" ").concat(defeito.getDomain()));
				this.defeitoService.verificarAtuacao(WELCOME, parametros, integrantes, defeito);
			}
		}
	}

	private String dePara(Defeito defeito) {
		
		if(defeito != null) {
			
			if (ANO.concat(Domain.PJ_ATENDIMENTO_B2C.name()).equals(defeito.getDomain())) {
				
				return DOMINIO_QA.concat(PROJECT.concat(defeito.getProjeto()).concat(DB_NAME.concat(Domain.PJ_ATENDIMENTO_B2C.getValor()).concat(DEFECT_ID.concat(defeito.getBug()).concat(PROJECT_QA.concat(defeito.getDomain())))));
			} else if (ANO.concat(Domain.PJ_DIGITAL.name()).equals(defeito.getDomain())) {
				
				return DOMINIO_QA.concat(PROJECT.concat(defeito.getProjeto()).concat(DB_NAME.concat(Domain.PJ_DIGITAL.getValor()).concat(DEFECT_ID.concat(defeito.getBug()).concat(PROJECT_QA.concat(defeito.getDomain())))));
			} else if (ANO.concat(Domain.PJ_EST_ONGOING_B2C.name()).equals(defeito.getDomain())) {
				
				return DOMINIO_QA.concat(PROJECT.concat(defeito.getProjeto()).concat(DB_NAME.concat(Domain.PJ_EST_ONGOING_B2C.getValor()).concat(DEFECT_ID.concat(defeito.getBug()).concat(PROJECT_QA.concat(defeito.getDomain())))));
			} else if (ANO.concat(Domain.PJ_OSS_B2C_FIXA.name()).equals(defeito.getDomain())) {
				
				return DOMINIO_QA.concat(PROJECT.concat(defeito.getProjeto()).concat(DB_NAME.concat(Domain.PJ_OSS_B2C_FIXA.getValor()).concat(DEFECT_ID.concat(defeito.getBug()).concat(PROJECT_QA.concat(defeito.getDomain())))));
			} else if (ANO.concat(Domain.PJ_TRANSF_B2C.name()).equals(defeito.getDomain())) {
				
				return DOMINIO_QA.concat(PROJECT.concat(defeito.getProjeto()).concat(DB_NAME.concat(Domain.PJ_TRANSF_B2C.getValor()).concat(DEFECT_ID.concat(defeito.getBug()).concat(PROJECT_QA.concat(defeito.getDomain())))));
			} else if (ANO.concat(Domain.PP_FIXA_MOVEL.name()).equals(defeito.getDomain())) {
				
				return DOMINIO_QA.concat(PROJECT.concat(defeito.getProjeto()).concat(DB_NAME.concat(Domain.PP_FIXA_MOVEL.getValor()).concat(DEFECT_ID.concat(defeito.getBug()).concat(PROJECT_QA.concat(defeito.getDomain())))));
			} else if (ANO.concat(Domain.REGRESSAO.name()).equals(defeito.getDomain())) {
				
				return DOMINIO_QA.concat(PROJECT.concat(defeito.getProjeto()).concat(DB_NAME.concat(Domain.REGRESSAO.getValor()).concat(DEFECT_ID.concat(defeito.getBug()).concat(PROJECT_QA.concat(defeito.getDomain())))));
			} else if (ANO.concat(Domain.SUSTENTACAO.name()).equals(defeito.getDomain())) {
				
				return DOMINIO_QA.concat(PROJECT.concat(defeito.getProjeto()).concat(DB_NAME.concat(Domain.SUSTENTACAO.getValor()).concat(DEFECT_ID.concat(defeito.getBug()).concat(PROJECT_QA.concat(defeito.getDomain())))));
			} else if (ANO.concat(Domain.PJ_OG_B2B_FINANCAS.name()).equals(defeito.getDomain())) {
				
				return DOMINIO_QA.concat(PROJECT.concat(defeito.getProjeto()).concat(DB_NAME.concat(Domain.PJ_OG_B2B_FINANCAS.getValor()).concat(DEFECT_ID.concat(defeito.getBug()).concat(PROJECT_QA.concat(defeito.getDomain())))));
			} else if (ANO.concat(Domain.PJ_TRANSF_B2B_APOIO.name()).equals(defeito.getDomain())) {
			
				return DOMINIO_QA.concat(PROJECT.concat(defeito.getProjeto()).concat(DB_NAME.concat(Domain.PJ_TRANSF_B2B_APOIO.getValor()).concat(DEFECT_ID.concat(defeito.getBug()).concat(PROJECT_QA.concat(defeito.getDomain())))));
			} else if (Domain.AMDOCS_E2E.name().equals(defeito.getDomain())) {
			
				return DOMINIO_DEV.concat(PROJECT.concat(defeito.getProjeto()).concat(DB_NAME.concat(Domain.AMDOCS_E2E.getValor()).concat(DEFECT_ID.concat(defeito.getBug()).concat(PROJECT_QA.concat(defeito.getDomain())))));
			}
		}
		return null;
	}

	public void limpar() {

		this.fileUpload = null;
		this.fileDownload = null;
	}

	public Part getArquivo() {
		return arquivo;
	}

	public void setArquivo(Part arquivo) {
		this.arquivo = arquivo;
	}

	public byte[] toByteArrayUsingJava(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int reads = is.read();
		while (reads != -1) {
			baos.write(reads);
			reads = is.read();
		}
		return baos.toByteArray();
	}

	public UploadedFile getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(UploadedFile fileUpload) {
		this.fileUpload = fileUpload;
	}

	public StreamedContent getFileDownload() {
		return fileDownload;
	}

	public void setFileDownload(StreamedContent fileDownload) {
		this.fileDownload = fileDownload;
	}

	public Collection<Defeito> getDefeitos() {
		return defeitos;
	}

	public void setDefeitos(Collection<Defeito> defeitos) {
		this.defeitos = defeitos;
	}

	public Collection<Integrante> getIntegrantes() {
		return integrantes;
	}

	public void setIntegrantes(Collection<Integrante> integrantes) {
		this.integrantes = integrantes;
	}

}
