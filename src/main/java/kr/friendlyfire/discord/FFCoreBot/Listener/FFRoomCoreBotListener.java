package kr.friendlyfire.discord.FFCoreBot.Listener;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import kr.friendlyfire.discord.FFCoreBot.domain.InstanceRoomInfo;
import kr.friendlyfire.discord.FFCoreBot.enums.FFRoomCoreBotConfig;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildReadyEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.ChannelAction;

public class FFRoomCoreBotListener extends ListenerAdapter {
	private Guild guild;
	private HashMap<String, InstanceRoomInfo> createdVoiceCh = new HashMap<String, InstanceRoomInfo>();

	private VoiceChannel JoinCreatedVoiceCh(VoiceChannel voiceChannel, Member member){
		if(voiceChannel.getParent().getId().equalsIgnoreCase(FFRoomCoreBotConfig.SEARCH_CATEGORY_ID.getStrVal())) {
			GuildController gc = guild.getController();
			Category cg = guild.getCategoryById(FFRoomCoreBotConfig.MAKE_CATEGORY_ID.getStrVal());
			//인스턴스 음성 채널을 만듬
			ChannelAction channelAction = cg.createVoiceChannel(voiceChannel.getName()).setUserlimit(voiceChannel.getUserLimit());
			List<PermissionOverride> permissionOverrides = voiceChannel.getRolePermissionOverrides();
			for (PermissionOverride permissionOverride : permissionOverrides) {
				channelAction.addPermissionOverride(permissionOverride.getRole(), permissionOverride.getAllowed(), permissionOverride.getDenied());
			}
			Channel vc = channelAction.complete();
					
			//유저를 인스턴스 음성 채널로 옮김
			try {
				gc.moveVoiceMember(member, guild.getVoiceChannelById(vc.getId())).complete();
			} catch (Exception e) {
			}
			//인스턴스 info 메세지를 만듬
			EmbedBuilder embedBuilder = new EmbedBuilder();
			embedBuilder.setAuthor("음성 채팅 정보", null , FFRoomCoreBotConfig.BOT_IMG.getStrVal());
			embedBuilder.addField("채널명", voiceChannel.getName(), false);
			embedBuilder.addField("호스트", "<@" + member.getUser().getId() + ">", true);
			embedBuilder.addField("목적", voiceChannel.getName(), true);
			embedBuilder.setFooter(FFRoomCoreBotConfig.BOT_NAME.getStrVal(), FFRoomCoreBotConfig.BOT_IMG.getStrVal());
			embedBuilder.setTimestamp(Instant.now());
			embedBuilder.setColor(14177041);
			//printTextChId 채널에 올라갈 메세지
			Message msg = guild.getTextChannelById(FFRoomCoreBotConfig.PRINT_TEXT_CH_ID.getStrVal())
					.sendMessage(embedBuilder.build()).complete();
			CreatedVoiceChRemoveCheck((VoiceChannel) vc, member);
			
			InstanceRoomInfo roomInfo = new InstanceRoomInfo(member.getUser().getId(), msg.getId(), voiceChannel.getName(), voiceChannel.getName(), "");

			//해쉬에 빈 넣음
			createdVoiceCh.put(vc.getId(), roomInfo);
			CreatedVoiceEmpty((VoiceChannel) vc);
			return (VoiceChannel) vc;
		} 
		return null;
	}
	private void CreatedVoiceChRemoveCheck(VoiceChannel voiceChannel, Member member) {
		if(createdVoiceCh.get(voiceChannel.getId()) != null) {
			InstanceRoomInfo roomInfo = createdVoiceCh.get(voiceChannel.getId());
			if(voiceChannel.getMembers().size() > 0 && roomInfo.getHostUserId().equals(member.getUser().getId())) {
				Member alterMember = voiceChannel.getMembers().get(0);
				roomInfo.setHostUserId(alterMember.getUser().getId());
				try {
					alterEmbed(alterMember);
				} catch (Exception e) {
					guild.getTextChannelById(FFRoomCoreBotConfig.PRINT_TEXT_CH_ID.getStrVal()).deleteMessageById(roomInfo.getMessageId()).complete();
				}
			}
			CreatedVoiceEmpty(voiceChannel);
		}
	}
	private void CreatedVoiceEmpty(VoiceChannel voiceChannel) {
		if(voiceChannel.getMembers().size() < 1) {
			voiceChannel.delete().complete();
			guild.getTextChannelById(FFRoomCoreBotConfig.PRINT_TEXT_CH_ID.getStrVal()).deleteMessageById(createdVoiceCh.get(voiceChannel.getId()).getMessageId()).queue();
			createdVoiceCh.remove(voiceChannel.getId());
		}
	}
	private void alterEmbed(Member member) {
		InstanceRoomInfo info = createdVoiceCh.get(member.getVoiceState().getChannel().getId());
		info.getMessageId();
		//printTextChId 채널에 올라간 메세지를 삭제함
		guild.getTextChannelById(FFRoomCoreBotConfig.PRINT_TEXT_CH_ID.getStrVal()).deleteMessageById(info.getMessageId()).complete();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		embedBuilder.setAuthor("음성 채팅 정보", null , FFRoomCoreBotConfig.BOT_IMG.getStrVal());
		embedBuilder.addField("채널명", info.getName(), false);
		embedBuilder.addField("호스트", "<@" + info.getHostUserId() + ">", true);
		embedBuilder.addField("목적", info.getCategory(), true);
		if(info.getMemo() != "") embedBuilder.addField("메모", info.getMemo(), false);
		embedBuilder.setFooter("FriendlyFireRoomCore", FFRoomCoreBotConfig.BOT_IMG.getStrVal());
		embedBuilder.setTimestamp(Instant.now());
		embedBuilder.setColor(14177041);
		//printTextChId 채널에 메세지를 다시 올림
		Message alterMsg = guild.getTextChannelById(FFRoomCoreBotConfig.PRINT_TEXT_CH_ID.getStrVal()).sendMessage(embedBuilder.build()).complete();
		info.setMessageId(alterMsg.getId());
	}

	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		new Thread() {
			@Override
			public void run() {
				VoiceChannel ch = JoinCreatedVoiceCh(event.getChannelJoined(), event.getMember());
			}
		}.start();
	}
	
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		new Thread() {
			@Override
			public void run() {
				try {
					VoiceChannel ch = JoinCreatedVoiceCh(event.getChannelJoined(), event.getMember());
					if(ch == null) {
						
					}else if(ch.getMembers().size() < 1) {
						CreatedVoiceChRemoveCheck(ch, event.getMember());
					}
					CreatedVoiceChRemoveCheck(event.getChannelLeft(), event.getMember());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		new Thread() {
			@Override
			public void run() {
				try {
					CreatedVoiceChRemoveCheck(event.getChannelLeft(), event.getMember());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
	}
	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
	}

	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		//허용된 서버가 아니면 봇이 해당 서버를 나감
		if(!FFRoomCoreBotConfig.SERVER_ID.getStrVal().equalsIgnoreCase(event.getGuild().getId())) {
			event.getGuild().leave().queue();
		}
		
	}

	@Override
	public void onGuildReady(GuildReadyEvent event) {
		//허용된 서버가 아니면 봇이 해당 서버를 나감
		if(!FFRoomCoreBotConfig.SERVER_ID.getStrVal().equalsIgnoreCase(event.getGuild().getId())) {
			event.getGuild().leave().queue();
		}
		guild = event.getGuild();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		try {
			User user = event.getAuthor();
			Member member = event.getMember();
			TextChannel tc = event.getTextChannel();
			Message msg = event.getMessage();
			
			//봇이면 무시
			if (user.isBot())
				return;
			if(msg.getContentRaw().charAt(0) == '!') {
				String[] args = msg.getContentRaw().substring(1).split(" ");
				//지정된 어드민이 아니면 해당 명령어를 사용할 수 없음
				if (tc.getId().equalsIgnoreCase(FFRoomCoreBotConfig.ADMIN_UID.getStrVal())){
					if(args[0].equalsIgnoreCase("프린트채널")){
						FFRoomCoreBotConfig.PRINT_TEXT_CH_ID.setVal(args[1]);
					}else if(args[0].equalsIgnoreCase("기준카테고리")){
						FFRoomCoreBotConfig.SEARCH_CATEGORY_ID.setVal(args[1]);						
					}else if(args[0].equalsIgnoreCase("인스턴스카테고리")){
						FFRoomCoreBotConfig.MAKE_CATEGORY_ID.setVal(args[1]);						
					}
				} else if (args.length == 1) return;
				try {
					InstanceRoomInfo instanceRoomInfo = createdVoiceCh.get(member.getVoiceState().getChannel().getId());
					if(!user.getId().equalsIgnoreCase(instanceRoomInfo.getHostUserId())) return; 
					
					if (args[0].equalsIgnoreCase("인원")){
						try {
							member.getVoiceState().getChannel().getManager().setUserLimit(Integer.parseInt(args[1])).complete();
						} catch (NumberFormatException e) {
							
						} catch (IllegalArgumentException e) {
						}
						
					} else if (args[0].equalsIgnoreCase("방제")){
						if(args.length < 2) return;
						String name = "";
						for (int i = 1; i < args.length; i++) {
							name += args[i] + " ";
						}
						try {
							member.getVoiceState().getChannel().getManager().setName(name).complete();
						}catch (java.lang.IllegalArgumentException e) {
							member.getVoiceState().getChannel().getManager().setName(name.substring(0, 100)).complete();
						}
						instanceRoomInfo.setName(name);
						alterEmbed(member);
					} else if (args[0].equalsIgnoreCase("메모")){
						if(args.length < 2) return;
						String memo = "";
						if(!args[1].equalsIgnoreCase("삭제")) {
							for (int i = 1; i < args.length; i++) {
								memo += args[i] + " ";
							}
						}
						instanceRoomInfo.setMemo(memo);
						alterEmbed(member);
					}
					msg.delete().complete();
				}catch (Exception e) {
					return;
				}

			}
		} catch (java.lang.StringIndexOutOfBoundsException e) {
			return;
		}
		
	}
}
