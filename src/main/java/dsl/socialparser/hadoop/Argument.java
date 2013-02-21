package dsl.socialparser.hadoop;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.restfb.types.Comment;
import com.restfb.types.Post;

import dsl.socialparser.parser.CrawledPostParser;
import dsl.socialparser.types.CrawledComment;
import dsl.socialparser.types.CrawledPost;

public class Argument {
		
	public static class ArgumentMapper extends Mapper<Object, Text, Text, Text> {
		
		CrawledPostParser parser = new CrawledPostParser();
			
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		
			// value contains the json body, convert it to a string
			String jsonBody = value.toString();  
			// parse json into a object
			CrawledPost p = parser.parse(jsonBody);
			
			// anyway, get something from the post
			String mesg=null;
			Post post = p.getPost();			
			SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.UK);
			if(post != null) {
				try {
					String time = ISO8601DATEFORMAT.format(post.getCreatedTime());
					int len=0;
					if(post.getMessage() != null)
						len = post.getMessage().length();
					long likes = 0;
					if(post.getLikesCount() != null)
						likes = post.getLikesCount();
					String msgID = post.getId();
					String from = post.getFrom().getId();
					
					mesg="*P ";
					mesg+="time = \""+time+"\" ";
					mesg+="len = "+len+" ";
					mesg+="likes = "+likes+"\n";
					mesg+="-- msg# = \""+msgID+"\"\n";
					mesg+="-- from = \""+from+"\"\n";
					
					for(CrawledComment c:p.getComments()) {
						Comment comment = c.getComment();
						time = ISO8601DATEFORMAT.format(post.getCreatedTime());
						len=0;
						if(comment.getMessage() != null)
							len = comment.getMessage().length();
						likes = 0;
						if(comment.getLikes() != null)
							likes = comment.getLikes();
						msgID = comment.getId();
						from = comment.getFrom().getId();

						mesg+="+C ";
						mesg+="time = \""+time+"\" ";
						mesg+="len = "+len+" ";
						mesg+="likes = "+likes+"\n";
						mesg+="-- msg# = \""+msgID+"\"\n";
						mesg+="-- from = \""+from+"\"\n";
					}
				} catch (NullPointerException e) {
					mesg="ERROR"+e.toString()+mesg;
				}

				// write the result
				context.write(new Text(mesg),new Text(""));
				
			} 
			 
		}
		
	}
	
	public static void main(String[] args) throws Exception {

		if(args.length < 2) {
			System.err.println("usage: Argument <input path> <output path>");
			
			System.exit(1);
		}
		
		String inputPath = args[0];
		String outputPath = args[1];
		
		Configuration conf = new Configuration();
		Job job = new Job(conf, "SocialParser - Argument");
		job.setJarByClass(Argument.class);
		job.setMapperClass(ArgumentMapper.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		FileInputFormat.addInputPath(job, new Path(inputPath) );
		FileOutputFormat.setOutputPath(job, new Path(outputPath) );
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
	
}
