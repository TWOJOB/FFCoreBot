package kr.friendlyfire.discord.FFCoreBot.domain;

public class InstanceRoomInfo {
	private String hostUserId;
	private String messageId;
	private String category;
	private String name;
	private String memo;

	public InstanceRoomInfo(String hostUserId, String messageId, String category, String name, String memo) {
		super();
		this.hostUserId = hostUserId;
		this.messageId = messageId;
		this.category = category;
		this.name = name;
		this.memo = memo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHostUserId() {
		return hostUserId;
	}
	public void setHostUserId(String hostUserId) {
		this.hostUserId = hostUserId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
