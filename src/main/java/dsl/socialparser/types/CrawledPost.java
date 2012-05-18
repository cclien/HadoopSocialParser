package dsl.socialparser.types;

import java.util.ArrayList;
import java.util.List;

import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;

public class CrawledPost {
	
	private Post post;
	private List<NamedFacebookType> likes = new ArrayList<NamedFacebookType>();
	private List<CrawledComment> comments = new ArrayList<CrawledComment>();
	
	public void setPost(Post post) {
		this.post=post;
	}
	
	public Post getPost() {
		return this.post;
	}
	
	public void addLike(NamedFacebookType e) {
		this.likes.add(e);
	}
	
	public List<NamedFacebookType> getLikes() {
		return this.likes;
	}
	
	public void addComment(CrawledComment e) {
		this.comments.add(e);
	}
	
	public List<CrawledComment> getComments() {
		return this.comments;
	}
		
}
