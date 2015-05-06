package com.excilys.aflak.junit.DAO;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.aflak.dao.model.CompanyDAO;
import com.excilys.aflak.model.Company;
import com.excilys.aflak.model.Company.CompanyBuilder;
import com.excilys.aflak.utils.ExecuteScript;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class CompanyDAOTest {
	List<Company> listCompanies = null;

	@Autowired
	private CompanyDAO dao;

	@Before
	public void setUp() throws Exception {

		listCompanies = new ArrayList<Company>();
		listCompanies.add(CompanyBuilder.crateDefaultCompany().withId(1L)
				.withName("Apple Inc.").build());

		listCompanies.add(CompanyBuilder.crateDefaultCompany().withId(2L)
				.withName("Thinking Machines").build());

		listCompanies.add(CompanyBuilder.crateDefaultCompany().withId(3L)
				.withName("RCA").build());

		listCompanies.add(CompanyBuilder.crateDefaultCompany().withId(4L)
				.withName("Netronics").build());

		ExecuteScript.execute();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void list() {
		List<Company> bddCompany = new ArrayList<Company>();
		bddCompany = dao.list();
		Assert.assertArrayEquals(bddCompany.toArray(), listCompanies.toArray());

	}

	@Test
	public void find() {
		Company comp = dao.find(1L);
		Assert.assertEquals(comp, listCompanies.get(0));

	}

}
