package com.excilys.aflak.dao.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.excilys.aflak.dao.connection.ConnectionBdd;
import com.excilys.aflak.dao.inter.IDAOComputer;
import com.excilys.aflak.dao.mapper.MapperDAO;
import com.excilys.aflak.dao.utils.TimeConvertorDAO;
import com.excilys.aflak.model.Computer;

import exception.DAOException;

@Repository
public class ComputerDAO implements IDAOComputer {

	// INSTANCE;
	private static Logger logger = LoggerFactory.getLogger(ComputerDAO.class);

	@Override
	public Long create(Computer computer) {
		PreparedStatement state = null;
		Connection connect = ConnectionBdd.POOLCONNECTIONS.getConnection();
		ResultSet set = null;
		long idComputer;
		try {
			state = connect
					.prepareStatement(
							"INSERT INTO computer (name,introduced,discontinued,company_id) VALUES(?,?,?,?)",
							state.RETURN_GENERATED_KEYS);
			state.setString(1, computer.getName());
			state.setTimestamp(2, TimeConvertorDAO
					.convertLocalDateTimeToTimestamp(computer.getIntroduced()));
			state.setTimestamp(3,
					TimeConvertorDAO.convertLocalDateTimeToTimestamp(computer
							.getDiscontinued()));
			if (computer.getCompany().getId() == (-1)) {
				state.setNull(4, Types.BIGINT);
			} else {
				state.setLong(4, (computer.getCompany().getId()));
			}

			state.executeUpdate();
			set = state.getGeneratedKeys();
			set.next();
			// retourn l'id du computer
			idComputer = set.getLong(1);

		} catch (SQLException e) {
			logger.error("Creation of computer failed");
			throw new DAOException("Connection Failed " + e);

		} finally {
			ConnectionBdd.POOLCONNECTIONS.closeConnection(state, set);
		}
		return idComputer;
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		int result = 0;
		Connection connect = ConnectionBdd.POOLCONNECTIONS.getConnection();
		PreparedStatement state = null;
		try {
			state = connect
					.prepareStatement("DELETE FROM computer WHERE id = ?");
			if (id > 0) {
				state.setLong(1, id);
			} else {
				state.setNull(1, Types.BIGINT);
			}
			result = state.executeUpdate();
			if (result == 0) {
				throw new DAOException("This ID doesn't exist in the bdd");
			}
			return true;
		} catch (SQLException e) {
			logger.error("delete computer failed");
			throw new DAOException("Connection Failed " + e);

		} finally {
			ConnectionBdd.POOLCONNECTIONS.closeConnection(state, null);
		}

	}

	@Override
	public Computer update(Computer computer) {
		Connection connect = ConnectionBdd.POOLCONNECTIONS.getConnection();
		PreparedStatement state = null;

		try {
			state = connect
					.prepareStatement("UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?");

			state.setString(1, computer.getName());

			state.setTimestamp(2, TimeConvertorDAO
					.convertLocalDateTimeToTimestamp(computer.getIntroduced()));
			state.setTimestamp(3,
					TimeConvertorDAO.convertLocalDateTimeToTimestamp(computer
							.getDiscontinued()));

			if (computer.getCompany() != null) {
				state.setLong(4, computer.getCompany().getId());
			} else {
				state.setNull(4, Types.BIGINT);
			}

			if (computer.getId() > 0) {
				state.setLong(5, computer.getId());
			} else {
				state.setNull(5, Types.BIGINT);
			}

			int result = state.executeUpdate();

		} catch (SQLException e) {
			logger.error("update computer failed");
			throw new DAOException("Connection Failed " + e);
		} finally {
			ConnectionBdd.POOLCONNECTIONS.closeConnection(state);
		}
		return null;
	}

	@Override
	public Computer find(Long id) {
		Computer computer = null;
		Connection connect = ConnectionBdd.POOLCONNECTIONS.getConnection();
		PreparedStatement state = null;
		ResultSet result = null;
		try {
			state = connect
					.prepareStatement("select computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name AS 'company_name' from computer left join company on  computer.company_id = company.id WHERE computer.id = ?");
			if (id > 0) {
				state.setLong(1, id);
			} else {
				state.setNull(1, Types.BIGINT);
			}

			result = state.executeQuery();

			if (result.first()) {
				computer = MapperDAO.mapComputer(result);
			}
		} catch (SQLException e) {
			logger.error("find computer failed");
			throw new DAOException("Connection Failed " + e);
		} finally {
			ConnectionBdd.POOLCONNECTIONS.closeConnection(state, result);
		}
		// TODO Auto-generated method stub
		return computer;
	}

	@Override
	public List<Computer> list() {
		List<Computer> listComputer = new ArrayList<Computer>();
		Computer computer = null;
		Connection connect = ConnectionBdd.POOLCONNECTIONS.getConnection();
		PreparedStatement state = null;
		ResultSet result = null;
		try {

			state = connect
					.prepareStatement("select computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name AS 'company_name' from computer left join company on  computer.company_id = company.id");

			result = state.executeQuery();
			while (result.next()) {
				computer = MapperDAO.mapComputer(result);
				listComputer.add(computer);

			}

		} catch (SQLException e) {
			logger.error("list computers failed");
			throw new DAOException("Connection Failed " + e);
		} finally {
			ConnectionBdd.POOLCONNECTIONS.closeConnection(state, result);
		}

		return listComputer;
	}

	@Override
	public List<Computer> getSomeComputers(int debut, int nbr) {
		List<Computer> list = new ArrayList<Computer>();
		Connection connect = ConnectionBdd.POOLCONNECTIONS.getConnection();
		PreparedStatement state = null;
		ResultSet result = null;
		Computer computer = null;
		try {
			state = connect
					.prepareStatement("select computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name AS 'company_name' from computer left join company on  computer.company_id = company.id LIMIT "
							+ nbr + " OFFSET " + debut);
			result = state.executeQuery();
			while (result.next()) {
				computer = MapperDAO.mapComputer(result);
				list.add(computer);
			}

		} catch (SQLException e) {
			logger.error("gets some computers computers failed");
			throw new DAOException("Connection Failed " + e);
		} finally {
			ConnectionBdd.POOLCONNECTIONS.closeConnection(state, result);
		}

		return list;
	}

	@Override
	public int getSizeTabComputers() {
		Connection connect = ConnectionBdd.POOLCONNECTIONS.getConnection();
		PreparedStatement state = null;
		ResultSet result = null;
		int size = 0;
		try {
			state = connect.prepareStatement("select COUNT(*) from computer");
			result = state.executeQuery();
			if (result.next()) {
				size = result.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("get size tab computers failed");
			throw new DAOException("Connection Failed " + e);
		} finally {
			ConnectionBdd.POOLCONNECTIONS.closeConnection(state, null);
		}
		return size;
	}

	@Override
	public List<Computer> getSomeFiltredComputer(String filtre, String colomn,
			String way, int debut, int limit) {
		Connection connect = ConnectionBdd.POOLCONNECTIONS.getConnection();
		PreparedStatement state = null;
		ResultSet result = null;
		List<Computer> list = new ArrayList<Computer>();
		Computer computer = null;
		try {
			state = connect
					.prepareStatement("SELECT computer.id, computer.name, computer.introduced, computer.discontinued, computer.company_id, company.name AS 'company_name' from computer left join company on computer.company_id = company.id WHERE computer.name like ? or company.name like ? ORDER BY "
							+ colomn + " " + way + " LIMIT ? OFFSET ?");
			filtre = "%" + filtre + "%";
			state.setString(1, filtre);
			state.setString(2, filtre);
			state.setInt(3, limit);
			state.setInt(4, debut);

			result = state.executeQuery();
			while (result.next()) {
				computer = MapperDAO.mapComputer(result);
				list.add(computer);
			}
		} catch (SQLException e) {
			logger.error("gets some filtred computers failed");
			throw new DAOException("Connection Failed " + e);
		} finally {
			ConnectionBdd.POOLCONNECTIONS.closeConnection(state, result);
		}
		return list;
	}

	@Override
	public int getSizeFiltredComputer(String filtre) {
		Connection connect = ConnectionBdd.POOLCONNECTIONS.getConnection();
		PreparedStatement state = null;
		ResultSet result = null;
		int size = 0;
		try {
			state = connect
					.prepareStatement("SELECT  COUNT(*) from computer left join company on computer.company_id = company.id WHERE computer.name like ? or company.name like ? ");
			filtre = "%" + filtre + "%";
			state.setString(1, filtre);
			state.setString(2, filtre);
			result = state.executeQuery();
			while (result.next()) {
				size = result.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("gets size filtred computers failed");
			throw new DAOException("Connection Failed " + e);
		} finally {
			ConnectionBdd.POOLCONNECTIONS.closeConnection(state, result);
		}
		return size;
	}

	@Override
	public void deleteComputerFromCompany(Long companyId) {
		Connection connect = ConnectionBdd.POOLCONNECTIONS.getConnection();
		PreparedStatement state = null;
		try {
			state = connect
					.prepareStatement("DELETE from computer WHERE company_id = ?");
			state.setLong(1, companyId);
			int ok = state.executeUpdate();

			logger.error("delete computer from company id failed");
		} catch (SQLException e) {
			throw new DAOException("Connection Failed " + e);
		} finally {
			ConnectionBdd.POOLCONNECTIONS.closeConnection(state);
		}
	}
}
