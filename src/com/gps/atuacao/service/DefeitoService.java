package com.gps.atuacao.service;
import java.io.InputStream;
import java.util.Collection;

import javax.ejb.Local;

import com.gps.atuacao.model.Defeito;
import com.gps.atuacao.model.Integrante;

import br.com.gvt.jeemodelinfra.exception.DAOException;

@Local
public interface DefeitoService {
	
	public String JNDI_NAME = "ejb/DefeitoServiceImpl";
	
	public void salvar(Defeito defeito);

	public Collection<Defeito> buscarDefeitos() throws DAOException;

	public Collection<Integrante> importarIntegrantes(InputStream inputStream) throws Exception;

	public void verificarAtuacao(String welcome, String parametros, Collection<Integrante> integrantes, Defeito defeito) throws Exception;

}
