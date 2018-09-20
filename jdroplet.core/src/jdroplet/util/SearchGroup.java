package jdroplet.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class SearchGroup {

	public static String AND = "AND";
	public static String OR = "OR";
	
	private ArrayList<SearchQuery> querys;
	private ArrayList<SearchGroup> groups;
	private String term = null;
	private boolean empty = true;
	
	public SearchGroup() {
		querys = new ArrayList<SearchQuery>();
		groups = new ArrayList<SearchGroup>();
		term = SearchGroup.OR;
	}
	
	public void addQuery(SearchQuery query) {
		querys.add(query);
		empty = false;
	}
	
	public void addGroup(SearchGroup group) {
		groups.add(group);
		empty = false;
	}
	
	public List<SearchQuery> getQuerys() {
		return querys;
	}
	
	public void setTerm(String term) {
		this.term = term;
	}
	
	public String getTerm() {
		return this.term;
	}
	
	public boolean isEmpty() {
		return empty;
	}
	
	@Override
	public String toString() {
		ArrayList<String> strs = null;
		
		strs = new ArrayList<String>();
		for (SearchQuery query:querys) {
			strs.add(query.toString());
		}
		
		for(SearchGroup group:groups) {
			if (group.isEmpty() == false)
				strs.add("(" + group.toString()+ ")");
		}
		return StringUtils.join(strs, " " + this.term + " ");
	}
}
