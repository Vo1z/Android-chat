package sirex_production.com;

import java.util.Date;

public class Message
{
    private String userName;
    private String userMessage;
    private long messageTime;

    public Message(){}

    public Message(String userName, String userMessage)
    {
        this.userName = userName;
        this.userMessage = userMessage;
        this.messageTime = new Date().getTime();
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserMessage()
    {
        return userMessage;
    }

    public long getMessageTime()
    {
        return messageTime;
    }
}