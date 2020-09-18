package com.gps.atuacao.business.dao;

import java.util.Collection;

import com.gps.atuacao.model.Defeito;

import br.com.gvt.jeemodelinfra.dao.GenericDAO;
import br.com.gvt.jeemodelinfra.exception.DAOException;

public interface DefeitoDao extends GenericDAO<Defeito, Long> {

	public Collection<Defeito> buscarDefeitos() throws DAOException;

}
