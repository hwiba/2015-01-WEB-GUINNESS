package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.common.AbstractDao;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;

public class CommentDao extends AbstractDao {
	private static CommentDao commentDao = new CommentDao();
	
	public static CommentDao getInstance() {
		return commentDao;
	}

	public void createcomment(Comment comment) throws SQLException, ClassNotFoundException {
		String query = "insert into COMMENTS (commentText, commentType, userId, noteId) values(?, ?, ?, ?)";
		queryNotForReturn(query, comment.getCommentText(), comment.getCommentType(), comment.getUserId(), comment.getNoteId());
	}
	
	@SuppressWarnings("unchecked")
	public List<Comment> readCommentListByNoteId(String noteId) throws MakingObjectListFromJdbcException, SQLException, ClassNotFoundException {
		String sql = "select * from COMMENTS where noteId = ?;";
		String[] paramsKey = { "commentText", "commentType", "createDate", "userId", "noteId"};
		List<?> list = queryForObjectsReturn(Comment.class, paramsKey, sql, noteId);
		return (List<Comment>) list;
	}
	
}