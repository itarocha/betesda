package br.com.itarocha.betesda.core.domain.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import lombok.Data;

@Data
public class Email {
	
	private String from;

	private List<String> to;

	private List<String> cc;

	private String subject;

	private String message;
	
	private boolean isHtml;

	public Email() {
		this.to = new ArrayList<>();
		this.cc = new ArrayList<>();
	}

	public Email(String from, String toList, String subject, String message) {
		this();
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.to.addAll(Arrays.asList(splitByComma(toList)));
	}

	public Email(String from, String toList, String ccList, String subject, String message) {
		this();
		this.from = from;
		this.subject = subject;
		this.message = message;
		this.to.addAll(Arrays.asList(splitByComma(toList)));
		this.cc.addAll(Arrays.asList(splitByComma(ccList)));
	}

	private String[] splitByComma(String toMultiple) {
		String[] toSplit = toMultiple.split(",");
		return toSplit;
	}

	public String getToAsList() {
		List<String> listOfItems = this.to;
		String separator = ",";

		StringBuilder sb = new StringBuilder();
		Iterator<String> stit = listOfItems.iterator();

		while (stit.hasNext()) {
			sb.append(stit.next());
			if (stit.hasNext()) {
				sb.append(separator);
			}
		}

		return sb.toString();
		
	}
}