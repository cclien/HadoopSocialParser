package dsl.socialparser.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.io.Text;


public class PackSequenceFile {

	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

		if(args.length < 2) {
			System.err.println("PackSequenceFile - Convert local JSON files into a Hadoop Sequence File");
			System.err.println("usage: PackSequenceFile <inputdir> <SequenceFilePath>");
			
			System.exit(1);
		}
		
		String inputDir = args[0];
		String outputPath = args[1];
		
		Configuration conf = new Configuration();
		
		FileSystem fs = FileSystem.getLocal(conf);
		Writer w = new Writer(fs, conf, new Path(outputPath), Text.class, Text.class);

		File inputFolder = new File(inputDir);
		
		for (File f : inputFolder.listFiles() ) {
			String fileName = f.toString();
			System.out.println("Processing "+fileName);
			
			FileInputStream s = new FileInputStream(f);
			byte[] b = new byte[1024*1024];			
			int l;
			
			Text t = new Text();
			
			while( (l=s.read(b)) > 0) {
				t.append(b, 0, l);
			}
			
			w.append(new Text(fileName), t);
		}
		
		w.close();
		
	}

}
