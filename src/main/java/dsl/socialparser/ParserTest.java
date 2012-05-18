package dsl.socialparser;

import java.io.File;
import java.io.FileInputStream;

import dsl.socialparser.parser.CrawledPostParser;
import dsl.socialparser.types.CrawledComment;
import dsl.socialparser.types.CrawledPost;

public class ParserTest {
	
	final static String EC_COMMENTS="{\"ec_comments\":"; 

	public static void main(String args[]) throws Exception {
		File f = new File("/home/cclien/SocialParser/input/1512216135_00000848_2011-02-11T03_135544726479188_201165019899154.json");
		
		FileInputStream fs = new FileInputStream(f);
		int b;	
		
		StringBuilder sb=new StringBuilder();
		
		while( (b=fs.read()) != -1) {
			sb.append((char)b);
		}
		
		String json = sb.toString();

		CrawledPostParser p = new CrawledPostParser();
		CrawledPost post = p.parse(json);
		
		System.out.println(post.getPost().getMessage());
		System.out.println("Likes:"+post.getLikes().size());
		System.out.println("Comments:"+post.getComments().size());
		for(CrawledComment c:post.getComments()) {
			System.out.println(c.getComment().getMessage());
			System.out.println("  Likes:"+c.getLikes().size());
		}
		
	}
}
