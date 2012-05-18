package dsl.socialparser.parser;

import java.util.List;

import com.restfb.types.Comment;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import com.restfb.types.Post.Comments;
import com.restfb.types.Post.Likes;

import com.restfb.DefaultJsonMapper;

import dsl.socialparser.types.CrawledComment;
import dsl.socialparser.types.CrawledPost;

public class CrawledPostParser {

	// Tags from the crawler
	final String EC_COMMENTS="{\"ec_comments\":";
	final String EC_LIKES="{\"ec_likes\":";
	final String EP_LIKES="{\"ep_likes\":";

	public CrawledPost parse (String JSONFileContent) {
			
		DefaultJsonMapper JsonMapper = new DefaultJsonMapper();
		CrawledPost post = new CrawledPost();
		CrawledComment lastComment=null;
		
		for(String s : JSONFileContent.split("\n\n") )  {

			if(s.startsWith("{\"id\":")) { // a post
				Post o = JsonMapper.toJavaObject(s, Post.class);
				post.setPost(o);
			} else if ( s.startsWith(EP_LIKES) ) {
				String sub = s.substring(EP_LIKES.length(), s.length()-1 );
				Likes o = JsonMapper.toJavaObject(sub, Likes.class);
				List<NamedFacebookType> likes = o.getData();
				for(NamedFacebookType e:likes) {
					post.addLike(e);
				}
			} else if ( s.startsWith(EC_COMMENTS) ) {
				String sub = s.substring(EC_COMMENTS.length(), s.length()-1 );
				Comments o = JsonMapper.toJavaObject(sub, Comments.class);
				List<Comment> comments = o.getData();
				for(Comment e:comments) {
					CrawledComment c = new CrawledComment();
					c.setComment(e);
					post.addComment(c);
					lastComment=c;
				}
			} else if ( s.startsWith(EC_LIKES)) {
				String sub = s.substring(EC_LIKES.length(), s.length()-1 );
				Likes o = JsonMapper.toJavaObject(sub, Likes.class);
				List<NamedFacebookType> likes = o.getData();
				if(lastComment != null) { // add ec_likes to last ec_comment
					for(NamedFacebookType e:likes) {
						lastComment.addLike(e);
					}
				}
			}
		}
	
		return post;
	}
	
}
