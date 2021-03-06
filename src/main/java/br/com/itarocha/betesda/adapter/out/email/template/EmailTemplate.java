package br.com.itarocha.betesda.adapter.out.email.template;

import br.com.itarocha.betesda.utils.StrUtil;

import java.util.Collection;
import java.util.Map;

public class EmailTemplate {
	
	private static final String BLANK = "";

	private String templateId;

	private String template;

	private Map<String, String> replacementParams;

	public EmailTemplate(String templateId) {
		this.templateId = templateId;
		try {
			this.template = loadTemplate(templateId);
		} catch (Exception e) {
			this.template = BLANK;
		}
	}

	public String getTemplate(Map<String, String> replacements) {
		String cTemplate = this.template;

		if (!isObjectEmpty(cTemplate)) {
			for (Map.Entry<String, String> entry : replacements.entrySet()) {
				cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
			}
		}

		return cTemplate;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Map<String, String> getReplacementParams() {
		return replacementParams;
	}

	public void setReplacementParams(Map<String, String> replacementParams) {
		this.replacementParams = replacementParams;
	}


	private String loadTemplate(String templateId) throws Exception {
		String content = BLANK;
		try {
			StringBuilder sb = StrUtil.loadFile("/email-templates/" + templateId);
			content = sb.toString();
			this.template = content;
		} catch (Exception e) {
			throw new Exception("Could not read template with ID = " + templateId);
		}
		return content;
	}

	private static boolean isObjectEmpty(Object object) {
		if(object == null) return true;
		else if(object instanceof String) {
			if (((String)object).trim().length() == 0) {
				return true;
			}
		} else if(object instanceof Collection) {
			return isCollectionEmpty((Collection<?>)object);
		}
		return false;
	}    
	
	private static boolean isCollectionEmpty(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		}
		return false;
	}
}