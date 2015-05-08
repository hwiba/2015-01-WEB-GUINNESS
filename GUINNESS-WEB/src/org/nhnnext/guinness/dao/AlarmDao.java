package org.nhnnext.guinness.dao;

import java.util.List;

import org.nhnnext.guinness.model.Alarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class AlarmDao extends JdbcDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(AlarmDao.class);
	
	public void create(Alarm alarm) {
		String sql = "insert into ALARMS (alarmId, calleeId, callerId, noteId, alarmText, alarmStatus, createDate) values(?, ?, ?, ?, ?, ?, default)";
		logger.debug("alarm: {}", alarm);
		logger.debug("알람 상태 {}",alarm.getAlarmStatus());
		
		getJdbcTemplate().update(sql, alarm.getAlarmId(), alarm.getCalleeId(), alarm.getCallerId(), alarm.getNoteId(), alarm.getAlarmText(), alarm.getAlarmStatus());
	}

	public Alarm read(String alarmId) {
		String sql = "select * from ALARMS where alarmId = ?";

		try {
			return getJdbcTemplate().queryForObject(
				sql,
				(rs, rowNum) -> new Alarm(rs.getString("alarmId"), rs
						.getString("calleeId"), rs.getString("callerId"), rs
						.getString("noteId"), rs.getString("alarmText"), rs.getString("alarmStatus"), rs
						.getString("createDate")), alarmId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<Alarm> list(String calleeId) {
		String sql = "select * from ALARMS where calleeId = ?";

		return getJdbcTemplate().query(
				sql,
				(rs, rowNum) -> new Alarm(rs.getString("alarmId"), rs
						.getString("calleeId"), rs.getString("callerId"), rs
						.getString("noteId"), rs.getString("alarmText"), rs.getString("alarmStatus"), rs
						.getString("createDate")), calleeId);
	}

	public void delete(String alarmId) {
		String sql = "delete from ALARMS where alarmId = ?";
		getJdbcTemplate().update(sql, alarmId);
	}

	public List<String> readNoteAlarm(String sessionUserId) {
		String sql = "select groupId from ALARMS as A, NOTES as N where A.alarmStatus = 'N' and A.calleeId ='"
				+ sessionUserId
				+ "' and N.noteId = A.noteId;";
		return getJdbcTemplate().queryForList(sql, String.class);
	}

}
