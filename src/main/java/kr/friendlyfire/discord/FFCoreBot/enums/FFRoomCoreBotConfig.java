package kr.friendlyfire.discord.FFCoreBot.enums;

public enum FFRoomCoreBotConfig {
	//String
	/**
	 * 디스코드 토큰
	 */
	TOKEN("84FDV1UkMurCy79rDQQR9LrLJEwxxrhD"), //디스코드 봇 토큰
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
	BOT_NAME("음성서버"),
	/**
	 * 봇의 대표이미지
	 */
	BOT_IMG("https://www.google.com/imgres?imgurl=http%3A%2F%2Fbizionkorea.cafe24.com%2Fdata%2Feditor%2F1410%2F810fb04cc8b8c2ba2b8842fb7bb4feec_vICoHABwEPSRrV5G51.jpg&imgrefurl=http%3A%2F%2Fshow.bizion.com%2Fbbs%2Fboard.php%3Fbo_table%3Dgear%26wr_id%3D749%26page%3D9%26device%3Dpc&tbnid=6Pgz5fPb_IvVTM&vet=12ahUKEwiRlf3V8MHvAhWOz4sBHfm1AhQQMygAegUIARDrAQ..i&docid=lX3S7KP6w9ImuM&w=670&h=479&q=%ED%97%A4%EB%93%9C%EC%85%8B&hl=ko&ved=2ahUKEwiRlf3V8MHvAhWOz4sBHfm1AhQQMygAegUIARDrAQ"),
	/**
	 * 관리자의 uid
	 */
	ADMIN_UID("347357018878771210"),
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
