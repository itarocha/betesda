package br.com.itarocha.betesda.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StrUtil {

	public static StringBuilder loadFile(String fileName) {
		StringBuilder sb = new StringBuilder();

		try ( InputStream in = StrUtil.class.getResourceAsStream(fileName);
			 BufferedReader reader = new BufferedReader(new InputStreamReader(in));  
		) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        sb.append(line).append("\n");
		    }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return sb;
	}

	@Deprecated
	public static StringBuilder loadFileOld(String fileName) {
		StringBuilder sb = new StringBuilder();

		try {
			Path path = Paths.get(StrUtil.class.getResource(fileName).toURI());
			Stream<String> lines = Files.lines(path);
			lines.forEach(line -> sb.append(line).append("\n"));
			lines.close();
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
		}
		
		return sb;
	}
	
}
