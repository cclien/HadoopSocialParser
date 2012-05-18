package dsl.socialparser.types;

import java.util.ArrayList;
import java.util.List;

import com.restfb.types.Comment;
import com.restfb.types.NamedFacebookType;

public class CrawledComment {
	private Comment comment;
	private List<NamedFacebookType> likes = new ArrayList<NamedFacebookType>();
	
	public void setComment(Comment e) {
		this.comment = e;
	}
	
	public Comment getComment() {
		return this.comment;
	}
	
	public void addLike(NamedFacebookType e) {
		this.likes.add(e);
	}
	
	public List<NamedFacebookType> getLikes() {
		return this.likes;
	}
}