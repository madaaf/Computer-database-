package com.excilys.aflak.junit.DAO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.aflak.dao.connection.ConnectionBdd;
import com.excilys.aflak.dao.model.ComputerDAO;
import com.excilys.aflak.model.Company.CompanyBuilder;
import com.excilys.aflak.model.Computer;
import com.excilys.aflak.model.Computer.ComputerBuilder;
import com.excilys.aflak.utils.ExecuteScript;
import com.excilys.aflak.utils.TimeConvertor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class ComputerDAOTest {
	private ArrayList<Computer> listComputersTest = null;

	/*
	 * 
	 * For DAO test, right data are expected Otherwise, null value is return
	 */
	@Autowired
	private ComputerDAO dao;

	@Before
	public void setUp() throws Exception {
		ConnectionBdd.POOLCONNECTIONS.setTest(true);
		listComputersTest = new ArrayList<Computer>();

		listComputersTest.add(ComputerBuilder
				.createDefaultComputer()
				.withId(1L)
				.withName("MacBook Pro 15.4 inch")
				.withCompany(
						CompanyBuilder.crateDefaultCompany().withId(1L)
								.withName("Apple Inc.").build()).build());

		listComputersTest
				.add(ComputerBuilder
						.createDefaultComputer()
						.withId(2L)
						.withName("CM-2a")
						.withCompany(
								CompanyBuilder.crateDefaultCompany().withId(2L)
										.withName("Thinking Machines").build())
						.build());

		listComputersTest
				.add(ComputerBuilder
						.createDefaultComputer()
						.withId(3L)
						.withName("CM-200")
						.withCompany(
								CompanyBuilder.crateDefaultCompany().withId(2L)
										.withName("Thinking Machines").build())
						.build());

		listComputersTest
				.add(ComputerBuilder
						.createDefaultComputer()
						.withId(4L)
						.withName("CM-5e")
						.withCompany(
								CompanyBuilder.crateDefaultCompany().withId(2L)
										.withName("Thinking Machines").build())
						.build());

		listComputersTest
				.add(ComputerBuilder
						.createDefaultComputer()
						.withId(5L)
						.withName("CM-5")
						.withIntroduced(
								LocalDateTime.of(1991, 01, 01, 00, 00, 00))
						.withCompany(
								CompanyBuilder.crateDefaultCompany().withId(2L)
										.withName("Thinking Machines").build())
						.build());

		listComputersTest.add(ComputerBuilder
				.createDefaultComputer()
				.withId(6L)
				.withName("MacBook Pro")
				.withIntroduced(LocalDateTime.of(2006, 01, 10, 00, 00, 00))
				.withCompany(
						CompanyBuilder.crateDefaultCompany().withId(1L)
								.withName("Apple Inc.").build()).build());

		ExecuteScript.execute();
	}

	@Test
	public void findOne() {

		Computer computer = dao.find(2L);
		Assert.assertEquals(computer, listComputersTest.get(1));
	}

	@Test
	public void getAll() {

		List<Computer> dbComputer = new ArrayList<Computer>();
		dbComputer = dao.list();
		Assert.assertArrayEquals(dbComputer.toArray(),
				listComputersTest.toArray());
	}

	// Computer without Date
	@Test
	public void createWithNullDate() {

		Computer com = ComputerBuilder
				.createDefaultComputer()
				.withName("MacBook Pro 15.4 inch")
				.withCompany(
						CompanyBuilder.crateDefaultCompany().withId(1L)
								.withName("Apple Inc.").build()).build();

		// insert the computer in the bdd
		dao.create(com);
		// get the last computer in the bdd
		Computer lastComputer = dao.find(7L);
		com.setId(7L);
		Assert.assertEquals(com, lastComputer);
	}

	@Test
	public void createWithFalseDate() {
		// Computer to add
		Computer com = ComputerBuilder
				.createDefaultComputer()
				.withId(7L)
				.withName("MacBook Pro 15.4 inch")
				.withIntroduced(
						TimeConvertor
								.convertStringToLocalDateTime("19-ded-2000"))
				.withDiscontinued(
						TimeConvertor
								.convertStringToLocalDateTime("30-02-2000"))
				.withCompany(
						CompanyBuilder.crateDefaultCompany().withId(1L)
								.withName("Apple Inc.").build()).build();

		// insert the computer in the bdd
		dao.create(com);
		com.setIntroduced(null);
		// get the last computer in the bdd
		Computer lastComputer = dao.find(7L);
		Assert.assertEquals(com, lastComputer);
	}

	@Test
	public void search() {
		// Computer to add
		Computer com = ComputerBuilder
				.createDefaultComputer()
				.withName("MacBook Pro 15.4 inch")
				.withCompany(
						CompanyBuilder.crateDefaultCompany().withId(1L)
								.withName("Apple Inc.").build()).build();

		// insert the computer in the bdd
		// ComputerDAO.INSTANCE.getSomeFiltredComputer("cb", null, null, 0, 0);

	}

	@Test
	public void delete() {
		dao.delete(6L);
		Computer com = dao.find(6L);
		Assert.assertNull(com);
	}

	@Test
	public void update() {
		Computer com = ComputerBuilder
				.createDefaultComputer()
				.withId(1L)
				.withName("Modified")
				.withCompany(
						CompanyBuilder.crateDefaultCompany().withId(1L)
								.withName("Apple Inc.").build()).build();

		dao.update(com);
		Computer com2 = dao.find(1L);
		Assert.assertEquals(com, com2);
	}
}
