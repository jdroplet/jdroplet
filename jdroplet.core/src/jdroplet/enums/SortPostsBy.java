package jdroplet.enums;

public enum SortPostsBy {
	LastPost (0),
	PostAuthor (1),
	TotalReplies (2),
	TotalViews (3),
	TotalLikes (4),
	TotalVotes(5),
	TotalRatings(6),
	Subject(7),
	LastModified(8),
	TotalValuables(9),
	Rand(10);
	
	private int value;
	SortPostsBy(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}

	public static SortPostsBy get(int value) {
		 return SortPostsBy.values()[value];
	}
}
