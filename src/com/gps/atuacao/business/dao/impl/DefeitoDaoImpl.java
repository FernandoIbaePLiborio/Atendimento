package com.gps.atuacao.business.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.gps.atuacao.business.dao.DefeitoDao;
import com.gps.atuacao.model.Defeito;

import br.com.gvt.jeemodelinfra.dao.AbstractGenericDAO;
import br.com.gvt.jeemodelinfra.exception.DAOException;

@Stateless
public class DefeitoDaoImpl extends AbstractGenericDAO<Defeito, Long> implements DefeitoDao {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public EntityManager getEntityManager() {
		
		return this.em;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Defeito> buscarDefeitos() throws DAOException {
		try {
			
			final StringBuilder jpql = new StringBuilder();
			jpql.append(" SELECT bugid AS DEFEITO, PROJECTS AS DOMINIO, ID_PROJETO, PROJETO, DIRETORIA, PACOTE, BUG_STATUS AS STATUS, ROOT_CAUSE, RESPONSIBLE_GROUP AS RESPONSÁVEL, ");
			jpql.append(" environment_type AS ESTEIRA, SISTEMA_ORIGEM, SISTEMA_CORRECAO, VENDOR AS EQUIPE, RDM AS CRQ, REOPENED, FIXING, DESENVOLVIMENTO, FUNCIONAL, ");
			jpql.append(" data_criacao, data_fechamento, CONT_TESTE, cont_desenvolvimento, cont_funcional, reopen, responsavel_correcao ");
			jpql.append(" FROM stg_defeitos ");
			jpql.append(" WHERE data_fechamento >= '2020-05-01' ");
			jpql.append(" AND (SISTEMA_ORIGEM LIKE '%GPS%' OR SISTEMA_CORRECAO LIKE '%GPS%' OR SISTEMA_ORIGEM LIKE '%FIXA%' OR SISTEMA_CORRECAO LIKE '%FIXA%' OR SISTEMA_ORIGEM LIKE '%URA%' OR SISTEMA_CORRECAO LIKE '%URA%' OR VENDOR LIKE '%TELEFONICA%')" );
			jpql.append(" AND BUG_STATUS IN('Closed', 'Not Fixed', 'Rejected') ORDER BY bugid DESC ");
			final Query query = this.em.createNativeQuery(jpql.toString());

			final List<Object[]> resultado = query.getResultList();
			final Collection<Defeito> defeitos = new ArrayList<Defeito>();

			for (int i = 0; i <= (resultado.size() - 1); i++) {
				final Object[] object = resultado.get(i);
				final Defeito defeito = new Defeito();
				defeito.setBug((String) object[0]);
				defeito.setDomain((String) object[1]);
				defeito.setProjeto((String) object[2]);
				defeitos.add(defeito);
			}
			return defeitos;
		} catch (final Exception e) {
			throw new DAOException(e);
		}
	}

	@Override
	protected Class<Defeito> getEntityClass() {
		
		return Defeito.class;
	}

}
