package ch.thmx.simulator.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportReader extends Reader {
	
	private BufferedReader reader;
	
	public ReportReader(Reader reader) {
		this.reader = new BufferedReader(reader);
	}

	@Override
	public void close() throws IOException {
		this.reader.close();
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		return this.reader.read(cbuf, off, len);
	}
	
	public Report readReport() throws IOException {
		
		String line = this.reader.readLine();
		if (line == null) {
			return null;
		}

		// Extract report type from RegEx
		//		# ** type: Message
		//		+ Group 1 -> Report type
		//		+ Group 2 -> Report message
		Pattern pattern = Pattern.compile("^.*\\*\\* (\\w+):(.*)$", Pattern.CASE_INSENSITIVE);		
		Matcher matcher = pattern.matcher(line);
		
		// No match means the report is unknown.
		if (!matcher.find()) {
			return new Unknown(line);
		}
		
		ReportType type = ReportType.fromName(matcher.group(1));
		String message = matcher.group(2);
		if (message != null) {
			message = message.trim();
		}
		
		switch (type) {
			case Note:
				return new Note(message);
			
			case Warning:
				return new Warning(message);
			
			case Failure:
				return new Failure(message);
			
			case FatalError:
				return new FatalError("", message);
				
			case Update:
				return new Update(message);
			
			case Final:
				return new Final(message);
				
			default:
				return new Unknown(message);
		}
	}

}
