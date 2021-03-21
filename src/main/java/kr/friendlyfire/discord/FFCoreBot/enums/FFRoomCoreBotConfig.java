package kr.friendlyfire.discord.FFCoreBot.enums;

public enum FFRoomCoreBotConfig {
	//String
	/**
	 * 디스코드 토큰
	 */
	TOKEN = System.getenv("BOT_TOKEN"), //디스코드 봇 토큰
	/**
	 * 서버의 ID
	 */
	SERVER_ID("805958884883038231"),
	/**
	 * 만들어진 방의 정보가 출력될 채널의 ID
	 */
	PRINT_TEXT_CH_ID("822103582261903430"),
	/**
	 * 해당 카테고리(ID기준)에 속한 모든 음성채널이 인스턴스 채널로 만들어짐
	 */
	SEARCH_CATEGORY_ID("805958885504057397"),
	/**
	 * 이 카테고리에 인스턴스 채널이 생성됨
	 */
	MAKE_CATEGORY_ID("805958885504057397"),
	/**
	 * 봇의 이름
	 */
	BOT_NAME(""),
	/**
	 * 봇의 대표이미지
	 */
	BOT_IMG(""),
	/**
	 * 관리자의 uid
	 */
	ADMIN_UID(""),
	;

	private String strVal;
	private int intVal;

	private FFRoomCoreBotConfig() {

	}
	private FFRoomCoreBotConfig(String strVal) {
		this.strVal = strVal;
	}
	private FFRoomCoreBotConfig(int intVal) {
		this.intVal = intVal;
	}

	public String getStrVal() {
		return strVal;
	}
	public Integer getIntVal() {
		return intVal;
	}
	public void setVal(String strVal) {
		this.strVal = strVal;
	}
	public void setVal(int intVal) {
		this.intVal = intVal;
	}
}
