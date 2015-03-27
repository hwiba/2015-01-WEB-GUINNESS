package org.nhnnext.guinness.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;

public class GroupDaoTest {

	private GroupDao groupDao;

	@Before
	public void setup() {
		groupDao = new GroupDao();
	}

	@Test
	public void testCheckExistGroupId() throws Exception {
		assertEquals(groupDao.checkExistGroupId("abcde"), true);
	}

	@Test
	public void testCheckUnExistGroupId() throws Exception {
		assertEquals(groupDao.checkExistGroupId("11111"), false);
	}

	@Test
	public void crud() throws ClassNotFoundException, MakingObjectListFromJdbcException, SQLException {
		Group group = GroupTest.TEST_GROUP;

		groupDao.deleteGroup(group);
		groupDao.createGroup(group);

		Group dbGroup = groupDao.findByGroupId(group.getGroupId());
		assertEquals(group, dbGroup);
	}

	@Test
	public void readGroupList() throws ClassNotFoundException, MakingObjectListFromJdbcException, SQLException {
		List<Group> list = groupDao.readGroupList("das@das.com");
		for (Group group : list) {
			System.out.println(group.getGroupName());
		}
		assertNotNull(list);
	}
}
