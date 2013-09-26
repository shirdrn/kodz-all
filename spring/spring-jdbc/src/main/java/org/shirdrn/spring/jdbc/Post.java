package org.shirdrn.spring.jdbc;

public class Post {

	private int id;
	private int postAuthor;
	private String postDate;
	private String postContent;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPostAuthor() {
		return postAuthor;
	}
	public void setPostAuthor(int postAuthor) {
		this.postAuthor = postAuthor;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	public String getPostContent() {
		return postContent;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[").append("id=").append(id).append(", ")
		.append("postAuthor=").append(postAuthor).append(", ")
		.append("postDate=").append(postDate).append(", ")
//		.append("postContent=").append(postContent).append(", ")
		.append("id=").append(id).append(", ")
		.append("]");
		return sb.toString();
	}
	
	
}
