package kr.friendlyfire.discord.FFCoreBot;

import kr.friendlyfire.discord.FFCoreBot.Listener.FFRoomCoreBotListener;
import kr.friendlyfire.discord.FFCoreBot.enums.FFRoomCoreBotConfig;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;

public class CreateDiscordBot extends JDABuilder {

	public CreateDiscordBot(int botType) {
		this.setAutoReconnect(true)
		.setStatus(OnlineStatus.ONLINE);

		switch (botType) {
		case 0:
			this.setGame(Game.of(GameType.DEFAULT, "채널 관리"))
			.setToken(FFRoomCoreBotConfig.TOKEN.getStrVal())
			.addEventListener(new FFRoomCoreBotListener());
			break;
		default:
			break;
		}
	}
	public CreateDiscordBot(boolean setAutoReconnect, OnlineStatus setStatus,
			Game setGame, String setToken, Object addEventListener) {
		this.setAutoReconnect(setAutoReconnect)
		.setStatus(setStatus)
		.setGame(setGame)
		.setToken(setToken)
		.addEventListener(addEventListener);
	}
}
