package kr.friendlyfire.discord.FFCoreBot;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

public class App 
{
	static Logger logger = Logger.getLogger(App.class);
	public enum BotType{
    	MAIN,
    }    
    public static void main( String[] args ) throws LoginException {
    	new CreateDiscordBot(BotType.MAIN.ordinal()).buildAsync();
    }
}