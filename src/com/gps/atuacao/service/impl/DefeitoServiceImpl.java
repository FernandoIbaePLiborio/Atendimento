package com.gps.atuacao.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gps.atuacao.business.dao.DefeitoDao;
import com.gps.atuacao.enums.Equipe;
import com.gps.atuacao.enums.TipoAtuacao;
import com.gps.atuacao.model.Defeito;
import com.gps.atuacao.model.Integrante;
import com.gps.atuacao.service.DefeitoService;
import com.gps.atuacao.service.request.Requisicao;

import br.com.gvt.jeemodelinfra.exception.DAOException;

/**
 * @author A0095499
 *
 */
@Stateless(name = DefeitoService.JNDI_NAME, mappedName = DefeitoService.JNDI_NAME)
public class DefeitoServiceImpl implements DefeitoService {
	
	@EJB
	private DefeitoDao defeitoDao;

    private Requisicao requisicaoService;

	@Override
	public void salvar(Defeito defeito) {
		return;
	}

	@Override
	public List<Integrante> importarIntegrantes(final InputStream inputStream) throws Exception {
		try {
			List<Integrante> integrantes = new ArrayList<Integrante>();
			final OPCPackage pkg = OPCPackage.open(inputStream);
			final XSSFWorkbook wb = new XSSFWorkbook(pkg);	
			final Sheet sheetGPS = wb.getSheetAt(0);
			final Sheet sheetURA = wb.getSheetAt(1);
			for (int i = 1; i < sheetGPS.getPhysicalNumberOfRows(); i++) {
				
				final Row row = sheetGPS.getRow(i);
				if (!this.isRowEmpty(row)) {
					
					if (!Objects.isNull(row.getCell(0)) && StringUtils.isNotEmpty(row.getCell(0).getStringCellValue())) {
						Integrante integrante = new Integrante();
						
						integrante.setName(row.getCell(0).getStringCellValue());
						integrante.setType(TipoAtuacao.DESENVOLVIMENTO_GPS.getValor());
						integrantes.add(integrante);
					}
					if (!Objects.isNull(row.getCell(1)) && StringUtils.isNotEmpty(row.getCell(1).getStringCellValue())) {
						Integrante integrante = new Integrante();
						
						integrante.setName(row.getCell(1).getStringCellValue());
						integrante.setType(TipoAtuacao.FUNCIONAL.getValor());
						integrantes.add(integrante);
					}
				}
			}
			for (int i = 1; i < sheetURA.getPhysicalNumberOfRows(); i++) {
				
				final Row row = sheetURA.getRow(i);
				if (!this.isRowEmpty(row)) {
					
					if (!Objects.isNull(row.getCell(0)) && StringUtils.isNotEmpty(row.getCell(0).getStringCellValue())) {
						Integrante integrante = new Integrante();
						
						integrante.setName(row.getCell(0).getStringCellValue());
						integrante.setType(TipoAtuacao.DESENVOLVIMENTO_URA.getValor());
						integrantes.add(integrante);
					}
				}
			}
			wb.close();
			return integrantes;
		} catch (IOException | InvalidFormatException e) {
			throw new EJBException(e);
		} 
	}

	@Override
	public Collection<Defeito> buscarDefeitos() throws DAOException {

		return defeitoDao.buscarDefeitos();
	}

	@SuppressWarnings("static-access")
	@Override
	public void verificarAtuacao(String welcome, String parametros, Collection<Integrante> integrantes, Defeito defeito) throws Exception {
		
		this.excutePost(welcome, parametros, integrantes, defeito);
	}
	
	/**
	 * @param targetURL
	 * @param urlParameters
	 * @param integrantes
	 * @param defeito
	 * @return
	 * @throws Exception
	 */
	public static Defeito excutePost(String targetURL, String urlParameters, Collection<Integrante> integrantes, Defeito defeito) throws Exception {
		URL url;
		HttpURLConnection connection = null;
		try {
			
			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			List<Integrante> atuantes = new ArrayList<>();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
				response.append('\r');
			}
			String res = response.toString();
			for (Integrante integrante : integrantes) {
				
				if (res.toUpperCase().contains(integrante.getName().toUpperCase())) {
					atuantes.add(integrante);
					System.out.println(integrante.getName());
				}
			}
			if (CollectionUtils.isNotEmpty(atuantes)) {
				
				if (!atuantes.stream().filter(i -> TipoAtuacao.DESENVOLVIMENTO_GPS.getValor().equals(i.getType())).collect(Collectors.toList()).isEmpty()
						&& !atuantes.stream().filter(i -> TipoAtuacao.FUNCIONAL.getValor().equals(i.getType())).collect(Collectors.toList()).isEmpty()) {
					defeito.setAtuacao(TipoAtuacao.AMBOS.getValor());
					defeito.setEquipe(Equipe.GPS.getValor());
				} else if ((!atuantes.stream().filter(i -> TipoAtuacao.DESENVOLVIMENTO_GPS.getValor().equals(i.getType())).collect(Collectors.toList()).isEmpty() 
						|| !atuantes.stream().filter(i -> TipoAtuacao.FUNCIONAL.getValor().equals(i.getType())).collect(Collectors.toList()).isEmpty())
						&& !atuantes.stream().filter(i -> TipoAtuacao.DESENVOLVIMENTO_URA.getValor().equals(i.getType())).collect(Collectors.toList()).isEmpty()) {
					defeito.setAtuacao(TipoAtuacao.ATENDIMENTO.getValor());
					defeito.setEquipe(Equipe.ATENDIMENTO.getValor());
				}else if (atuantes.stream().anyMatch(i -> TipoAtuacao.DESENVOLVIMENTO_GPS.getValor().equals(i.getType()))) {
					defeito.setAtuacao(TipoAtuacao.DESENVOLVIMENTO_GPS.getValor());
					defeito.setEquipe(Equipe.GPS.getValor());
				} else if (atuantes.stream().anyMatch(i -> TipoAtuacao.FUNCIONAL.getValor().equals(i.getType()))) {
					defeito.setAtuacao(TipoAtuacao.FUNCIONAL.getValor());
					defeito.setEquipe(Equipe.GPS.getValor());
				} else if (atuantes.stream().anyMatch(i -> TipoAtuacao.DESENVOLVIMENTO_URA.getValor().equals(i.getType()))) {
					defeito.setAtuacao(TipoAtuacao.DESENVOLVIMENTO_URA.getValor());
					defeito.setEquipe(Equipe.URA.getValor());
				}
				List<String> resp = new ArrayList<String>();
				for (Integrante integrante : atuantes) {
					
					resp.add(integrante.getName());
				}
				System.out.println(defeito.getBug().concat(" ").concat(defeito.getDomain()));
				defeito.setResponsavel(resp.toString().replace("[", "").replace("]", ""));
				
			} else {
				defeito.setAtuacao(TipoAtuacao.OUTRAS_ESQUIPES.getValor());
				defeito.setResponsavel("-");
			}
			rd.toString();
			rd.close();
		} catch (Exception e) {
			System.out.println(targetURL.concat(urlParameters));
			throw new EJBException(e);
		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
		return defeito;
	}
	

	private boolean isCellNotBlank(final Cell cell) {
		
		if (cell != null) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		} else {
			return Boolean.FALSE;
		}
		
		return cell != null && StringUtils.isNotBlank(cell.getStringCellValue());
	}

	public boolean isRowEmpty(final Row row) {

		if (row == null) {
			return Boolean.TRUE;
		}

		return !this.isCellNotBlank(row.getCell(0)) && !this.isCellNotBlank(row.getCell(1));
	}

}
	